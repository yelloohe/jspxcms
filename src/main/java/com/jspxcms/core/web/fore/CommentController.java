package com.jspxcms.core.web.fore;

import static com.jspxcms.core.domain.SiteComment.STATUS_DENIED;
import static com.jspxcms.core.domain.SiteComment.STATUS_LOGIN;
import static com.jspxcms.core.domain.SiteComment.STATUS_OFF;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.common.captcha.Captchas;
import com.jspxcms.common.web.Anchor;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Comment;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.SiteComment;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.CommentService;
import com.jspxcms.core.service.SensitiveWordService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.service.VoteMarkService;
import com.jspxcms.core.support.Commentable;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import com.jspxcms.core.support.SiteResolver;
import com.jspxcms.core.support.Siteable;
import com.octo.captcha.service.CaptchaService;

/**
 * CommentController
 * 
 * @author liufang
 * 
 */
@Controller
public class CommentController {
	public static final String TPL_PREFIX = "sys_comment";
	public static final String TPL_SUFFIX = ".html";
	public static final String TEMPLATE = TPL_PREFIX + TPL_SUFFIX;

	@RequestMapping("/comment")
	public String view(String ftype, Integer fid, Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return view(null, ftype, fid, page, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/comment")
	public String view(@PathVariable String siteNumber, String ftype,
			Integer fid, Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		Site site = Context.getCurrentSite();
		if (StringUtils.isBlank(ftype)) {
			ftype = "Info";
		}
		if (!Validations.notNull(fid, messages, "fid")) {
			return resp.post(401);
		}
		Object bean = service.getEntity(ftype, fid);
		if (!Validations.exist(bean, messages, "CommentObject", ftype + fid)) {
			return resp.post(451);
		}
		Anchor anchor = (Anchor) bean;
		// Site site = ((Siteable) bean).getSite();
		modelMap.addAttribute("anchor", anchor);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(TEMPLATE);
	}

	@RequestMapping("/comment_like")
	public void like(Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		like(null, id, request, response);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/comment_like")
	public void like(@PathVariable String siteNumber, Integer id,
			HttpServletRequest request, HttpServletResponse response) {
		siteResolver.resolveSite(siteNumber);
		if (id == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Comment comment = service.get(id);
		if (comment == null) {
			Servlets.writeHtml(response, "0");
			return;
		}
		Integer userId = Context.getCurrentUserId();
		String ip = Servlets.getRemoteAddr(request);
		String cookie = Site.getIdentityCookie(request, response);
		if (userId != null) {
			if (voteMarkService
					.isUserVoted(Comment.LIKE_MARK, id, userId, null)) {
				Servlets.writeHtml(response, "0");
				return;
			}
		} else if (voteMarkService.isCookieVoted(Comment.LIKE_MARK, id, cookie,
				null)) {
			Servlets.writeHtml(response, "0");
			return;
		}
		voteMarkService.mark(Comment.LIKE_MARK, id, userId, ip, cookie);
		comment.setScore(comment.getScore() + 1);
		service.update(comment);
		String result = Integer.toString(comment.getScore());
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("/comment_submit")
	public String submit(String fname, String ftype, Integer fid,
			Integer parentId, String text, String captcha,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return submit(null, fname, ftype, fid, parentId, text, captcha,
				request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/comment_submit")
	public String submit(@PathVariable String siteNumber, String fname,
			String ftype, Integer fid, Integer parentId, String text,
			String captcha, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		siteResolver.resolveSite(siteNumber);
		User user = Context.getCurrentUser();
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		if (StringUtils.isBlank(fname)) {
			fname = "com.jspxcms.core.domain.InfoComment";
		}
		if (StringUtils.isBlank(ftype)) {
			ftype = Info.COMMENT_TYPE;
		}
		if (!Validations.notNull(fid, messages, "id")) {
			return resp.post(401);
		}
		Object bean = service.getEntity(ftype, fid);
		if (!Validations.exist(bean, messages, fname, fid)) {
			return resp.post(451);
		}
		if (bean instanceof Commentable) {
			Collection<MemberGroup> groups = Context.getCurrentGroups(request);
			int commentStatus = ((Commentable) bean).getCommentStatus(user,
					groups);
			if (commentStatus == STATUS_OFF) {
				return resp.post(503, "comment.off");
			} else if (commentStatus == STATUS_LOGIN) {
				return resp.post(504, "comment.needLogin");
			} else if (commentStatus == STATUS_DENIED) {
				return resp.post(501, "comment.noPermission");
			}
		} else {
			// 不可评论对象
			return resp.post(502, "comment.notAllow");
		}
		Site site = ((Siteable) bean).getSite();
		SiteComment conf = new SiteComment(site.getCustoms());
		if (!Validations.notEmpty(text, conf.getMaxLength(), messages, "text")) {
			resp.post(401);
		}
		if (conf.isNeedCaptcha(user)) {
			if (!Captchas.isValid(captchaService, request, captcha)) {
				return resp.post(100, "error.captcha");
			}
		}

		text = sensitiveWordService.replace(text);
		Comment comment = (Comment) Class.forName(fname).newInstance();
		comment.setFid(fid);
		comment.setText(text);
		comment.setIp(Servlets.getRemoteAddr(request));
		if (conf.isAudit(user)) {
			comment.setStatus(Comment.AUDITED);
			resp.setStatus(0);
		} else {
			comment.setStatus(Comment.SAVED);
			resp.setStatus(1);
		}
		if (user == null) {
			user = userService.getAnonymous();
		}
		service.save(comment, user.getId(), site.getId(), parentId);
		return resp.post();
	}

	@RequestMapping("/comment_list")
	public String list(String ftype, Integer fid, Integer page,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		return list(null, ftype, fid, page, request, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/comment_list")
	public String list(@PathVariable String siteNumber, String ftype,
			Integer fid, Integer page, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		if (StringUtils.isBlank(ftype)) {
			ftype = "Info";
		}
		if (fid == null) {
			// TODO
		}
		Object bean = service.getEntity(ftype, fid);
		if (bean == null) {
			// TODO
		}
		Anchor anchor = (Anchor) bean;
		// Site site = ((Siteable) bean).getSite();
		String tpl = Servlets.getParam(request, "tpl");
		if (StringUtils.isBlank(tpl)) {
			tpl = "_list";
		}
		modelMap.addAttribute("anchor", anchor);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(TPL_PREFIX + tpl + TPL_SUFFIX);
	}

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private SensitiveWordService sensitiveWordService;
	@Autowired
	private VoteMarkService voteMarkService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService service;

}

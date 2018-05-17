package com.jspxcms.ext.web.fore;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.VoteMarkService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import com.jspxcms.core.support.SiteResolver;
import com.jspxcms.ext.domain.Vote;
import com.jspxcms.ext.service.VoteService;

/**
 * VoteController 投票Controller
 * 
 * 投票展示、投票提交、投票结果
 * 
 * @author liufang
 * 
 */
@Controller
public class VoteController {
	public static final String TEMPLATE = "sys_vote.html";
	public static final String VIEW_TEMPLATE = "sys_vote_view.html";

	@RequestMapping("/vote/{id}")
	public String view(@PathVariable("id") Integer id,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return view(null, id, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/vote/{id}")
	public String view(@PathVariable String siteNumber,
			@PathVariable("id") Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		Vote vote = service.get(id);
		if (!Validations.exist(vote, messages, "Vote", id)) {
			return resp.notFound();
		}

		Site currSite = Context.getCurrentSite();
		modelMap.addAttribute("vote", vote);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return currSite.getTemplate(VIEW_TEMPLATE);
	}

	@RequestMapping("/vote")
	public String form(Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return form(null, id, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/vote")
	public String form(@PathVariable String siteNumber, Integer id,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		if (!Validations.notNull(id, messages, "id")) {
			return resp.badRequest();
		}
		Vote vote = service.get(id);
		if (!Validations.exist(vote, messages, "Vote", id)) {
			return resp.notFound();
		}

		modelMap.addAttribute("vote", vote);
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(TEMPLATE);
	}

	@RequestMapping(value = "/vote", method = RequestMethod.POST)
	public String submit(Integer id, Integer[] optionIds,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return submit(null, id, optionIds, request, response, modelMap);
	}

	@RequestMapping(value = Constants.SITE_PREFIX_PATH + "/vote", method = RequestMethod.POST)
	public String submit(@PathVariable String siteNumber, Integer id,
			Integer[] optionIds, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		String cookie = Site.getIdentityCookie(request, response);
		String result = validateSubmit(request, resp, cookie, id, optionIds);
		if (resp.hasErrors()) {
			return result;
		}

		Integer userId = Context.getCurrentUserId();
		String ip = Servlets.getRemoteAddr(request);
		service.vote(id, optionIds, userId, ip, cookie);
		resp.addData("id", id);
		return resp.post();
	}

	private String validateSubmit(HttpServletRequest request, Response resp,
			String coo, Integer id, Integer[] optionIds) {
		List<String> messages = resp.getMessages();
		if (!Validations.notNull(id, messages, "id")) {
			return resp.post(401);
		}
		if (!Validations.notEmpty(optionIds)) {
			return resp.post(402);
		}
		Vote vote = service.get(id);
		if (!Validations.exist(vote, messages, "Vote", id)) {
			return resp.post(451);
		}
		if (vote.getStatus() == Vote.DISABLED_STATUS) {
			return resp.post(510, "vote.desiabled");
		}
		Date now = new Date();
		Date beginDate = vote.getBeginDate();
		if (beginDate != null && beginDate.after(now)) {
			return resp.post(520, "vote.notStarted");
		}
		Date endDate = vote.getEndDate();
		if (endDate != null && endDate.before(now)) {
			return resp.post(530, "vote.ended");
		}
		if (vote.getMaxSelected() > 0
				&& optionIds.length > vote.getMaxSelected()) {
			return resp.post(540, "vote.exceedsMaxSelected");
		}
		Integer userId = null;
		String ip = null;
		String cookie = null;
		if (vote.getMode() == Vote.USER_MODE) {
			User user = Context.getCurrentUser();
			if (user == null) {
				return resp.post(550, "vote.needLogin");
			}
			userId = user.getId();
		} else if (vote.getMode() == Vote.IP_MODE) {
			ip = Servlets.getRemoteAddr(request);
		} else if (vote.getMode() == Vote.COOKIE_MODE) {
			cookie = coo;
		}
		if (userId != null || ip != null || cookie != null) {
			Integer beforeHour = null;
			if (vote.getInterval() != 0) {
				beforeHour = vote.getInterval() * 24;
			}
			if (voteMarkService.isVoted(Vote.MARK_CODE, vote.getId(), userId,
					ip, cookie, beforeHour)) {
				return resp.post(560, "vote.voted");
			}
		}
		return null;
	}

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private VoteMarkService voteMarkService;
	@Autowired
	private VoteService service;
}

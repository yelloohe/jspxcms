package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPERATION_SUCCESS;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Comment;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.CommentService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;

/**
 * CommentController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/comment")
public class CommentController {
	private static final Logger logger = LoggerFactory
			.getLogger(CommentController.class);

	@RequiresPermissions("core:comment:list")
	@RequestMapping("list.do")
	public String list(
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		Page<Comment> pagedList = service.findAll(site.getId(), params,
				pageable);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/comment/comment_list";
	}

	@RequiresPermissions("core:comment:create")
	@RequestMapping("create.do")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		if (id != null) {
			Comment bean = service.get(id);
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "core/comment/comment_form";
	}

	@RequiresPermissions("core:comment:edit")
	@RequestMapping("edit.do")
	public String edit(
			Integer id,
			Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Comment bean = service.get(id);
		Backends.validateDataInSite(bean, site.getId());
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<Comment> side = service.findSide(site.getId(), params, bean,
				position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/comment/comment_form";
	}

	@RequiresPermissions("core:comment:save")
	@RequestMapping("save.do")
	public String save(Comment bean, Integer parentId, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Integer userId = Context.getCurrentUserId();
		service.save(bean, userId, siteId, parentId);
		logService.operation("opr.comment.add",
				StringUtils.substring(bean.getText(), 0, 150), null,
				bean.getId(), request);
		logger.info("save Comment, id={}.", bean.getId());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else if (Constants.REDIRECT_CREATE.equals(redirect)) {
			return "redirect:create.do";
		} else {
			ra.addAttribute("id", bean.getId());
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:comment:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Comment bean,
			Integer position, String redirect, HttpServletRequest request,
			RedirectAttributes ra) {
		Backends.validateDataInSite(bean, Context.getCurrentSiteId());
		service.update(bean);
		logger.info("update Comment, id={}.", bean.getId());
		logService.operation("opr.comment.edit",
				StringUtils.substring(bean.getText(), 0, 150), null,
				bean.getId(), request);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:comment:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Comment[] beans = service.delete(ids);
		for (Comment bean : beans) {
			logService.operation("opr.comment.delete",
					StringUtils.substring(bean.getText(), 0, 150), null,
					bean.getId(), request);
			logger.info("delete Comment, id={}.", bean.getId());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:comment:audit")
	@RequestMapping("audit.do")
	public String audit(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Comment[] beans = service.audit(ids);
		for (Comment bean : beans) {
			logService.operation("opr.comment.audit",
					StringUtils.substring(bean.getText(), 0, 150), null,
					bean.getId(), request);
			logger.info("audit Comment, id={}.", bean.getId());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:comment:anti_audit")
	@RequestMapping("anti_audit.do")
	public String antiAudit(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Comment[] beans = service.unaudit(ids);
		for (Comment bean : beans) {
			logService.operation("opr.comment.antiAudit",
					StringUtils.substring(bean.getText(), 0, 150), null,
					bean.getId(), request);
			logger.info("unaudit Comment, id={}.", bean.getId());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Comment preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	private void validateIds(Integer[] ids, Integer siteId) {
		for (Integer id : ids) {
			Backends.validateDataInSite(service.get(id), siteId);
		}
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private CommentService service;
}

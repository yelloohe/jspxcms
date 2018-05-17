package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.Workflow;
import com.jspxcms.core.domain.WorkflowGroup;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.WorkflowGroupService;
import com.jspxcms.core.service.WorkflowService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;

/**
 * WorkflowController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/workflow")
public class WorkflowController {
	private static final Logger logger = LoggerFactory
			.getLogger(WorkflowController.class);

	@RequiresPermissions("core:workflow:list")
	@RequestMapping("list.do")
	public String list(
			@PageableDefault(sort = { "seq", "id" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		List<Workflow> list = service.findList(siteId, params,
				pageable.getSort());
		List<WorkflowGroup> groupList = groupService.findList(siteId);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("groupList", groupList);
		return "core/workflow/workflow_list";
	}

	@RequiresPermissions("core:workflow:create")
	@RequestMapping("create.do")
	public String create(Integer id, Integer groupId,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		WorkflowGroup group = null;
		if (id != null) {
			Workflow bean = service.get(id);
			Backends.validateDataInSite(bean, siteId);
			group = bean.getGroup();
		}
		if (groupId != null) {
			group = groupService.get(groupId);
		}
		List<WorkflowGroup> groupList = groupService.findList(siteId);
		modelMap.addAttribute("groupList", groupList);
		modelMap.addAttribute("group", group);
		modelMap.addAttribute(OPRT, CREATE);
		return "core/workflow/workflow_form";
	}

	@RequiresPermissions("core:workflow:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position, @PageableDefault(sort = {
			"seq", "id" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Workflow bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<Workflow> side = service.findSide(siteId, params, bean,
				position, pageable.getSort());
		List<WorkflowGroup> groupList = groupService.findList(siteId);
		modelMap.addAttribute("groupList", groupList);
		modelMap.addAttribute("group", bean.getGroup());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/workflow/workflow_form";
	}

	@RequiresPermissions("core:workflow:save")
	@RequestMapping("save.do")
	public String save(Workflow bean, Integer groupId, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		WorkflowGroup group = groupService.get(groupId);
		Backends.validateDataInSite(group, siteId);
		service.save(bean, groupId, siteId);
		logService.operation("opr.workflow.add", bean.getName(), null,
				bean.getId(), request);
		logger.info("save Workflow, name={}.", bean.getName());
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

	@RequiresPermissions("core:workflow:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Workflow bean,
			Integer groupId, Integer position, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		WorkflowGroup group = groupService.get(groupId);
		Backends.validateDataInSite(group, siteId);
		Backends.validateDataInSite(bean, siteId);
		service.update(bean, groupId);
		logService.operation("opr.workflow.edit", bean.getName(), null,
				bean.getId(), request);
		logger.info("update Workflow, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:workflow:update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer[] id, String[] name,
			String[] description, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(id, site.getId());
		List<Workflow> beans = service.batchUpdate(id, name, description);
		for (Workflow bean : beans) {
			logService.operation("opr.workflow.batchEdit", bean.getName(),
					null, bean.getId(), request);
		}
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:workflow:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Workflow[] beans = service.delete(ids);
		for (Workflow bean : beans) {
			logService.operation("opr.workflow.delete", bean.getName(), null,
					bean.getId(), request);
			logger.info("delete Workflow, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Workflow preloadBean(@RequestParam(required = false) Integer oid) {
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
	private WorkflowGroupService groupService;
	@Autowired
	private WorkflowService service;
}

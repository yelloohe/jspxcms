package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Role;
import com.jspxcms.core.domain.Workflow;
import com.jspxcms.core.domain.WorkflowStep;
import com.jspxcms.core.service.RoleService;
import com.jspxcms.core.service.WorkflowService;
import com.jspxcms.core.service.WorkflowStepService;
import com.jspxcms.core.support.Context;

@Controller
@RequestMapping("/core/workflow_step")
public class WorkflowStepController {
	private static final Logger logger = LoggerFactory
			.getLogger(WorkflowStepController.class);

	@RequiresPermissions("core:workflow_step:list")
	@RequestMapping("list.do")
	public String list(Integer workflowId, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Sort sort = new Sort("seq", "id");
		List<WorkflowStep> list = service.findList(workflowId, null, sort);
		Workflow workflow = workflowService.get(workflowId);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("workflow", workflow);
		return "core/workflow_step/workflow_step_list";
	}

	@RequiresPermissions("core:workflow_step:create")
	@RequestMapping("create.do")
	public String create(Integer id, Integer workflowId,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		if (id != null) {
			WorkflowStep bean = service.get(id);
			modelMap.addAttribute("bean", bean);
		}
		Workflow workflow = workflowService.get(workflowId);
		List<Role> roleList = roleService.findList(siteId);
		modelMap.addAttribute(OPRT, CREATE);
		modelMap.addAttribute("workflow", workflow);
		modelMap.addAttribute("roleList", roleList);
		return "core/workflow_step/workflow_step_form";
	}

	@RequiresPermissions("core:workflow_step:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		WorkflowStep bean = service.get(id);
		Workflow workflow = bean.getWorkflow();
		List<Role> roleList = roleService.findList(siteId);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("workflow", workflow);
		modelMap.addAttribute("roleList", roleList);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/workflow_step/workflow_step_form";
	}

	@RequiresPermissions("core:workflow_step:save")
	@RequestMapping("save.do")
	public String save(WorkflowStep bean, Integer[] roleIds,
			Integer workflowId, String redirect, HttpServletRequest request,
			RedirectAttributes ra) {
		service.save(bean, roleIds, workflowId);
		logger.info("save WorkflowStep, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			ra.addAttribute("workflowId", workflowId);
			return "redirect:list.do";
		} else if (Constants.REDIRECT_CREATE.equals(redirect)) {
			ra.addAttribute("workflowId", workflowId);
			return "redirect:create.do";
		} else {
			ra.addAttribute("id", bean.getId());
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:workflow_step:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") WorkflowStep bean,
			Integer[] roleIds, Integer position, String redirect,
			RedirectAttributes ra) {
		service.update(bean, roleIds);
		logger.info("update WorkflowStep, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			ra.addAttribute("workflowId", bean.getWorkflow().getId());
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:workflow_step:update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer workflowId, Integer[] id, String[] name,
			HttpServletRequest request, RedirectAttributes ra) {
		service.batchUpdate(id, name);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addAttribute("workflowId", workflowId);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:workflow_step:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, Integer workflowId,
			RedirectAttributes ra) {
		WorkflowStep[] beans = service.delete(ids);
		for (WorkflowStep bean : beans) {
			logger.info("delete WorkflowStep, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		ra.addAttribute("workflowId", workflowId);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public WorkflowStep preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	@Autowired
	private RoleService roleService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WorkflowStepService service;
}

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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.OrgService;

/**
 * OrgController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/org_global")
public class OrgGlobalController {
	private static final Logger logger = LoggerFactory
			.getLogger(OrgGlobalController.class);

	// @RequiresPermissions("core:org_global:left")
	// @RequestMapping("left.do")
	// public String left(HttpServletRequest request,
	// org.springframework.ui.Model modelMap) {
	// List<Org> list = service.findList(null, null, null, null);
	// modelMap.addAttribute("list", list);
	// return "core/org_global/org_global_left";
	// }

	@RequestMapping("list.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:org_global:list")
	public String list(
			Integer queryParentId,
			@RequestParam(defaultValue = "true") boolean showDescendants,
			@PageableDefault(sort = "treeNumber", direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		List<Org> list = service.findList(null, queryParentId, showDescendants,
				params, pageable.getSort());
		List<Org> orgList = service.findList();
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("orgList", orgList);
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		return "core/org_global/org_global_list";
	}

	@RequestMapping("create.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:org_global:create")
	public String create(Integer id, Integer parentId, Integer queryParentId,
			Boolean showDescendants, org.springframework.ui.Model modelMap) {
		Org bean = null, parent = null;
		if (id != null) {
			bean = service.get(id);
		}
		if (bean != null) {
			parent = bean.getParent();
		} else if (parentId != null) {
			parent = service.get(parentId);
		}
		modelMap.addAttribute(OPRT, CREATE);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("parent", parent);
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		return "core/org_global/org_global_form";
	}

	@RequestMapping("edit.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:org_global:edit")
	public String edit(
			Integer id,
			Integer queryParentId,
			@RequestParam(defaultValue = "true") boolean showDescendants,
			Integer position,
			@PageableDefault(sort = "treeNumber", direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Org bean = service.get(id);
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<Org> side = service.findSide(null, queryParentId,
				showDescendants, params, bean, position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("parent", bean.getParent());
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/org_global/org_global_form";
	}

	@RequestMapping("save.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:org_global:save")
	public String save(Org bean, Integer parentId, Integer queryParentId,
			Boolean showDescendants, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		service.save(bean, parentId);
		logService.operation("opr.org.add", bean.getName(), null, bean.getId(),
				request);
		logger.info("save Org, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addAttribute("parentId", parentId);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else if (Constants.REDIRECT_CREATE.equals(redirect)) {
			return "redirect:create.do";
		} else {
			ra.addAttribute("id", bean.getId());
			return "redirect:edit.do";
		}
	}

	@RequestMapping("update.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:org_global:update")
	public String update(@ModelAttribute("bean") Org bean, Integer parentId,
			Integer queryParentId, Boolean showDescendants, Integer position,
			String redirect, HttpServletRequest request, RedirectAttributes ra) {
		service.update(bean, parentId);
		logService.operation("opr.org.edit", bean.getName(), null,
				bean.getId(), request);
		logger.info("update Org, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequestMapping("batch_update.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:org_global:batch_update")
	public String batchUpdate(Integer[] id, String[] name, String[] number,
			String[] phone, String[] address, Integer queryParentId,
			Boolean showDescendants, Pageable pageable,
			HttpServletRequest request, RedirectAttributes ra) {
		if (ArrayUtils.isNotEmpty(id)) {
			// 有排序的情况下不更新树结构，以免引误操作。
			boolean isUpdateTree = pageable.getSort() == null;
			Org[] beans = service.batchUpdate(id, name, number, phone, address,
					isUpdateTree);
			for (Org bean : beans) {
				logService.operation("opr.org.batchEdit", bean.getName(), null,
						bean.getId(), request);
				logger.info("update Org, name={}.", bean.getName());
			}
		}
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		ra.addFlashAttribute("refreshLeft", true);
		return "redirect:list.do";
	}

	@RequestMapping("delete.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:org_global:delete")
	public String delete(Integer[] ids, Integer queryParentId,
			Boolean showDescendants, HttpServletRequest request,
			RedirectAttributes ra) {
		Org[] beans = service.delete(ids);
		for (Org bean : beans) {
			logService.operation("opr.org.delete", bean.getName(), null,
					bean.getId(), request);
			logger.info("delete Org, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Org preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private OrgService service;
}

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
import org.springframework.beans.BeanUtils;
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
import com.jspxcms.core.domain.Role;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.RoleService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;

/**
 * RoleController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/role")
public class RoleController {
	private static final Logger logger = LoggerFactory
			.getLogger(RoleController.class);

	@RequiresPermissions("core:role:list")
	@RequestMapping("list.do")
	public String list(
			@PageableDefault(sort = { "seq", "id" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		List<Role> list = service.findList(siteId, params, pageable.getSort());
		modelMap.addAttribute("list", list);
		return "core/role/role_list";
	}

	@RequiresPermissions("core:role:create")
	@RequestMapping("create.do")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		if (id != null) {
			Role bean = service.get(id);
			Backends.validateDataInSite(bean, site.getId());
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "core/role/role_form";
	}

	@RequiresPermissions("core:role:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position, @PageableDefault(sort = {
			"seq", "id" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Role bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<Role> side = service.findSide(siteId, params, bean, position,
				pageable.getSort());
		modelMap.addAttribute("infoPerms", bean.getInfoPerms());
		modelMap.addAttribute("nodePerms", bean.getNodePerms());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/role/role_form";
	}

	@RequiresPermissions("core:role:save")
	@RequestMapping("save.do")
	public String save(Role bean, Integer[] infoPermIds, Integer[] nodePermIds,
			String redirect, HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		service.save(bean, infoPermIds, nodePermIds, siteId);
		logService.operation("opr.role.add", bean.getName(), null,
				bean.getId(), request);
		logger.info("save Role, name={}.", bean.getName());
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

	@RequiresPermissions("core:role:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Role bean,
			Integer[] infoPermIds, Integer[] nodePermIds, Integer position,
			String redirect, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		Backends.validateDataInSite(bean, site.getId());
		// 如果拥有所有权限，则清空权限字符串。
		if (bean.getAllPerm()) {
			bean.setPerms(null);
		}
		if (infoPermIds == null) {
			infoPermIds = new Integer[0];
		}
		if (nodePermIds == null) {
			nodePermIds = new Integer[0];
		}
		service.update(bean, infoPermIds, nodePermIds);
		logService.operation("opr.role.edit", bean.getName(), null,
				bean.getId(), request);
		logger.info("update Role, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:role:update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer[] id, String[] name, Integer[] rank,
			String[] description, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(id, site.getId());
		List<Role> beans = service.batchUpdate(id, name, rank, description);
		for (Role bean : beans) {
			logService.operation("opr.role.batchEdit", bean.getName(), null,
					bean.getId(), request);
			logger.info("batch update Role. name={}", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:role:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Role[] beans = service.delete(ids);
		for (Role bean : beans) {
			logService.operation("opr.role.delete", bean.getName(), null,
					bean.getId(), request);
			logger.info("delete Role, name={}", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Role preloadBean(@RequestParam(required = false) Integer oid) {
		if (oid != null) {
			Role bean = service.get(oid);
			Role obj = new Role();
			BeanUtils.copyProperties(bean, obj);
			return obj;
		} else {
			return null;
		}
	}

	private void validateIds(Integer[] ids, Integer siteId) {
		for (Integer id : ids) {
			Backends.validateDataInSite(service.get(id), siteId);
		}
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private RoleService service;
}

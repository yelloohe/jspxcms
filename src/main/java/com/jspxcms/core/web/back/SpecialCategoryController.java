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
import com.jspxcms.core.domain.SpecialCategory;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.SpecialCategoryService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;

/**
 * SpecialCategoryController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/special_category")
public class SpecialCategoryController {
	private static final Logger logger = LoggerFactory
			.getLogger(SpecialCategoryController.class);

	@RequestMapping("list.do")
	@RequiresPermissions("core:special_category:list")
	public String list(
			@PageableDefault(sort = { "seq", "id" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		List<SpecialCategory> list = service.findList(siteId, params,
				pageable.getSort());
		modelMap.addAttribute("list", list);
		return "core/special_category/special_category_list";
	}

	@RequestMapping("create.do")
	@RequiresPermissions("core:special_category:create")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		if (id != null) {
			SpecialCategory bean = service.get(id);
			Backends.validateDataInSite(bean, site.getId());
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "core/special_category/special_category_form";
	}

	@RequiresPermissions("core:special_category:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position, @PageableDefault(sort = {
			"seq", "id" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		SpecialCategory bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<SpecialCategory> side = service.findSide(siteId, params, bean,
				position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/special_category/special_category_form";
	}

	@RequestMapping("save.do")
	@RequiresPermissions("core:special_category:save")
	public String save(SpecialCategory bean, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		service.save(bean, siteId);
		logService.operation("opr.specialCategory.add", bean.getName(), null,
				bean.getId(), request);
		logger.info("save SpecialCategory, name={}.", bean.getName());
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

	@RequestMapping("update.do")
	@RequiresPermissions("core:special_category:update")
	public String update(@ModelAttribute("bean") SpecialCategory bean,
			Integer position, String redirect, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		Backends.validateDataInSite(bean, site.getId());
		service.update(bean);
		Backends.validateDataInSite(bean, site.getId());
		logService.operation("opr.specialCategory.edit", bean.getName(), null,
				bean.getId(), request);
		logger.info("update SpecialCategory, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequestMapping("batch_update.do")
	@RequiresPermissions("core:special_category:batch_update")
	public String batchUpdate(Integer[] id, String[] name, Integer[] views,
			HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(id, site.getId());
		if (ArrayUtils.isNotEmpty(id)) {
			SpecialCategory[] beans = service.batchUpdate(id, name, views);
			for (SpecialCategory bean : beans) {
				logService.operation("opr.specialCategory.batchEdit",
						bean.getName(), null, bean.getId(), request);
				logger.info("update SpecialCategory, name={}.", bean.getName());
			}
		}
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequestMapping("delete.do")
	@RequiresPermissions("core:special_category:delete")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		SpecialCategory[] beans = service.delete(ids);
		for (SpecialCategory bean : beans) {
			logService.operation("opr.specialCategory.delete", bean.getName(),
					null, bean.getId(), request);
			logger.info("delete SpecialCategory, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public SpecialCategory preloadBean(
			@RequestParam(required = false) Integer oid) {
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
	private SpecialCategoryService service;
}

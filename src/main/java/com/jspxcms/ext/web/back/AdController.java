package com.jspxcms.ext.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;
import com.jspxcms.ext.domain.Ad;
import com.jspxcms.ext.domain.AdSlot;
import com.jspxcms.ext.service.AdService;
import com.jspxcms.ext.service.AdSlotService;

@Controller
@RequestMapping("/ext/ad")
public class AdController {
	private static final Logger logger = LoggerFactory
			.getLogger(AdController.class);

	@RequiresPermissions("ext:ad:list")
	@RequestMapping("list.do")
	public String list(
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		List<Ad> list = service.findList(siteId, params, pageable.getSort());
		List<AdSlot> slotList = slotService.findList(siteId);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("slotList", slotList);
		return "ext/ad/ad_list";
	}

	@RequiresPermissions("ext:ad:create")
	@RequestMapping("create.do")
	public String create(Integer id, Integer slotId,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		if (id != null) {
			Ad bean = service.get(id);
			Backends.validateDataInSite(bean, siteId);
			modelMap.addAttribute("bean", bean);
		}
		List<AdSlot> slotList = slotService.findList(siteId);
		if (CollectionUtils.isEmpty(slotList)) {
			throw new CmsException("ad.error.slotNotFound");
		}
		AdSlot slot;
		if (slotId != null) {
			slot = slotService.get(slotId);
		} else {
			slot = slotList.get(0);
		}
		modelMap.addAttribute("slotList", slotList);
		modelMap.addAttribute("slot", slot);
		modelMap.addAttribute(OPRT, CREATE);
		return "ext/ad/ad_form";
	}

	@RequiresPermissions("ext:ad:edit")
	@RequestMapping("edit.do")
	public String edit(
			Integer id,
			Integer querySlotId,
			Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Ad bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<Ad> side = service.findSide(siteId, params, bean, position,
				pageable.getSort());
		List<AdSlot> slotList = slotService.findList(siteId);
		AdSlot slot = bean.getSlot();
		if (querySlotId != null) {
			slot = slotService.get(querySlotId);
		}
		modelMap.addAttribute("slotList", slotList);
		modelMap.addAttribute("slot", slot);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "ext/ad/ad_form";
	}

	@RequiresPermissions("ext:ad:save")
	@RequestMapping("save.do")
	public String save(Ad bean, Integer slotId, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		AdSlot slot = slotService.get(slotId);
		Backends.validateDataInSite(slot, siteId);
		service.save(bean, slotId, siteId);
		logService.operation("opr.ad.add", bean.getName(), null, bean.getId(),
				request);
		logger.info("save Ad, name={}.", bean.getName());
		ra.addAttribute("querySlotId", slotId);
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

	@RequiresPermissions("ext:ad:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Ad bean, Integer slotId,
			Integer position, String redirect, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		AdSlot slot = slotService.get(slotId);
		Backends.validateDataInSite(slot, site.getId());
		Backends.validateDataInSite(bean, site.getId());
		service.update(bean, slotId);
		logService.operation("opr.ad.edit", bean.getName(), null, bean.getId(),
				request);
		logger.info("update Ad, name={}.", bean.getName());
		ra.addAttribute("querySlotId", slotId);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("ext:ad:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, Integer querySlotId,
			HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Ad[] beans = service.delete(ids);
		for (Ad bean : beans) {
			logService.operation("opr.ad.delete", bean.getName(), null,
					bean.getId(), request);
			logger.info("delete Ad, name={}.", bean.getName());
		}
		ra.addAttribute("querySlotId", querySlotId);
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Ad preloadBean(@RequestParam(required = false) Integer oid) {
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
	private AdSlotService slotService;
	@Autowired
	private AdService service;
}

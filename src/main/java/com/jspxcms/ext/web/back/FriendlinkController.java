package com.jspxcms.ext.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPERATION_SUCCESS;
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
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;
import com.jspxcms.ext.domain.Friendlink;
import com.jspxcms.ext.domain.FriendlinkType;
import com.jspxcms.ext.service.FriendlinkService;
import com.jspxcms.ext.service.FriendlinkTypeService;

/**
 * FriendlinkController
 * 
 * @author yangxing
 * 
 */

@Controller
@RequestMapping("/ext/friendlink")
public class FriendlinkController {
	private static final Logger logger = LoggerFactory
			.getLogger(FriendlinkController.class);

	@RequiresPermissions("ext:friendlink:list")
	@RequestMapping("list.do")
	public String list(HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		Integer siteId = Context.getCurrentSiteId();
		List<Friendlink> list = service.findList(siteId, params);
		modelMap.addAttribute("list", list);
		List<FriendlinkType> typeList = friendlinkTypeService.findList(siteId);
		modelMap.addAttribute("typeList", typeList);
		return "ext/friendlink/friendlink_list";
	}

	@RequiresPermissions("ext:friendlink:create")
	@RequestMapping("create.do")
	public String create(Integer id, Integer typeId,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		FriendlinkType type = null;
		if (id != null) {
			Friendlink bean = service.get(id);
			Backends.validateDataInSite(bean, siteId);
			type = bean.getType();
			modelMap.addAttribute("bean", bean);
		}
		if (typeId != null) {
			type = friendlinkTypeService.get(typeId);
		}
		List<FriendlinkType> typeList = friendlinkTypeService.findList(siteId);
		modelMap.addAttribute("typeList", typeList);
		modelMap.addAttribute("type", type);
		modelMap.addAttribute(OPRT, CREATE);
		return "ext/friendlink/friendlink_form";
	}

	@RequiresPermissions("ext:friendlink:edit")
	@RequestMapping("edit.do")
	public String edit(
			Integer id,
			Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Friendlink bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<Friendlink> side = service.findSide(siteId, params, bean,
				position);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("type", bean.getType());
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		List<FriendlinkType> typeList = friendlinkTypeService.findList(siteId);
		modelMap.addAttribute("typeList", typeList);
		modelMap.addAttribute(OPRT, EDIT);
		return "ext/friendlink/friendlink_form";
	}

	@RequiresPermissions("ext:friendlink:save")
	@RequestMapping("save.do")
	public String save(Friendlink bean, Integer typeId, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		FriendlinkType type = friendlinkTypeService.get(typeId);
		Backends.validateDataInSite(type, siteId);
		service.save(bean, typeId, siteId);
		logService.operation("opr.friendlink.add", bean.getName(), null,
				bean.getId(), request);
		logger.info("save Friendlink, name={}.", bean.getName());
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

	@RequiresPermissions("ext:friendlink:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Friendlink bean,
			Integer typeId, Integer position, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		FriendlinkType type = friendlinkTypeService.get(typeId);
		Backends.validateDataInSite(type, site.getId());
		Backends.validateDataInSite(bean, site.getId());
		service.update(bean, typeId);
		logService.operation("opr.friendlink.edit", bean.getName(), null,
				bean.getId(), request);
		logger.info("update Friendlink, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("ext:friendlink:batch_update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer[] id, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(id, site.getId());
		if (ArrayUtils.isNotEmpty(id)) {
			Friendlink[] beans = service.batchUpdate(id);
			for (Friendlink bean : beans) {
				logService.operation("opr.friendlink.batchEdit",
						bean.getName(), null, bean.getId(), request);
				logger.info("update Friendlink, name={}.", bean.getName());
			}
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("ext:friendlink:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Friendlink[] beans = service.delete(ids);
		for (Friendlink bean : beans) {
			logService.operation("opr.friendlink.delete", bean.getName(), null,
					bean.getId(), request);
			logger.info("delete Friendlink, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Friendlink preloadBean(@RequestParam(required = false) Integer oid) {
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
	private FriendlinkTypeService friendlinkTypeService;
	@Autowired
	private FriendlinkService service;
}

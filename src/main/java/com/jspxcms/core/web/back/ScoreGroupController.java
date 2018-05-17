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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.ScoreGroup;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.ScoreGroupService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;

@Controller
@RequestMapping("/core/score_group")
public class ScoreGroupController {
	private static final Logger logger = LoggerFactory
			.getLogger(ScoreGroupController.class);

	@RequiresPermissions("core:score_group:list")
	@RequestMapping("list.do")
	public String list(
			@PageableDefault(sort = { "seq", "id" }) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		List<ScoreGroup> list = service.findList(site.getId(), params,
				pageable.getSort());
		modelMap.addAttribute("list", list);
		return "core/score_group/score_group_list";
	}

	@RequiresPermissions("core:score_group:create")
	@RequestMapping("create.do")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		if (id != null) {
			ScoreGroup bean = service.get(id);
			Backends.validateDataInSite(bean, site.getId());
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "core/score_group/score_group_form";
	}

	@RequiresPermissions("core:score_group:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position, @PageableDefault(sort = {
			"seq", "id" }) Pageable pageable, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		ScoreGroup bean = service.get(id);
		Backends.validateDataInSite(bean, site.getId());
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<ScoreGroup> side = service.findSide(site.getId(), params, bean,
				position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/score_group/score_group_form";
	}

	@RequiresPermissions("core:score_group:save")
	@RequestMapping("save.do")
	public String save(ScoreGroup bean, String[] itemName, Integer[] itemScore,
			String[] itemIcon, String redirect, HttpServletRequest request,
			RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Backends.validateDataInSite(bean, siteId);
		service.save(bean, itemName, itemScore, itemIcon, siteId);
		logService.operation("opr.scoreGroup.add", bean.getName(), null,
				bean.getId(), request);
		logger.info("save ScoreGroup, name={}.", bean.getName());
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

	@RequiresPermissions("core:score_group:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") ScoreGroup bean,
			Integer[] itemId, String[] itemName, Integer[] itemScore,
			String[] itemIcon, Integer position, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		Backends.validateDataInSite(bean, site.getId());
		service.update(bean, itemId, itemName, itemScore, itemIcon);
		logService.operation("opr.scoreGroup.edit", bean.getName(), null,
				bean.getId(), request);
		logger.info("update ScoreGroup, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:score_group:batch_update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer[] id, String[] name, String[] number,
			String[] description, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(id, site.getId());
		if (ArrayUtils.isNotEmpty(id)) {
			ScoreGroup[] beans = service.batchUpdate(id, name, number,
					description);
			for (ScoreGroup bean : beans) {
				logService.operation("opr.scoreGroup.batchEdit",
						bean.getName(), null, bean.getId(), request);
				logger.info("update ScoreGroup, name={}.", bean.getName());
			}
		}
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:score_group:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		ScoreGroup[] beans = service.delete(ids);
		for (ScoreGroup bean : beans) {
			logService.operation("opr.scoreGroup.delete", bean.getName(), null,
					bean.getId(), request);
			logger.info("delete ScoreGroup, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	/**
	 * 检查编号是否存在
	 */
	@RequestMapping("check_number.do")
	public void checkNumber(String number, String original,
			HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isBlank(number) || StringUtils.equals(number, original)) {
			Servlets.writeHtml(response, "true");
			return;
		}
		// 检查数据库是否重名
		Integer siteId = Context.getCurrentSiteId();
		String result = service.numberExist(number, siteId) ? "false" : "true";
		Servlets.writeHtml(response, result);
	}

	@ModelAttribute("bean")
	public ScoreGroup preloadBean(@RequestParam(required = false) Integer oid) {
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
	private ScoreGroupService service;
}

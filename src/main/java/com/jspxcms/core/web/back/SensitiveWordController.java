package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
import com.jspxcms.core.domain.SensitiveWord;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.SensitiveWordService;
import com.jspxcms.core.support.Context;

@Controller
@RequestMapping("/core/sensitive_word")
public class SensitiveWordController {
	private static final Logger logger = LoggerFactory
			.getLogger(SensitiveWordController.class);

	@RequestMapping("list.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:sensitive_word:list")
	public String list(
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		Page<SensitiveWord> pagedList = service.findAll(params, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/sensitive_word/sensitive_word_list";
	}

	@RequestMapping("create.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:sensitive_word:create")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		if (id != null) {
			SensitiveWord bean = service.get(id);
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "core/sensitive_word/sensitive_word_form";
	}

	@RequestMapping("edit.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:sensitive_word:edit")
	public String edit(
			Integer id,
			Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		SensitiveWord bean = service.get(id);
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<SensitiveWord> side = service.findSide(params, bean, position,
				pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/sensitive_word/sensitive_word_form";
	}

	@RequestMapping("save.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:sensitive_word:save")
	public String save(SensitiveWord bean, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		service.save(bean, siteId);
		logService.operation("opr.sensitiveWord.add", bean.getName(), null,
				bean.getId(), request);
		logger.info("save SensitiveWord, name={}.", bean.getName());
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
	@RequiresRoles("super")
	@RequiresPermissions("core:sensitive_word:update")
	public String update(@ModelAttribute("bean") SensitiveWord bean,
			Integer position, String redirect, HttpServletRequest request,
			RedirectAttributes ra) {
		service.update(bean);
		logService.operation("opr.sensitiveWord.edit", bean.getName(), null,
				bean.getId(), request);
		logger.info("update SensitiveWord, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequestMapping("delete.do")
	@RequiresRoles("super")
	@RequiresPermissions("core:sensitive_word:delete")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		SensitiveWord[] beans = service.delete(ids);
		for (SensitiveWord bean : beans) {
			logService.operation("opr.sensitiveWord.delete", bean.getName(),
					null, bean.getId(), request);
			logger.info("delete SensitiveWord, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public SensitiveWord preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private SensitiveWordService service;
}

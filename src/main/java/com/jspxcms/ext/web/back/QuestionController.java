package com.jspxcms.ext.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.service.QuestionService;

@Controller
@RequestMapping("/ext/question")
public class QuestionController {
	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

	@RequiresPermissions("ext:question:list")
	@RequestMapping("list.do")
	public String list(@PageableDefault(sort = { "creationDate", "id" }, direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		Page<Question> pagedList = service.findAll(site.getId(), params, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		return "ext/question/question_list";
	}

	@RequiresPermissions("ext:question:view")
	@RequestMapping("view.do")
	public String view(Integer id, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Question bean = service.get(id);
		Backends.validateDataInSite(bean, site.getId());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute(OPRT, CREATE);
		return "ext/question/question_view";
	}

	@RequiresPermissions("ext:question:create")
	@RequestMapping("create.do")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		if (id != null) {
			Question bean = service.get(id);
			Backends.validateDataInSite(bean, site.getId());
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "ext/question/question_form";
	}

	@RequiresPermissions("ext:question:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position,
			@PageableDefault(sort = { "creationDate", "id" }, direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Question bean = service.get(id);
		Backends.validateDataInSite(bean, site.getId());
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		RowSide<Question> side = service.findSide(site.getId(), params, bean, position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "ext/question/question_form";
	}

	@RequiresPermissions("ext:question:save")
	@RequestMapping("save.do")
	public String save(Question bean, String[] itemTitle, Boolean[] itemEssay, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		service.save(bean, itemTitle, itemEssay, siteId);
		logService.operation("opr.question.add", bean.getTitle(), null, bean.getId(), request);
		logger.info("save Question, title={}.", bean.getTitle());
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

	@RequiresPermissions("ext:question:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Question bean, Integer[] itemId, String[] itemTitle,
			Boolean[] itemEssay, Integer position, String redirect, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		Backends.validateDataInSite(bean, site.getId());
		service.update(bean, itemId, itemTitle, itemEssay);
		logService.operation("opr.question.edit", bean.getTitle(), null, bean.getId(), request);
		logger.info("update Question, title={}.", bean.getTitle());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("ext:question:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Question[] beans = service.delete(ids);
		for (Question bean : beans) {
			logService.operation("opr.question.delete", bean.getTitle(), null, bean.getId(), request);
			logger.info("delete Question, title={}.", bean.getTitle());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Question preloadBean(@RequestParam(required = false) Integer oid) {
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
	private QuestionService service;
}

package com.jspxcms.ext.web.back;

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
import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.service.QuestionItemService;

@Controller
@RequestMapping("/ext/question_item")
public class QuestionItemController {
	private static final Logger logger = LoggerFactory
			.getLogger(QuestionItemController.class);

	@RequiresPermissions("ext:question:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position, @PageableDefault(sort = {
			"seq", "id" }, direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		QuestionItem bean = service.get(id);
		Backends.validateDataInSite(bean.getQuestion(), site.getId());
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<QuestionItem> side = service.findSide(params, bean, position,
				pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("question", bean.getQuestion());
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "ext/question_item/question_item_form";
	}

	@RequiresPermissions("ext:question:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") QuestionItem bean,
			Integer[] optionId, String[] optionTitle, Integer position,
			String redirect, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		Backends.validateDataInSite(bean.getQuestion(), site.getId());
		service.update(bean, optionId, optionTitle);
		logService.operation("opr.questionItem.edit", bean.getTitle(), null,
				bean.getId(), request);
		logger.info("update Question, title={}.", bean.getTitle());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			ra.addAttribute("id", bean.getQuestion().getId());
			ra.addAttribute("position", position);
			return "redirect:../question/edit.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@ModelAttribute("bean")
	public QuestionItem preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private QuestionItemService service;
}

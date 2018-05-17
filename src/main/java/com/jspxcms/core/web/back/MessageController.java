package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.jspxcms.core.domain.Message;
import com.jspxcms.core.domain.MessageText;
import com.jspxcms.core.service.MessageService;

@Controller
@RequestMapping("/core/message")
public class MessageController {
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@RequiresPermissions("core:message:list")
	@RequestMapping("list.do")
	public String list(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		Page<Message> pagedList = service.findAll(params, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/message/message_list";
	}

	@RequiresPermissions("core:message:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Message bean = service.get(id);
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		RowSide<Message> side = service.findSide(params, bean, position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/message/message_form";
	}

	@RequiresPermissions("core:message:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Message bean, @ModelAttribute("messageText") MessageText messageText,
			Integer position, String redirect, RedirectAttributes ra) {
		service.update(bean);
		logger.info("update Message, text={}.", StringUtils.substring(bean.getText(), 0, 50));
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:message:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, RedirectAttributes ra) {
		List<Message> beans = service.delete(ids);
		for (Message bean : beans) {
			logger.info("delete Message, text={}.", StringUtils.substring(bean.getText(), 0, 50));
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute
	public void preloadBean(@RequestParam(required = false) Integer oid, org.springframework.ui.Model modelMap) {
		if (oid != null) {
			Message bean = service.get(oid);
			modelMap.addAttribute("bean", bean);
			if (bean != null) {
				MessageText messageText = bean.getMessageText();
				modelMap.addAttribute("messageText", messageText);
			}
		}
	}

	@Autowired
	private MessageService service;
}

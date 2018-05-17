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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.MailOutbox;
import com.jspxcms.core.domain.MailText;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.MailOutboxService;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.Context;

@Controller
@RequestMapping("/core/mail_outbox")
public class MailOutboxController {
	private static final Logger logger = LoggerFactory.getLogger(MailOutboxController.class);

	@RequiresPermissions("core:mail_outbox:list")
	@RequestMapping("list.do")
	public String list(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		Page<MailOutbox> pagedList = service.findAll(params, pageable);
		List<MemberGroup> groupList = groupService.findRealGroups();
		modelMap.addAttribute("pagedList", pagedList);
		modelMap.addAttribute("groupList", groupList);
		return "core/mail_outbox/mail_outbox_list";
	}

	@RequiresPermissions("core:mail_outbox:send")
	@RequestMapping("send.do")
	public String send(@RequestParam(defaultValue = "false") boolean allReceive, String receiverUsername,
			Integer[] receiverGroupIds, MailText mailText, String redirect, HttpServletRequest request,
			RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		MailOutbox bean = service.send(receiverUsername, receiverGroupIds, allReceive, user, mailText);
		logger.info("save MailOutbox, title={}.", bean.getTitle());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:mail_outbox:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		MailOutbox bean = service.get(id);
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		RowSide<MailOutbox> side = service.findSide(params, bean, position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/mail_outbox/mail_outbox_form";
	}

	@RequiresPermissions("core:mail_outbox:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") MailOutbox bean, @ModelAttribute("mailText") MailText mailText,
			Integer position, String redirect, RedirectAttributes ra) {
		service.update(bean);
		logger.info("update MailOutbox, title={}.", bean.getTitle());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:mail_outbox:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, RedirectAttributes ra) {
		List<MailOutbox> beans = service.delete(ids);
		for (MailOutbox bean : beans) {
			logger.info("delete MailOutbox, title={}.", bean.getTitle());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	/**
	 * 检查接收人
	 */
	@RequestMapping(value = "check_receiver.do")
	@ResponseBody
	public String checkReceiver(String receiverUsername) {
		return String.valueOf(doCheckReceiver(receiverUsername));
	}

	/**
	 * 检查接收人是否合法
	 * 
	 * @param receiverUsername
	 *            接收人用户名
	 * @return
	 */
	private boolean doCheckReceiver(String receiverUsername) {
		if (StringUtils.isBlank(receiverUsername)) {
			return false;
		}
		User receiver = userService.findByUsername(receiverUsername);
		// 用户不存在
		if (receiver == null) {
			return false;
		}
		// 匿名用户不能接收站内信
		if (receiver.isAnonymous()) {
			return false;
		}
		return true;
	}

	@ModelAttribute
	public void preloadBean(@RequestParam(required = false) Integer oid, org.springframework.ui.Model modelMap) {
		if (oid != null) {
			MailOutbox bean = service.get(oid);
			modelMap.addAttribute("bean", bean);
			if (bean != null) {
				MailText mailText = bean.getMailText();
				modelMap.addAttribute("mailText", mailText);
			}
		}
	}

	@Autowired
	private UserService userService;
	@Autowired
	private MemberGroupService groupService;
	@Autowired
	private MailOutboxService service;
}

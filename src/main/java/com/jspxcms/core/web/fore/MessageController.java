package com.jspxcms.core.web.fore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.MailInbox;
import com.jspxcms.core.domain.Message;
import com.jspxcms.core.domain.MessageText;
import com.jspxcms.core.domain.Notification;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.MailInboxService;
import com.jspxcms.core.service.MessageService;
import com.jspxcms.core.service.NotificationService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;

/**
 * MessageController
 * 
 * @author liufang
 * 
 */
@Controller
public class MessageController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String MESSAGE_TEMPLATE = "sys_member_message.html";
	public static final String MESSAGE_SEND_TEMPLATE = "sys_member_message_send.html";
	public static final String MESSAGE_CONTACT_TEMPLATE = "sys_member_message_contact.html";
	public static final String INBOX_TEMPLATE = "sys_member_inbox.html";
	public static final String INBOX_SHOW_TEMPLATE = "sys_member_inbox_show.html";
	public static final String NOTIFICATION_TEMPLATE = "sys_member_notification.html";

	@RequestMapping(value = { "/my/message", Constants.SITE_PREFIX_PATH + "/my/message" })
	public String message(Integer page, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(MESSAGE_TEMPLATE);
	}

	@RequestMapping(value = { "/my/message/send", Constants.SITE_PREFIX_PATH + "/my/message/send" })
	public String messageSendForm(Integer page, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(MESSAGE_SEND_TEMPLATE);
	}

	@RequestMapping(value = { "/my/message/send",
			Constants.SITE_PREFIX_PATH + "/my/message/send" }, method = RequestMethod.POST)
	public String messageSend(String receiverUsername, MessageText messageText, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		User user = Context.getCurrentUser();
		if (!doCheckReceiver(receiverUsername)) {
			return resp.post(501, "message.receiver.reject");
		}
		User receiver = userService.findByUsername(receiverUsername);
		messageService.send(user.getId(), receiver.getId(), messageText);
		return resp.post();
	}

	/**
	 * 检查接收人
	 */
	@RequestMapping(value = { "/my/message/check_receiver",
			Constants.SITE_PREFIX_PATH + "/my/message/check_receiver" })
	@ResponseBody
	public String checkReceiver(String receiverUsername) {
		return String.valueOf(doCheckReceiver(receiverUsername));
	}

	private boolean doCheckReceiver(String receiverUsername) {
		User user = Context.getCurrentUser();
		// 用户名为空，或者与自己一样，则不能发送邮件
		if (StringUtils.isBlank(receiverUsername) || StringUtils.equals(receiverUsername, user.getUsername())) {
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

	@RequestMapping(value = { "/my/message/contact/{id}",
			Constants.SITE_PREFIX_PATH + "/my/message/contact/{id}" })
	public String messageContact(@PathVariable Integer id, Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		User contact = userService.get(id);
		messageService.setRead(user.getId(), id);
		data.put("contact", contact);
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(MESSAGE_CONTACT_TEMPLATE);
	}

	/**
	 * 删除消息
	 * 
	 * @param ids
	 *            消息ID数组
	 * @param page
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = { "/my/message/delete", Constants.SITE_PREFIX_PATH + "/my/message/delete" })
	public String messageDelete(Integer[] ids, Integer page, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		if (ArrayUtils.isEmpty(ids)) {
			return resp.badRequest();
		}
		User user = Context.getCurrentUser();
		for (Integer id : ids) {
			Message bean = messageService.get(id);
			// 不是自己发送的，也不是自己接收的，不能删除
			if (!bean.getSender().getId().equals(user.getId()) && !bean.getReceiver().getId().equals(user.getId())) {
				return resp.post(501, "error.forbiddenData");
			}
		}
		messageService.deleteById(ids, user.getId());
		return resp.post();
	}

	/**
	 * 删除联系人
	 * 
	 * @param ids
	 *            联系人ID数组
	 * @param page
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = { "/my/message/delete_by_contact",
			Constants.SITE_PREFIX_PATH + "/my/message/delete_by_contact" })
	public String messageDeleteByContact(Integer[] ids, Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		if (ArrayUtils.isEmpty(ids)) {
			return resp.badRequest();
		}
		User user = Context.getCurrentUser();
		for (Integer id : ids) {
			messageService.deleteByContactId(user.getId(), id);
		}
		return resp.post();
	}

	@RequestMapping(value = { "/my/inbox", Constants.SITE_PREFIX_PATH + "/my/inbox" })
	public String inbox(Integer page, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(INBOX_TEMPLATE);
	}

	@RequestMapping(value = { "/my/inbox/{id}", Constants.SITE_PREFIX_PATH + "/my/inbox/{id}" })
	public String inboxShow(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		Map<String, Object> data = modelMap.asMap();
		MailInbox inbox = inboxService.get(id);
		if (inbox == null) {
			return resp.notFound();
		}
		if (!inbox.getReceiver().getId().equals(user.getId())) {
			return resp.forbidden();
		}
		inboxService.read(id);
		data.put("inbox", inbox);
		ForeContext.setData(data, request);
		return site.getTemplate(INBOX_SHOW_TEMPLATE);
	}

	@RequestMapping(value = { "/my/inbox/delete", Constants.SITE_PREFIX_PATH + "/my/inbox/delete" })
	public String inboxDelete(Integer[] ids, Integer page, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		if (ArrayUtils.isEmpty(ids)) {
			return resp.badRequest();
		}
		User user = Context.getCurrentUser();
		for (Integer id : ids) {
			MailInbox bean = inboxService.get(id);
			// 不是自己发送的，也不是自己接收的，不能删除
			if (!bean.getReceiver().getId().equals(user.getId())) {
				return resp.post(501, "error.forbiddenData");
			}
			inboxService.delete(id);
		}
		return resp.post();
	}

	@RequestMapping(value = { "/my/notification", Constants.SITE_PREFIX_PATH + "/my/notification" })
	public String notification(Integer page, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(NOTIFICATION_TEMPLATE);
	}

	@RequestMapping(value = { "/my/notification/delete",
			Constants.SITE_PREFIX_PATH + "/my/notification/delete" })
	public String notificationDelete(Integer[] ids, Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		if (ArrayUtils.isEmpty(ids)) {
			return resp.badRequest();
		}
		User user = Context.getCurrentUser();
		for (Integer id : ids) {
			Notification bean = notificationService.get(id);
			// 不是自己发送的，也不是自己接收的，不能删除
			if (!bean.getReceiver().getId().equals(user.getId())) {
				return resp.post(501, "error.forbiddenData");
			}
			notificationService.delete(id);
		}
		return resp.post();
	}

	@Autowired
	private NotificationService notificationService;
	@Autowired
	private MailInboxService inboxService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;
}

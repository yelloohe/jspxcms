package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPERATION_FAILURE;
import static com.jspxcms.core.constant.Constants.OPERATION_SUCCESS;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.security.CredentialsDigest;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.MailInbox;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Message;
import com.jspxcms.core.domain.MessageText;
import com.jspxcms.core.domain.Notification;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.CommentService;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.MailInboxService;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.MessageService;
import com.jspxcms.core.service.NotificationService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;
import com.jspxcms.ext.service.GuestbookService;
import com.jspxcms.ext.service.VisitLogService;

/**
 * HomepageController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/homepage")
public class HomepageController {
	private static final Logger logger = LoggerFactory.getLogger(HomepageController.class);

	// @RequiresPermissions("core:homepage:welcome")
	@RequestMapping("welcome.do")
	public String welcome(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		modelMap.addAttribute("site", site);
		modelMap.addAttribute("user", user);

		Date endNext = new DateTime().plusDays(1).withMillisOfDay(0).toDate();
		Date begin = new DateTime(endNext.getTime()).minusDays(30).toDate();
		List<Object[]> visitList = visitService.trafficByDay(begin, endNext, site.getId());
		String groupBy = "day";
		modelMap.addAttribute("visitList", visitList);
		modelMap.addAttribute("groupBy", groupBy);

		List<Object[]> browserList = visitService.browserByTime(begin, endNext, site.getId());
		modelMap.addAttribute("browserList", browserList);

		Date begin7 = new DateTime(endNext.getTime()).minusDays(7).toDate();
		long infos = infoQuery.countByDate(site.getId(), begin7);
		modelMap.addAttribute("infos", infos);

		long users = userService.countByDate(begin7);
		modelMap.addAttribute("users", users);

		long comments = commentService.countByDate(site.getId(), begin7);
		modelMap.addAttribute("comments", comments);

		long guestbooks = guestbookService.countByDate(site.getId(), begin7);
		modelMap.addAttribute("guestbooks", guestbooks);

		return "core/homepage/welcome";
	}

	@RequiresPermissions("core:homepage:environment")
	@RequestMapping("environment.do")
	public String environment(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Properties props = System.getProperties();
		Runtime runtime = Runtime.getRuntime();
		long freeMemory = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long maxMemory = runtime.maxMemory();
		long usedMemory = totalMemory - freeMemory;
		long useableMemory = maxMemory - totalMemory + freeMemory;
		int div = 1000;
		double freeMemoryMB = ((double) freeMemory) / div / div;
		double totalMemoryMB = ((double) totalMemory) / div / div;
		double usedMemoryMB = ((double) usedMemory) / div / div;
		double maxMemoryMB = ((double) maxMemory) / div / div;
		double useableMemoryMB = ((double) useableMemory) / div / div;
		modelMap.addAttribute("props", props);
		modelMap.addAttribute("maxMemoryMB", maxMemoryMB);
		modelMap.addAttribute("usedMemoryMB", usedMemoryMB);
		modelMap.addAttribute("useableMemoryMB", useableMemoryMB);
		modelMap.addAttribute("totalMemoryMB", totalMemoryMB);
		modelMap.addAttribute("freeMemoryMB", freeMemoryMB);
		return "core/homepage/environment";
	}

	@RequiresPermissions("core:homepage:personal:edit")
	@RequestMapping(value = "personal_edit.do")
	public String personalEdit(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		modelMap.addAttribute("user", user);
		return "core/homepage/personal";
	}

	@RequiresPermissions("core:homepage:personal:update")
	@RequestMapping(value = "personal_update.do")
	public String personalUpdate(String origPassword, String rawPassword, HttpServletRequest request,
			RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		if (credentialsDigest.matches(user.getPassword(), origPassword, user.getSaltBytes())) {
			userService.updatePassword(user.getId(), rawPassword);

			Integer siteId = Context.getCurrentSiteId();
			Integer userId = Context.getCurrentUserId();
			String ip = Servlets.getRemoteAddr(request);
			logService.operation("opr.personal.password.edit", user.getUsername(), null, user.getId(), ip, userId,
					siteId);
			logger.info("personal password edit, name={}.", user.getUsername());

			ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		} else {
			ra.addFlashAttribute(MESSAGE, OPERATION_FAILURE);
		}
		return "redirect:personal_edit.do";
	}

	@RequiresPermissions("core:homepage:notification:list")
	@RequestMapping(value = "notification_list.do")
	public String notificationList(
			@PageableDefault(sort = "sendTime", direction = Direction.DESC, size = Constants.PAGE_SIZE) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		Page<Notification> pagedList = notificationService.findAll(user.getId(), null, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/homepage/notification_list";
	}

	@RequiresPermissions("core:homepage:notification:delete")
	@RequestMapping(value = "notification_delete.do")
	public String notificationDelete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		validateNotificationIds(ids, user.getId());
		notificationService.delete(ids);
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:notification_list.do";
	}

	@RequestMapping(value = "notification_delete_ajax.do")
	public void notificationView(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		validateNotificationIds(ids, user.getId());
		notificationService.delete(ids);
	}

	@RequiresPermissions("core:homepage:message:list")
	@RequestMapping(value = "message_list.do")
	public String messageList(@RequestParam(defaultValue = "false") boolean unread,
			@PageableDefault(sort = "send_time_", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		List<MemberGroup> memberGroupList = memberGroupService.findRealGroups();
		Page<Object[]> pagedList = messageService.findByUserId(user.getId(), unread, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		modelMap.addAttribute("memberGroupList", memberGroupList);
		return "core/homepage/message_list";
	}

	@RequiresPermissions("core:homepage:message:list")
	@RequestMapping(value = "message_contact.do")
	public String messageContact(Integer contactId,
			@PageableDefault(sort = "sendTime", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		messageService.setRead(user.getId(), contactId);
		User contact = userService.get(contactId);
		Page<Message> pagedList = messageService.findByContactId(user.getId(), contactId, pageable);
		modelMap.addAttribute("contactId", contactId);
		modelMap.addAttribute("contact", contact);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/homepage/message_contact";
	}

	@RequiresPermissions("core:homepage:message:send")
	@RequestMapping(value = "message_send.do", method = RequestMethod.POST)
	public String messageSend(String receiverUsername, Integer contactId, MessageText messageText,
			HttpServletRequest request, RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		if (!doCheckReceiver(receiverUsername)) {
			throw new CmsException("message.receiver.reject");
		}
		User receiver = userService.findByUsername(receiverUsername);
		messageService.send(user.getId(), receiver.getId(), messageText);
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		if (contactId != null) {
			ra.addAttribute("contactId", contactId);
			return "redirect:message_contact.do";
		}
		return "redirect:message_list.do";
	}

	@RequiresPermissions("core:homepage:message:delete")
	@RequestMapping(value = "message_delete.do")
	public String messageDelete(Integer contactId, Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		validateMessageIds(ids, user.getId());
		messageService.deleteById(ids, user.getId());
		ra.addAttribute("contactId", contactId);
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:message_contact.do";
	}

	@RequiresPermissions("core:homepage:message:delete")
	@RequestMapping(value = "message_delete_by_contact.do")
	public String messageDeleteByContact(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		for (Integer id : ids) {
			Message m = messageService.get(id);
			Integer contectId = m.getSender().getId() != user.getId() ? m.getSender().getId() : m.getReceiver().getId();
			messageService.deleteByContactId(user.getId(), contectId);
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:message_list.do";
	}

	/**
	 * 检查接收人
	 */
	@RequestMapping(value = "check_receiver.do")
	@ResponseBody
	public String checkReceiver(String receiverUsername) {
		return String.valueOf(doCheckReceiver(receiverUsername));
	}

	@RequiresPermissions("core:homepage:mail_inbox:list")
	@RequestMapping(value = "mail_inbox_list.do")
	public String mailInboxList(@RequestParam(defaultValue = "false") boolean unread,
			@PageableDefault(sort = "receiveTime", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		Page<MailInbox> pagedList = inboxService.findAll(user.getId(), params, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/homepage/mail_inbox_list";
	}

	@RequiresPermissions("core:homepage:mail_inbox:show")
	@RequestMapping(value = "mail_inbox_show.do")
	public String mailInboxShow(Integer id, Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		MailInbox bean = inboxService.get(id);
		if (bean == null) {
			throw new CmsException("objectNotFound", MailInbox.class.getName(), String.valueOf(id));
		}
		if (!user.getId().equals(bean.getReceiver().getId())) {
			throw new CmsException("accessDenied");
		}
		inboxService.read(id);
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		RowSide<MailInbox> side = inboxService.findSide(user.getId(), params, bean, position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		return "core/homepage/mail_inbox_show";
	}

	@RequiresPermissions("core:homepage:mail_inbox:delete")
	@RequestMapping(value = "mail_inbox_delete.do")
	public String mailInboxDelete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		validateMailInboxIds(ids, user.getId());
		inboxService.delete(ids);
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:mail_inbox_list.do";
	}

	/**
	 * 检查接收人是否合法
	 * 
	 * @param receiverUsername
	 *            接收人用户名
	 * @return
	 */
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

	private void validateMessageIds(Integer[] ids, Integer userId) {
		for (Integer id : ids) {
			Message bean = messageService.get(id);
			// 不是自己发送的，也不是自己接收的，不能删除
			if (!bean.getSender().getId().equals(userId) && !bean.getReceiver().getId().equals(userId)) {
				throw new CmsException("error.forbiddenData");
			}
		}
	}

	private void validateMailInboxIds(Integer[] ids, Integer userId) {
		for (Integer id : ids) {
			MailInbox bean = inboxService.get(id);
			// 不是自己发送的不能删除
			if (!bean.getReceiver().getId().equals(userId)) {
				throw new CmsException("error.forbiddenData");
			}
		}
	}

	private void validateNotificationIds(Integer[] ids, Integer userId) {
		for (Integer id : ids) {
			Notification bean = notificationService.get(id);
			// 不是自己发送的不能删除
			if (!bean.getReceiver().getId().equals(userId)) {
				throw new CmsException("error.forbiddenData");
			}
		}
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private InfoQueryService infoQuery;
	@Autowired
	private CommentService commentService;
	@Autowired
	private GuestbookService guestbookService;
	@Autowired
	private VisitLogService visitService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private MailInboxService inboxService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private CredentialsDigest credentialsDigest;
	@Autowired
	private UserService userService;
	@Autowired
	private MemberGroupService memberGroupService;
}

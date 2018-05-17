package com.jspxcms.core.service.impl;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Message;
import com.jspxcms.core.domain.MessageText;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.MessageDao;
import com.jspxcms.core.service.MessageService;
import com.jspxcms.core.service.NotificationService;
import com.jspxcms.core.service.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService, UserDeleteListener {
	@Override
	public Page<Message> findAll(Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	@Override
	public RowSide<Message> findSide(Map<String, String[]> params, Message bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<Message>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Message> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Message> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<Message> sp = SearchFilter.spec(filters, Message.class);
		return sp;
	}

	@Override
	public Page<Object[]> findByUserId(Integer userId, boolean unread, Pageable pageable) {
		Page<Object[]> page = dao.groupByUserId(userId, unread, pageable);
		List<Object[]> content = page.getContent();
		List<Object[]> resultList = fillWithObject(content);
		return new PageImpl<Object[]>(resultList, pageable, page.getTotalElements());
	}

	@Override
	public List<Object[]> findByUserId(Integer userId, boolean unread, Limitable limitable) {
		List<Object[]> content = dao.groupByUserId(userId, unread, limitable);
		List<Object[]> resultList = fillWithObject(content);
		return resultList;
	}

	/**
	 * 填充Message和User对象
	 * 
	 * @param content
	 * @return
	 */
	private List<Object[]> fillWithObject(List<Object[]> content) {
		List<Object[]> resultList = new ArrayList<Object[]>();
		for (int i = 0, len = content.size(); i < len; i++) {
			Message message = get(((Number) content.get(i)[0]).intValue());
			User user = userService.get(((Number) content.get(i)[1]).intValue());
			resultList.add(i, ArrayUtils.addAll(new Object[] { message, user }, content.get(i)));
		}
		return resultList;
	}

	@Override
	public Page<Message> findByContactId(Integer userId, Integer contactId, Pageable pageable) {
		return dao.findByContactId(userId, contactId, pageable);
	}

	@Override
	public List<Message> findByContactId(Integer userId, Integer contactId, Limitable limitable) {
		return dao.findByContactId(userId, contactId, limitable);
	}

	@Override
	public Message get(Integer id) {
		return dao.findOne(id);
	}

	@Override
	@Transactional
	public Message save(Message bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Override
	@Transactional
	public Message send(Integer senderId, Integer receiverId, MessageText messageText) {
		Message bean = new Message();
		User sender = userService.get(senderId);
		bean.setSender(sender);
		User receiver = userService.get(receiverId);
		bean.setReceiver(receiver);
		bean.setMessageText(messageText);
		messageText.setMessage(bean);
		save(bean);

		String sourceName = sender.getUsername();
		String sourceUrl = sender.getUrl();
		String message = bean.getTitle();
		String url = bean.getUrl();
		String backendUrl = bean.getBackendUrl();
		notificationService.send(Message.NOTIFICATION_TYPE, senderId, receiverId,
				Constants.NOTIFICATION_CONTENT_MESSAGE, sourceName, sourceUrl, message, null, null, url, backendUrl);
		return bean;
	}

	@Override
	@Transactional
	public Message update(Message bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Override
	@Transactional
	public int setRead(Integer userId, Integer senderId) {
		return dao.setRead(userId, senderId);
	}

	@Override
	@Transactional
	public int deleteByContactId(Integer userId, Integer contactId) {
		int number = dao.deleteByContactId(userId, contactId);
		number += dao.setDeleteFlagBySender(userId, contactId);
		number += dao.setDeleteFlagByReceiver(contactId, userId);
		return number;
	}

	@Override
	@Transactional
	public int deleteById(Integer[] ids, Integer userId) {
		int number = 0;
		for (Integer id : ids) {
			Message bean = get(id);
			if (bean.getReceiver().getId().equals(userId)) {
				// 接收方删除
				if (bean.getDeletionFlag() == Message.DELETION_SEND) {
					// 发送方已经删除，则彻底删除
					dao.delete(bean);
					number++;
				} else {
					// 发送方未删除，则标记接收方删除
					bean.setDeletionFlag(Message.DELETION_RECEIVE);
				}
			} else if (bean.getSender().getId().equals(userId)) {
				// 发送方删除
				if (bean.getDeletionFlag() == Message.DELETION_RECEIVE) {
					// 接收方已经删除，则彻底删除
					dao.delete(bean);
					number++;
				} else {
					// 接收方未删除，则标记发送方删除
					bean.setDeletionFlag(Message.DELETION_SEND);
				}
			} else {
				// 该用户与信息无关，不能删除，所以不作处理
			}
		}
		return number;
	}

	@Override
	@Transactional
	public Message delete(Integer id) {
		Message bean = dao.findOne(id);
		dao.delete(bean);
		return bean;
	}

	@Override
	@Transactional
	public List<Message> delete(Integer[] ids) {
		List<Message> beans = new ArrayList<Message>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
		}
		return beans;
	}

	@Override
	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			List<Integer> idList = Arrays.asList(ids);
		//	dao.deleteMessageTextByUserId(idList);
			dao.deleteByUserId(idList);
		}
	}

	private NotificationService notificationService;
	private UserService userService;

	@Autowired
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private MessageDao dao;

	@Autowired
	public void setDao(MessageDao dao) {
		this.dao = dao;
	}
}

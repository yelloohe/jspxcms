package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Notification;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.NotificationDao;
import com.jspxcms.core.service.NotificationService;
import com.jspxcms.core.service.UserService;

@Service
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService, UserDeleteListener {
	public Page<Notification> findAll(Integer receiverId, Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(receiverId, params), pageable);
	}

	public List<Notification> findAll(Integer receiverId, Map<String, String[]> params, Limitable limitable) {
		return dao.findAll(spec(receiverId, params), limitable);
	}

	private Specification<Notification> spec(final Integer receiverId, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Notification> fsp = SearchFilter.spec(filters, Notification.class);
		Specification<Notification> sp = new Specification<Notification>() {
			public Predicate toPredicate(Root<Notification> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (receiverId != null) {
					pred = cb.and(pred, cb.equal(root.get("receiver").get("id"), receiverId));
				}
				return pred;
			}
		};
		return sp;
	}

	public int countByReceiverId(Integer receiverId) {
		return dao.countByReceiverId(receiverId);
	}

	public Notification send(String type, Integer key, Integer receiverId, String contentTpl, String sourceName,
			String sourceUrl, String message, String targetName, String targetUrl, String url, String backendUrl) {
		Notification bean = dao.findByTypeAndKeyAndReceiverId(type, key, receiverId);
		if (bean == null) {
			User receiver = userService.get(receiverId);
			bean = new Notification(type, key, receiver);
			bean.setReceiver(receiver);
			bean.setQty(0);
		}
		bean.setQty(bean.getQty() + 1);
		bean.addSource(sourceName, sourceUrl);
		bean.processText(contentTpl, message, targetName, targetUrl);
		bean.setUrl(url);
		bean.setBackendUrl(backendUrl);
		return save(bean);
	}

	public Notification get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Notification save(Notification bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Notification update(Notification bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Notification delete(Integer id) {
		Notification bean = dao.findOne(id);
		if (bean != null) {
			dao.delete(bean);
		}
		return bean;
	}

	@Transactional
	public List<Notification> delete(Integer[] ids) {
		List<Notification> beans = new ArrayList<Notification>(ids.length);
		for (Integer id : ids) {
			Notification bean = delete(id);
			if (bean != null) {
				beans.add(bean);
			}
		}
		return beans;
	}

	@Override
	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			List<Integer> idList = Arrays.asList(ids);
	//		dao.deleteNotificationSourceByUserId(idList);
			dao.deleteByUserId(idList);
		}
	}

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private NotificationDao dao;

	@Autowired
	public void setDao(NotificationDao dao) {
		this.dao = dao;
	}
}

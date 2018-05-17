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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.MailInbox;
import com.jspxcms.core.domain.MailOutbox;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.MailInboxDao;
import com.jspxcms.core.service.MailInboxService;
import com.jspxcms.core.service.UserService;

@Service
@Transactional(readOnly = true)
public class MailInboxServiceImpl implements MailInboxService, UserDeleteListener {
	public Page<MailInbox> findAll(Integer receiverId, Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(receiverId, params), pageable);
	}

	public List<MailInbox> findAll(Integer receiverId, Map<String, String[]> params, Limitable limitable) {
		return dao.findAll(spec(receiverId, params), limitable);
	}

	public RowSide<MailInbox> findSide(Integer receiverId, Map<String, String[]> params, MailInbox bean,
			Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<MailInbox>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<MailInbox> list = dao.findAll(spec(receiverId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<MailInbox> spec(final Integer receiverId, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<MailInbox> fsp = SearchFilter.spec(filters, MailInbox.class);
		Specification<MailInbox> sp = new Specification<MailInbox>() {
			public Predicate toPredicate(Root<MailInbox> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (receiverId != null) {
					pred = cb.and(pred, cb.equal(root.get("receiver").get("id"), receiverId));
				}
				return pred;
			}
		};
		return sp;
	}

	public int receive(MailOutbox outbox, String receiverUsername, Integer[] receiverGroupIds, boolean allReceive) {
		if (allReceive) {
			return dao.allReceive(outbox);
		} else if (ArrayUtils.isNotEmpty(receiverGroupIds)) {
			return dao.groupReceiveGroup(outbox, receiverGroupIds);
		} else if (StringUtils.isNotBlank(receiverUsername)) {
			User receiver = userService.findByUsername(receiverUsername);
			if (receiver != null && !receiver.isAnonymous()) {
				MailInbox bean = new MailInbox(outbox);
				bean.setReceiver(receiver);
				save(bean);
				return 1;
			}
		}
		return 0;
	}

	@Transactional
	public void read(Integer id) {
		MailInbox bean = get(id);
		bean.setUnread(false);
		MailOutbox outbox = bean.getOutbox();
		outbox.setReadNumber(outbox.getReadNumber() + 1);
	}

	public MailInbox get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public MailInbox save(MailInbox bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public MailInbox update(MailInbox bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public MailInbox delete(Integer id) {
		MailInbox bean = dao.findOne(id);
		dao.delete(bean);
		return bean;
	}

	@Transactional
	public List<MailInbox> delete(Integer[] ids) {
		List<MailInbox> beans = new ArrayList<MailInbox>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
		}
		return beans;
	}

	@Override
	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			for (MailInbox bean : dao.findByReceiverIdIn(Arrays.asList(ids))) {
				delete(bean.getId());
			}
		}
	}

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private MailInboxDao dao;

	@Autowired
	public void setDao(MailInboxDao dao) {
		this.dao = dao;
	}
}

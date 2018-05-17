package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
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
import com.jspxcms.core.domain.MailOutbox;
import com.jspxcms.core.domain.MailText;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.MailOutboxDao;
import com.jspxcms.core.service.MailInboxService;
import com.jspxcms.core.service.MailOutboxService;

@Service
@Transactional(readOnly = true)
public class MailOutboxServiceImpl implements MailOutboxService, UserDeleteListener {
	public Page<MailOutbox> findAll(Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	public RowSide<MailOutbox> findSide(Map<String, String[]> params, MailOutbox bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<MailOutbox>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<MailOutbox> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<MailOutbox> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<MailOutbox> sp = SearchFilter.spec(filters, MailOutbox.class);
		return sp;
	}

	public MailOutbox get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public MailOutbox send(String receiverUsername, Integer[] receiverGroupIds, boolean allReceive, User sender,
			MailText mailText) {
		MailOutbox bean = new MailOutbox();
		bean.setSender(sender);
		bean.setMailText(mailText);
		save(bean);
		int receiverNumber = inboxService.receive(bean, receiverUsername, receiverGroupIds, allReceive);
		bean.setReceiverNumber(receiverNumber);
		return bean;
	}

	@Transactional
	public MailOutbox save(MailOutbox bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public MailOutbox update(MailOutbox bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public MailOutbox delete(Integer id) {
		MailOutbox bean = dao.findOne(id);
		dao.delete(bean);
		return bean;
	}

	@Transactional
	public List<MailOutbox> delete(Integer[] ids) {
		List<MailOutbox> beans = new ArrayList<MailOutbox>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
		}
		return beans;
	}

	@Override
	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			for (MailOutbox bean : dao.findBySenderIdIn(Arrays.asList(ids))) {
				delete(bean.getId());
			}
		}
	}

	private MailInboxService inboxService;

	@Autowired
	public void setInboxService(MailInboxService inboxService) {
		this.inboxService = inboxService;
	}

	private MailOutboxDao dao;

	@Autowired
	public void setDao(MailOutboxDao dao) {
		this.dao = dao;
	}
}

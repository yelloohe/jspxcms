package com.jspxcms.ext.repository.impl;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.jspxcms.common.orm.JpqlBuilder;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.repository.plus.QuestionDaoPlus;

public class QuestionDaoImpl implements QuestionDaoPlus {
	public List<Question> findList(Integer userId, Integer historyUserId, Boolean inPeriod, Integer[] status,
			Integer[] siteId, Limitable limitable) {
		JpqlBuilder jb = predicate(userId, historyUserId, inPeriod, status, siteId);
		return jb.list(em, Question.class, limitable);
	}

	public Page<Question> findPage(Integer userId, Integer historyUserId, Boolean inPeriod, Integer[] status,
			Integer[] siteId, Pageable pageable) {
		JpqlBuilder jb = predicate(userId, historyUserId, inPeriod, status, siteId);
		return jb.page(em, Question.class, pageable);
	}

	private JpqlBuilder predicate(Integer userId, Integer historyUserId, Boolean inPeriod, Integer[] status,
			Integer[] siteId) {
		JpqlBuilder jb = new JpqlBuilder("select distinct bean from Question bean");
		jb.append(" where 1=1");
		if (userId != null) {
			jb.append(" and bean.id not in");
			jb.append(" (select record.question.id from QuestionRecord record where record.user.id=:userId)");
			jb.setParameter("userId", userId);
		}
		if (historyUserId != null) {
			jb.append(" and bean.id in");
			jb.append(" (select record.question.id from QuestionRecord record where record.user.id=:historyUserId)");
			jb.setParameter("historyUserId", historyUserId);
		}
		if (inPeriod != null) {
			if (inPeriod) {
				jb.append(" and (bean.beginDate is null or bean.beginDate <= current_date())");
				jb.append(" and (bean.endDate is null or bean.endDate >= current_date())");
			}
		}
		if (ArrayUtils.isNotEmpty(status)) {
			jb.append(" and bean.status in (:status)");
			jb.setParameter("status", Arrays.asList(status));
		}
		if (ArrayUtils.isNotEmpty(siteId)) {
			jb.append(" and bean.site.id in (:siteId)");
			jb.setParameter("siteId", Arrays.asList(siteId));
		}
		return jb;
	}

	public Question findLatest(Integer[] status, Integer siteId) {
		JpqlBuilder jb = new JpqlBuilder("select bean from Question bean");
		jb.append(" where 1=1");
		if (siteId != null) {
			jb.append(" and bean.site.id=:siteId");
			jb.setParameter("siteId", siteId);
		}
		if (ArrayUtils.isNotEmpty(status)) {
			jb.append(" and bean.status in (:status)");
			jb.setParameter("status", Arrays.asList(status));
		}
		Sort sort = new Sort(new Order(Direction.DESC, "creationDate"));
		List<Question> list = jb.list(em, Question.class, sort);
		return list.isEmpty() ? null : list.get(0);
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

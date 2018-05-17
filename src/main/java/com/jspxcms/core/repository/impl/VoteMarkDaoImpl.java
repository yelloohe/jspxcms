package com.jspxcms.core.repository.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;

import com.jspxcms.core.domain.dsl.QVoteMark;
import com.jspxcms.core.repository.plus.VoteMarkDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class VoteMarkDaoImpl implements VoteMarkDaoPlus {
	public int countMark(String ftype, Integer fid, Integer userId, String ip,
			String cookie, Date after) {
		JPAQuery<Long> query = new JPAQuery<Long>(this.em);
		QVoteMark bean = QVoteMark.voteMark;
		query.select(bean.count());
		query.from(bean);
		BooleanBuilder exp = new BooleanBuilder();
		exp = exp.and(bean.ftype.eq(ftype));
		exp = exp.and(bean.fid.eq(fid));
		if (userId != null) {
			exp = exp.and(bean.user.id.eq(userId));
		} else if (StringUtils.isNotBlank(ip)) {
			exp = exp.and(bean.ip.eq(ip));
		} else if (StringUtils.isNotBlank(cookie)) {
			exp = exp.and(bean.cookie.eq(cookie));
		} else {
			throw new IllegalArgumentException(
					"userId or ip or cookie is required.");
		}
		if (after != null) {
			exp = exp.and(bean.date.after(after));
		}
		query.where(exp);
		return query.fetch().iterator().next().intValue();
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

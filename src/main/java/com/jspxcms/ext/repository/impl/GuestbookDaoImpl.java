package com.jspxcms.ext.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.QuerydslUtils;
import com.jspxcms.ext.domain.Guestbook;
import com.jspxcms.ext.domain.dsl.QGuestbook;
import com.jspxcms.ext.repository.plus.GuestbookDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * GuestbookDaoImpl
 * 
 * @author yangxing
 * 
 */
public class GuestbookDaoImpl implements GuestbookDaoPlus {
	public List<Guestbook> findList(Integer[] siteId, String[] type,
			Integer[] typeId, Boolean isRecommend, Boolean isReply,
			Integer[] status, Limitable limitable) {
		JPAQuery<Guestbook> query = new JPAQuery<Guestbook>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QGuestbook guestbook = QGuestbook.guestbook;
		predicate(query, guestbook, siteId, type, typeId, isRecommend, isReply,
				status);
		return QuerydslUtils.list(query, guestbook, limitable);
	}

	public Page<Guestbook> findPage(Integer[] siteId, String[] type,
			Integer[] typeId, Boolean isRecommend, Boolean isReply,
			Integer[] status, Pageable pageable) {
		JPAQuery<Guestbook> query = new JPAQuery<Guestbook>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QGuestbook guestbook = QGuestbook.guestbook;
		predicate(query, guestbook, siteId, type, typeId, isRecommend, isReply,
				status);
		return QuerydslUtils.page(query, guestbook, pageable);
	}

	private void predicate(JPAQuery<Guestbook> query, QGuestbook guestbook,
			Integer[] siteId, String[] type, Integer[] typeId,
			Boolean isRecommend, Boolean isReply, Integer[] status) {
		query.from(guestbook);

		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(siteId)) {
			exp = exp.and(guestbook.site.id.in(siteId));
		}
		if (ArrayUtils.isNotEmpty(type)) {
			exp = exp.and(guestbook.type.number.in(type));
		}
		if (ArrayUtils.isNotEmpty(typeId)) {
			exp = exp.and(guestbook.type.id.in(typeId));
		}
		if (isRecommend != null) {
			exp = exp.and(guestbook.recommend.eq(isRecommend));
		}
		if (isReply != null) {
			exp = exp.and(guestbook.reply.eq(isReply));
		}
		if (ArrayUtils.isNotEmpty(status)) {
			exp = exp.and(guestbook.status.in(status));
		}
		query.where(exp);
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

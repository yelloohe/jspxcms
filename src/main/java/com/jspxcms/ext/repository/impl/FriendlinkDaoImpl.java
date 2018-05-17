package com.jspxcms.ext.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.jpa.QueryHints;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.QuerydslUtils;
import com.jspxcms.ext.domain.Friendlink;
import com.jspxcms.ext.domain.dsl.QFriendlink;
import com.jspxcms.ext.repository.plus.FriendlinkDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * FriendlinkDaoImpl
 * 
 * @author yangxing
 * 
 */
public class FriendlinkDaoImpl implements FriendlinkDaoPlus {
	public List<Friendlink> findList(Integer[] siteId, String[] type,
			Integer[] typeId, Boolean isWithLogo, Boolean isRecommend,
			Integer[] status, Limitable limitable) {
		JPAQuery<Friendlink> query = new JPAQuery<Friendlink>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QFriendlink friendlink = QFriendlink.friendlink;
		predicate(query, friendlink, siteId, type, typeId, isWithLogo,
				isRecommend, status);
		return QuerydslUtils.list(query, friendlink, limitable);
	}

	private void predicate(JPAQuery<Friendlink> query, QFriendlink friendlink,
			Integer[] siteId, String[] type, Integer[] typeId,
			Boolean isWithLogo, Boolean isRecommend, Integer[] status) {
		query.from(friendlink);
		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(siteId)) {
			exp = exp.and(friendlink.site.id.in(siteId));
		}
		if (ArrayUtils.isNotEmpty(type)) {
			exp = exp.and(friendlink.type.number.in(type));
		}
		if (ArrayUtils.isNotEmpty(typeId)) {
			exp = exp.and(friendlink.type.id.in(typeId));
		}
		if (isWithLogo != null) {
			exp = exp.and(friendlink.withLogo.eq(isWithLogo));
		}
		if (isRecommend != null) {
			exp = exp.and(friendlink.recommend.eq(isRecommend));
		}
		if (ArrayUtils.isNotEmpty(status)) {
			exp = exp.and(friendlink.status.in(status));
		}
		query.where(exp);
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

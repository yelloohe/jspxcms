package com.jspxcms.core.repository.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.jpa.QueryHints;

import com.jspxcms.core.domain.Model;
import com.jspxcms.core.domain.dsl.QModel;
import com.jspxcms.core.repository.plus.ModelDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * ModelDaoImpl
 * 
 * @author liufang
 * 
 */
public class ModelDaoImpl implements ModelDaoPlus {
	public List<Model> findList(Integer siteId, String type) {
		JPAQuery<Model> query = new JPAQuery<Model>(this.em);
		QModel model = QModel.model;
		query.from(model);
		BooleanBuilder exp = new BooleanBuilder();
		exp = exp.and(model.site.id.eq(siteId));
		if (type != null) {
			exp = exp.and(model.type.eq(type));
		}
		query.where(exp);
		query.orderBy(model.seq.asc());
		return query.fetch();
	}

	public Model findDefault(Integer siteId, String type) {
		JPAQuery<Model> query = new JPAQuery<Model>(this.em);
		QModel model = QModel.model;
		query.from(model);
		BooleanBuilder exp = new BooleanBuilder();
		if (siteId != null) {
			exp = exp.and(model.site.id.eq(siteId));
		}
		exp = exp.and(model.type.eq(type));
		query.where(exp);
		query.orderBy(model.seq.asc());
		query.limit(1);
		List<Model> list = query.fetch();
		return !list.isEmpty() ? list.get(0) : null;
	}

	public List<Model> findByNumbers(String[] numbers, Integer[] siteIds) {
		if (ArrayUtils.isEmpty(numbers)) {
			return Collections.emptyList();
		}
		JPAQuery<Model> query = new JPAQuery<Model>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QModel model = QModel.model;
		query.from(model);

		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(numbers)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = numbers.length; i < len; i++) {
				e = e.or(model.number.eq(numbers[i]));
			}
			exp = exp.and(e);
		}
		if (ArrayUtils.isNotEmpty(siteIds)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = siteIds.length; i < len; i++) {
				e = e.or(model.site.id.eq(siteIds[i]));
			}
			exp = exp.and(e);
		}
		query.where(exp);
		return query.fetch();
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

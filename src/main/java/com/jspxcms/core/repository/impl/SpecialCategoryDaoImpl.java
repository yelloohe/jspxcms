package com.jspxcms.core.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.QuerydslUtils;
import com.jspxcms.core.domain.SpecialCategory;
import com.jspxcms.core.domain.dsl.QSpecialCategory;
import com.jspxcms.core.repository.plus.SpecialCategoryDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * SpecialCategoryDaoImpl
 * 
 * @author liufang
 * 
 */
public class SpecialCategoryDaoImpl implements SpecialCategoryDaoPlus {
	public List<SpecialCategory> getList(Integer[] siteId, Limitable limitable) {
		JPAQuery<SpecialCategory> query = new JPAQuery<SpecialCategory>(this.em);
		QSpecialCategory scategory = QSpecialCategory.specialCategory;
		query.from(scategory);
		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(siteId)) {
			exp = exp.and(scategory.site.id.in(siteId));
		}
		query.where(exp);
		return QuerydslUtils.list(query, scategory, limitable);

	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

}

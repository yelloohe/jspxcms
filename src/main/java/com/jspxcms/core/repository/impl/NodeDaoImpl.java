package com.jspxcms.core.repository.impl;

import com.jspxcms.common.orm.JpqlBuilder;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.QuerydslUtils;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.dsl.QNode;
import com.jspxcms.core.repository.plus.NodeDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

/**
 * NodeDaoImpl
 * 
 * @author liufang
 * 
 */
public class NodeDaoImpl implements NodeDaoPlus {
	public List<Node> findList(Integer[] siteId, Integer parentId, String treeNumber, Boolean isRealNode,
			Boolean isHidden,Integer[] p0, Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6,
			Limitable limitable) {
		JPAQuery<Node> query = new JPAQuery<Node>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QNode node = QNode.node;
		predicate(query, node, siteId, parentId, treeNumber, isRealNode, isHidden,p0, p1, p2, p3, p4, p5, p6);
		return QuerydslUtils.list(query, node, limitable);
	}

	public Page<Node> findPage(Integer[] siteId, Integer parentId, String treeNumber, Boolean isRealNode,
			Boolean isHidden,Integer[] p0, Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6,
			Pageable pageable) {
		JPAQuery<Node> query = new JPAQuery<Node>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QNode node = QNode.node;
		predicate(query, node, siteId, parentId, treeNumber, isRealNode, isHidden,p0, p1, p2, p3, p4, p5, p6);
		return QuerydslUtils.page(query, node, pageable);
	}

	private void predicate(JPAQuery<Node> query, QNode node, Integer[] siteId, Integer parentId, String treeNumber,
			Boolean isRealNode, Boolean isHidden, Integer[] p0,Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5,
			Integer[] p6) {
		query.from(node);
		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(siteId)) {
			exp = exp.and(node.site.id.in(siteId));
		}
		if (parentId != null) {
			exp = exp.and(node.parent.id.eq(parentId));
		}
		if (StringUtils.isNotBlank(treeNumber)) {
			exp = exp.and(node.treeNumber.startsWith(treeNumber));
		}
		if (isRealNode != null) {
			exp = exp.and(node.realNode.eq(isRealNode));
		}
		if (isHidden != null) {
			exp = exp.and(node.hidden.eq(isHidden));
		}
		if (ArrayUtils.isNotEmpty(p0)) {
			exp = exp.and(node.p0.in(p0));
		}
		if (ArrayUtils.isNotEmpty(p1)) {
			exp = exp.and(node.p1.in(p1));
		}
		if (ArrayUtils.isNotEmpty(p2)) {
			exp = exp.and(node.p2.in(p2));
		}
		if (ArrayUtils.isNotEmpty(p3)) {
			exp = exp.and(node.p3.in(p3));
		}
		if (ArrayUtils.isNotEmpty(p4)) {
			exp = exp.and(node.p4.in(p4));
		}
		if (ArrayUtils.isNotEmpty(p5)) {
			exp = exp.and(node.p5.in(p5));
		}
		if (ArrayUtils.isNotEmpty(p6)) {
			exp = exp.and(node.p6.in(p6));
		}
		query.where(exp);
	}

	public List<Node> findByNumbersLike(String[] numbers, Integer[] siteIds) {
		if (ArrayUtils.isEmpty(numbers)) {
			return Collections.emptyList();
		}
		JPAQuery<Node> query = new JPAQuery<Node>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QNode node = QNode.node;
		query.from(node);
		BooleanBuilder exp = new BooleanBuilder();
		for (int i = 0, len = numbers.length; i < len; i++) {
			exp = exp.or(node.number.like(numbers[i]));
		}
		query.where(exp);
		return query.fetch();
	}

	public List<Node> findByNumbers(String[] numbers, Integer[] siteIds) {
		if (ArrayUtils.isEmpty(numbers)) {
			return Collections.emptyList();
		}
		JPAQuery<Node> query = new JPAQuery<Node>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QNode node = QNode.node;
		query.from(node);

		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(numbers)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = numbers.length; i < len; i++) {
				e = e.or(node.number.eq(numbers[i]));
			}
			exp = exp.and(e);
		}
		if (ArrayUtils.isNotEmpty(siteIds)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = siteIds.length; i < len; i++) {
				e = e.or(node.site.id.eq(siteIds[i]));
			}
			exp = exp.and(e);
		}
		query.where(exp);
		return query.fetch();
	}

	public List<Node> findForHtml(Integer siteId, Integer nodeId, String treeNumber, boolean forUpdate, Integer lastId,
			int maxResult) {
		JpqlBuilder jb = new JpqlBuilder("from Node bean where 1=1");
		jb.append(" and bean.site.id = :siteId");
		jb.setParameter("siteId", siteId);
		if (lastId != null) {
			jb.append(" and bean.id > :lastId");
			jb.setParameter("lastId", lastId);
		}
		if (forUpdate) {
			jb.append(" and (bean.htmlStatus = '" + Node.HTML_TOBE_DELETE);
			jb.append("' or bean.htmlStatus = '" + Node.HTML_TOBE_UPDATE);
			jb.append("')");
		}
		if (StringUtils.isNotBlank(treeNumber)) {
			jb.append(" and bean.treeNumber like :treeNumber");
			jb.setParameter("treeNumber", treeNumber + "%");
		} else if (nodeId != null) {
			jb.append(" and bean.id = :nodeId");
			jb.setParameter("nodeId", nodeId);
		}
		jb.append(" order by bean.id asc");
		TypedQuery<Node> query = jb.createQuery(em, Node.class);
		query.setMaxResults(maxResult);
		return query.getResultList();
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

}

package com.jspxcms.core.repository.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.QuerydslUtils;
import com.jspxcms.core.domain.Comment;
import com.jspxcms.core.domain.dsl.QComment;
import com.jspxcms.core.repository.plus.CommentDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * CommentDaoImpl
 * 
 * @author liufang
 * 
 */
public class CommentDaoImpl implements CommentDaoPlus {
	public List<Comment> findList(String ftype, Integer fid, Integer creatorId,
			Integer[] status, Integer[] siteId, Limitable limitable) {
		JPAQuery<Comment> query = new JPAQuery<Comment>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QComment comment = QComment.comment;
		predicate(query, comment, ftype, fid, creatorId, status, siteId);
		return QuerydslUtils.list(query, comment, limitable);
	}

	public Page<Comment> findPage(String ftype, Integer fid, Integer creatorId,
			Integer[] status, Integer[] siteId, Pageable pageable) {
		JPAQuery<Comment> query = new JPAQuery<Comment>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QComment comment = QComment.comment;
		predicate(query, comment, ftype, fid, creatorId, status, siteId);
		return QuerydslUtils.page(query, comment, pageable);
	}

	private void predicate(JPAQuery<Comment> query, QComment comment, String ftype,
			Integer fid, Integer creatorId, Integer[] status, Integer[] siteId) {
		query.from(comment);
		BooleanBuilder exp = new BooleanBuilder();
		if (StringUtils.isBlank(ftype)) {
			exp.and(comment.ftype.eq(ftype));
		}
		if (fid != null) {
			exp.and(comment.fid.eq(fid));
		}
		if (creatorId != null) {
			exp.and(comment.creator.id.eq(creatorId));
		}
		if (ArrayUtils.isNotEmpty(status)) {
			exp.and(comment.status.in(status));
		}
		if (ArrayUtils.isNotEmpty(siteId)) {
			exp.and(comment.site.id.in(siteId));
		}
		query.where(exp);
	}

	public Object getEntity(String entityName, Serializable id) {
		String jpql = "from " + entityName + " bean where bean.id=:id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", id).setMaxResults(1);
		Iterator<?> it = query.getResultList().iterator();
		return it.hasNext() ? it.next() : null;
	}

	public void flushAndRefresh(Comment entity) {
		em.flush();
		em.refresh(entity);
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

package com.jspxcms.core.repository.impl;

import com.jspxcms.common.orm.JpqlBuilder;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.QuerydslUtils;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.dsl.*;
import com.jspxcms.core.repository.plus.InfoDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * InfoDaoImpl
 * 
 * @author liufang
 * 
 */
public class InfoDaoImpl implements InfoDaoPlus {
	public List<Info> findList(Integer[] modelId, Integer[] nodeId, Integer[] attrId, Integer[] specialId,
			Integer[] tagId, Integer[] siteId, Integer[] mainNodeId, Integer[] userId, Integer[] viewGroupId,
			Integer[] viewOrgId, String[] treeNumber, String[] specialTitle, String[] tagName, Integer[] priority,
			Date beginDate, Date endDate, String[] title, Integer[] includeId, Integer[] excludeId,
			Integer[] excludeMainNodeId, String[] excludeTreeNumber, Boolean isWithImage, String[] status,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6, Limitable limitable) {
		JPAQuery<Info> query = new JPAQuery<Info>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QInfo info = QInfo.info;
		predicate(query, info, modelId, nodeId, attrId, specialId, tagId, siteId, mainNodeId, userId, viewGroupId,
				viewOrgId, treeNumber, specialTitle, tagName, priority, beginDate, endDate, title, includeId,
				excludeId, excludeMainNodeId, excludeTreeNumber, isWithImage, status,p0, p1, p2, p3, p4, p5, p6);
		return QuerydslUtils.list(query, info, limitable);
	}

	public Page<Info> findPage(Integer[] modelId, Integer[] nodeId, Integer[] attrId, Integer[] specialId,
			Integer[] tagId, Integer[] siteId, Integer[] mainNodeId, Integer[] userId, Integer[] viewGroupId,
			Integer[] viewOrgId, String[] treeNumber, String[] specialTitle, String[] tagName, Integer[] priority,
			Date beginDate, Date endDate, String[] title, Integer[] includeId, Integer[] excludeId,
			Integer[] excludeMainNodeId, String[] excludeTreeNumber, Boolean isWithImage, String[] status,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6, Pageable pageable) {
		JPAQuery<Info> query = new JPAQuery<Info>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QInfo info = QInfo.info;
		predicate(query, info, modelId, nodeId, attrId, specialId, tagId, siteId, mainNodeId, userId, viewGroupId,
				viewOrgId, treeNumber, specialTitle, tagName, priority, beginDate, endDate, title, includeId,
				excludeId, excludeMainNodeId, excludeTreeNumber, isWithImage, status,p0, p1, p2, p3, p4, p5, p6);
		return QuerydslUtils.page(query, info, pageable);
	}

	private void predicate(JPAQuery<Info> query, QInfo info, Integer[] modelId, Integer[] nodeId, Integer[] attrId,
			Integer[] specialId, Integer[] tagId, Integer[] siteId, Integer[] mainNodeId, Integer[] userId,
			Integer[] viewGroupId, Integer[] viewOrgId, String[] treeNumber, String[] specialTitle, String[] tagName,
			Integer[] priority, Date beginDate, Date endDate, String[] title, Integer[] includeId, Integer[] excludeId,
			Integer[] excludeMainNodeId, String[] excludeTreeNumber, Boolean isWithImage, String[] status,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6) {
		boolean isDistinct = false;
		query.from(info);
		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(modelId)) {
			QNode node = QNode.node;
			query.innerJoin(info.node, node);
			exp = exp.and(node.infoModel.id.in(modelId));
		}
		if (ArrayUtils.isNotEmpty(nodeId)) {
			QInfoNode infoNode = QInfoNode.infoNode;
			query.innerJoin(info.infoNodes, infoNode);
			exp = exp.and(infoNode.node.id.in(nodeId));
			if (nodeId.length > 1) {
				isDistinct = true;
			}
		}
		if (ArrayUtils.isNotEmpty(attrId)) {
			QInfoAttribute infoAttr = QInfoAttribute.infoAttribute;
			QAttribute attr = QAttribute.attribute;
			query.innerJoin(info.infoAttrs, infoAttr).innerJoin(infoAttr.attribute, attr);
			exp = exp.and(attr.id.in(attrId));
			if (attrId.length > 1) {
				isDistinct = true;
			}
		}
		boolean isSpecialId = ArrayUtils.isNotEmpty(specialId);
		boolean isSpecialTitle = ArrayUtils.isNotEmpty(specialTitle);
		if (isSpecialId || isSpecialTitle) {
			QInfoSpecial infoSpeical = QInfoSpecial.infoSpecial;
			query.innerJoin(info.infoSpecials, infoSpeical);
			QSpecial special = QSpecial.special;
			query.innerJoin(infoSpeical.special, special);
			BooleanBuilder e = new BooleanBuilder();
			if (isSpecialId) {
				for (int i = 0, len = specialId.length; i < len; i++) {
					e = e.or(special.id.eq(specialId[i]));
				}
				if (specialId.length > 1) {
					isDistinct = true;
				}
			}
			if (isSpecialTitle) {
				for (int i = 0, len = specialTitle.length; i < len; i++) {
					e = e.or(special.title.like(specialTitle[i]));
				}
				isDistinct = true;
			}
			exp = exp.and(e);
		}
		boolean isTagId = ArrayUtils.isNotEmpty(tagId);
		boolean isTagName = ArrayUtils.isNotEmpty(tagName);
		if (isTagId || isTagName) {
			QInfoTag infoTag = QInfoTag.infoTag;
			query.innerJoin(info.infoTags, infoTag);
			QTag tag = QTag.tag;
			query.innerJoin(infoTag.tag, tag);
			BooleanBuilder e = new BooleanBuilder();
			if (isTagId) {
				for (int i = 0, len = tagId.length; i < len; i++) {
					e = e.or(tag.id.eq(tagId[i]));
				}
				if (tagId.length > 1) {
					isDistinct = true;
				}
			}
			if (isTagName) {
				for (int i = 0, len = tagName.length; i < len; i++) {
					e = e.or(tag.name.like(tagName[i]));
				}
				isDistinct = true;
			}
			exp = exp.and(e);
		}
		if (ArrayUtils.isNotEmpty(siteId)) {
			exp = exp.and(info.site.id.in(siteId));
		}
		if (ArrayUtils.isNotEmpty(mainNodeId)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = mainNodeId.length; i < len; i++) {
				e = e.or(info.node.id.eq(mainNodeId[i]));
			}
			exp = exp.and(e);
		}
		if (ArrayUtils.isNotEmpty(excludeMainNodeId)) {
			exp = exp.and(info.node.id.notIn(excludeMainNodeId));
		}
		if (ArrayUtils.isNotEmpty(userId)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = userId.length; i < len; i++) {
				e = e.or(info.creator.id.eq(userId[i]));
			}
			exp = exp.and(e);
		}
		if (viewGroupId != null || viewOrgId != null) {
			BooleanBuilder e = new BooleanBuilder();
			if (viewGroupId != null && viewGroupId.length > 0) {
				QNodeMemberGroup nodeGroup = QNodeMemberGroup.nodeMemberGroup;
				query.leftJoin(info.node.nodeGroups, nodeGroup);
				e = e.or(nodeGroup.group.id.in(viewGroupId).and(nodeGroup.viewPerm.eq(true)));
				QInfoMemberGroup infoGroup = QInfoMemberGroup.infoMemberGroup;
				query.leftJoin(info.infoGroups, infoGroup);
				e = e.or(infoGroup.group.id.in(viewGroupId).and(infoGroup.viewPerm.eq(true)));
				isDistinct = true;
			}
			if (viewOrgId != null && viewOrgId.length > 0) {
				QNodeOrg nodeOrg = QNodeOrg.nodeOrg;
				query.leftJoin(info.node.nodeOrgs, nodeOrg);
				e = e.or(nodeOrg.org.id.in(viewOrgId).and(nodeOrg.viewPerm.eq(true)));
				QInfoOrg infoOrg = QInfoOrg.infoOrg;
				query.leftJoin(info.infoOrgs, infoOrg);
				e = e.or(infoOrg.org.id.in(viewOrgId).and(infoOrg.viewPerm.eq(true)));
				isDistinct = true;
			}
			exp = exp.and(e);
		}
		boolean isTreeNumber = ArrayUtils.isNotEmpty(treeNumber);
		boolean isExcludeTreeNumber = ArrayUtils.isNotEmpty(excludeTreeNumber);
		if (isTreeNumber || isExcludeTreeNumber) {
			StringPath tnPath = info.node.treeNumber;
			if (isTreeNumber) {
				BooleanBuilder e = new BooleanBuilder();
				for (int i = 0, len = treeNumber.length; i < len; i++) {
					e = e.or(tnPath.startsWith(treeNumber[i]));
				}
				exp = exp.and(e);
			}
			if (isExcludeTreeNumber) {
				for (int i = 0, len = excludeTreeNumber.length; i < len; i++) {
					exp = exp.and(tnPath.startsWith(excludeTreeNumber[i]).not());
				}
			}
		}
		if (ArrayUtils.isNotEmpty(priority)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = priority.length; i < len; i++) {
				e = e.or(info.priority.eq(priority[i]));
			}
			exp = exp.and(e);
		}
		if (beginDate != null) {
			exp = exp.and(info.publishDate.goe(beginDate));
		}
		if (endDate != null) {
			exp = exp.and(info.publishDate.loe(endDate));
		}
		if (ArrayUtils.isNotEmpty(title)) {
			QInfoDetail infoDetail = QInfoDetail.infoDetail;
			query.innerJoin(info.detail, infoDetail);
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = title.length; i < len; i++) {
				e = e.or(infoDetail.title.like(title[i]));
			}
			exp = exp.and(e);
		}
		if (ArrayUtils.isNotEmpty(includeId)) {
			exp = exp.and(info.id.in(includeId));
		}
		if (ArrayUtils.isNotEmpty(excludeId)) {
			exp = exp.and(info.id.notIn(excludeId));
		}
		if (ArrayUtils.isNotEmpty(excludeTreeNumber)) {

		}
		if (isWithImage != null) {
			exp = exp.and(info.withImage.eq(isWithImage));
		}
		if (ArrayUtils.isNotEmpty(status)) {
			exp = exp.and(info.status.in(status));
		}
		if (ArrayUtils.isNotEmpty(p0)) {
			exp = exp.and(info.p0.in(p0));
		}
		if (ArrayUtils.isNotEmpty(p1)) {
			exp = exp.and(info.p1.in(p1));
		}
		if (ArrayUtils.isNotEmpty(p2)) {
			exp = exp.and(info.p2.in(p2));
		}
		if (ArrayUtils.isNotEmpty(p3)) {
			exp = exp.and(info.p3.in(p3));
		}
		if (ArrayUtils.isNotEmpty(p4)) {
			exp = exp.and(info.p4.in(p4));
		}
		if (ArrayUtils.isNotEmpty(p5)) {
			exp = exp.and(info.p5.in(p5));
		}
		if (ArrayUtils.isNotEmpty(p6)) {
			exp = exp.and(info.p6.in(p6));
		}
		query.where(exp);
		if (isDistinct) {
			query.distinct();
		}
	}

	public Info findNext(Integer siteId, Integer nodeId, Integer id, Date publishDate) {
		JPAQuery<Info> query = new JPAQuery<Info>(this.em);
		QInfo info = QInfo.info;
		query.from(info);
		BooleanBuilder exp = new BooleanBuilder();
		if (nodeId != null) {
			exp = exp.and(info.node.id.eq(nodeId));
		} else if (siteId != null) {
			exp = exp.and(info.site.id.eq(siteId));
		}
		exp = exp.and(info.publishDate.gt(publishDate).or(info.publishDate.eq(publishDate).and(info.id.gt(id))));
		// exp = exp.and(info.id.lt(id).or(info.id.gt(id)));
		exp = exp.and(info.status.eq(Info.NORMAL));
		query.where(exp);
		query.orderBy(info.publishDate.asc(), info.id.asc());
		query.limit(1);
		List<Info> list = query.fetch();
		return list.isEmpty() ? null : list.get(0);
	}

	public Info findPrev(Integer siteId, Integer nodeId, Integer id, Date publishDate) {
		JPAQuery<Info> query = new JPAQuery<Info>(this.em);
		QInfo info = QInfo.info;
		query.from(info);
		BooleanBuilder exp = new BooleanBuilder();
		if (nodeId != null) {
			exp = exp.and(info.node.id.eq(nodeId));
		} else if (siteId != null) {
			exp = exp.and(info.site.id.eq(siteId));
		}
		exp = exp.and(info.publishDate.lt(publishDate).or(info.publishDate.eq(publishDate).and(info.id.lt(id))));
		// exp = exp.and(info.id.lt(id).or(info.id.gt(id)));
		exp = exp.and(info.status.eq(Info.NORMAL));
		query.where(exp);
		query.orderBy(info.publishDate.desc(), info.id.desc());
		query.limit(1);
		List<Info> list = query.fetch();
		return list.isEmpty() ? null : list.get(0);
	}

	public List<Info> findForHtml(Integer siteId, Integer nodeId, String treeNumber, boolean forUpdate, Integer lastId,
			int maxResult) {
		JpqlBuilder jb = new JpqlBuilder("from Info bean where 1=1");
		jb.append(" and bean.site.id = :siteId");
		if (lastId != null) {
			jb.append(" and bean.id > :lastId");
			jb.setParameter("lastId", lastId);
		}
		if (forUpdate) {
			jb.append(" and (bean.htmlStatus = '" + Node.HTML_TOBE_DELETE);
			jb.append("' or bean.htmlStatus = '" + Node.HTML_TOBE_UPDATE);
			jb.append("')");
		}
		jb.setParameter("siteId", siteId);
		if (StringUtils.isNotBlank(treeNumber)) {
			jb.append(" and bean.node.treeNumber like :treeNumber");
			jb.setParameter("treeNumber", treeNumber + "%");
		} else if (nodeId != null) {
			jb.append(" and bean.node.id = :nodeId");
			jb.setParameter("nodeId", nodeId);
		}
		jb.append(" order by bean.id asc");
		TypedQuery<Info> query = jb.createQuery(em, Info.class);
		query.setMaxResults(maxResult);
		return query.getResultList();
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

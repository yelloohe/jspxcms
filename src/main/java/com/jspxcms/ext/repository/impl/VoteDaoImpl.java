package com.jspxcms.ext.repository.impl;

import com.jspxcms.common.orm.JpqlBuilder;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Vote;
import com.jspxcms.ext.domain.dsl.QVote;
import com.jspxcms.ext.repository.plus.VoteDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

public class VoteDaoImpl implements VoteDaoPlus {
    public List<Vote> findList(String[] number, Boolean inPeriod, Integer[] status, Integer[] siteId, Limitable limitable) {
        JpqlBuilder jb = predicate(number, inPeriod, status, siteId);
        return jb.list(em, Vote.class, limitable);
    }

    public Page<Vote> findPage(String[] number, Boolean inPeriod, Integer[] status, Integer[] siteId, Pageable pageable) {
        JpqlBuilder jb = predicate(number, inPeriod, status, siteId);
        return jb.page(em, Vote.class, pageable);
    }

    private JpqlBuilder predicate(String[] number, Boolean inPeriod, Integer[] status, Integer[] siteId) {
        JpqlBuilder jb = new JpqlBuilder("select bean from Vote bean");
        jb.append(" where 1=1");
        if (ArrayUtils.isNotEmpty(number)) {
            jb.append(" and (1=2");
            for (int i = 0, len = number.length; i < len; i++) {
                jb.append(" or bean.number like :number" + i);
                jb.setParameter("number" + i, number[i]);
            }
            jb.append(")");
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

    public Vote findByNumber(String number, Integer[] status, Integer siteId) {
        JPAQuery<Vote> query = new JPAQuery<Vote>(this.em);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        QVote bean = QVote.vote;
        query.from(bean);
        BooleanExpression exp = bean.status.eq(Vote.NORMAL_STATUS);
        if (siteId != null) {
            exp = exp.and(bean.site.id.eq(siteId));
        }
        if (ArrayUtils.isNotEmpty(status)) {
            exp = exp.and(bean.status.in(status));
        }
        if (StringUtils.isNotBlank(number)) {
            exp = exp.and(bean.number.eq(number));
        }
        query.where(exp);
        query.orderBy(bean.creationDate.desc(), bean.id.desc());
        query.limit(1);
        List<Vote> list = query.fetch();
        return list.isEmpty() ? null : list.get(0);
    }

    public Vote findLatest(Integer[] status, Integer siteId) {
        JPAQuery<Vote> query = new JPAQuery<Vote>(this.em);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        QVote bean = QVote.vote;
        query.from(bean);
        BooleanBuilder exp = new BooleanBuilder();
        if (siteId != null) {
            exp = exp.and(bean.site.id.eq(siteId));
        }
        if (ArrayUtils.isNotEmpty(status)) {
            exp = exp.and(bean.status.in(status));
        }
        query.where(exp);
        query.orderBy(bean.creationDate.desc(), bean.id.desc());
        query.limit(1);
        List<Vote> list = query.fetch();
        return list.isEmpty() ? null : list.get(0);
    }

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }
}

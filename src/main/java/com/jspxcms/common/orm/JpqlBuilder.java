package com.jspxcms.common.orm;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.type.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java Persistence Query Language 查询
 * <p>
 * 由于 {@link org.springframework.data.jpa.repository.query.QueryUtils#applySorting} ，查询语句中的表名需要加别名，比如 select * from user t，而不能select * from user。
 *
 * @author liufang
 */
public class JpqlBuilder {
    private StringBuilder jpql;
    private String countQueryString;
    private String countProjection;
    private List<Parameter> parameters = new ArrayList<>();
    /**
     * native查询时，某些类型无法匹配，比如nvarchar的值是-9，hibernate的方言无法匹配这一类型
     */
    private Map<String, Type> scalars = new LinkedHashMap<>();

    private static final String COUNT_STRING = "^\\s*(select\\s+(?:distinct\\s+)?([\\s\\S]*?)\\s+)?from\\s+[\\s\\S]*?(\\sgroup\\s+by\\s+[\\s\\S]*?)?(\\sorder\\s[\\s\\S]*?)?$";
    private static final Pattern COUNT_PATTERN = Pattern.compile(COUNT_STRING, Pattern.CASE_INSENSITIVE);

    public JpqlBuilder() {
        jpql = new StringBuilder();
    }

    public JpqlBuilder(String queryString) {
        jpql = new StringBuilder(queryString);
    }

    public JpqlBuilder append(String queryString) {
        jpql.append(queryString);
        return this;
    }

    public JpqlBuilder setParameter(String name, Object value) {
        parameters.add(new Parameter(name, value, null));
        return this;
    }

    public JpqlBuilder setParameter(String name, Date value, TemporalType temporalType) {
        parameters.add(new Parameter(name, value, temporalType));
        return this;
    }

    public JpqlBuilder setParameter(String name, Calendar value, TemporalType temporalType) {
        parameters.add(new Parameter(name, value, temporalType));
        return this;
    }

    public JpqlBuilder addScalar(String columnAlias, Type type) {
        scalars.put(columnAlias, type);
        return this;
    }

    public JpqlBuilder addScalar(String columnAlias) {
        scalars.put(columnAlias, null);
        return this;
    }

    public Query createNativeQuery(EntityManager em) {
        return createNativeQuery(em, (Sort) null);
    }

    public <T> Query createNativeQuery(EntityManager em, Class<T> resultClass) {
        return createNativeQuery(em, resultClass, null);
    }

    public Query createNativeQuery(EntityManager em, Sort sort) {
        return createNativeQuery(em, null, sort);
    }

    public <T> Query createNativeQuery(EntityManager em, Class<T> resultClass, Sort sort) {
        String sql = jpql.toString();
        if (sort != null) {
            // String alias = QueryUtils.detectAlias(sql);
            sql = QueryUtils.applySorting(sql, sort, null);
        }
        Query query;
        if (resultClass != null) {
            query = em.createNativeQuery(sql, resultClass);
        } else {
            query = em.createNativeQuery(sql);
        }
        applyParameters(query);
        return query;
    }

    public Query createQuery(EntityManager em, Sort sort) {
        String queryString = jpql.toString();
        if (sort != null) {
            String alias = QueryUtils.detectAlias(queryString);
            queryString = QueryUtils.applySorting(queryString, sort, alias);
        }
        Query query = em.createQuery(queryString);
        applyParameters(query);
        return query;
    }

    public <T> TypedQuery<T> createQuery(EntityManager em, Class<T> resultClass, Sort sort) {
        String queryString = jpql.toString();
        if (sort != null) {
            String alias = QueryUtils.detectAlias(queryString);
            queryString = QueryUtils.applySorting(queryString, sort, alias);
        }
        TypedQuery<T> query = em.createQuery(queryString, resultClass);
        applyParameters(query);
        return query;
    }

    public Query createQuery(EntityManager em) {
        return createQuery(em, (Sort) null);
    }

    public <T> TypedQuery<T> createQuery(EntityManager em, Class<T> resultClass) {
        return createQuery(em, resultClass, null);
    }

    public Query createCountQuery(EntityManager em) {
        String cqs = StringUtils.isNotBlank(getCountQueryString()) ? getCountQueryString() : QueryUtils
                .createCountQueryFor(jpql.toString(), getCountProjection());
        Query query = em.createQuery(cqs);
        applyParameters(query);
        return query;
    }

    public Query createNativeCountQuery(EntityManager em) {
        String cqs = StringUtils.isNotBlank(getCountQueryString()) ? getCountQueryString() : createCountQueryString();
        Query query = em.createNativeQuery(cqs);
        applyParameters(query);
        return query;
    }

    private void applyParameters(Query query) {
        for (Parameter parameter : parameters) {
            String name = parameter.getName();
            Object value = parameter.getValue();
            TemporalType temporalType = parameter.getTemporalType();
            if (temporalType == null) {
                query.setParameter(name, value);
            } else {
                if (value instanceof Date) {
                    query.setParameter(name, (Date) value, temporalType);
                } else if (value instanceof Calendar) {
                    query.setParameter(name, (Calendar) value, temporalType);
                } else {
                    throw new IllegalStateException("value must be java.utile.Date or java.util.Calendar");
                }
            }
        }
    }

    public String getQueryString() {
        return jpql.toString();
    }

    public String createCountQueryString() {
        Matcher m = COUNT_PATTERN.matcher(jpql);
        if (!m.matches()) {
            throw new IllegalStateException("query string invalidated: " + jpql);
        }
        StringBuilder countJpql = new StringBuilder();
        int countStart = m.start(2);
        int countEnd = m.end(2);
        int groupStart = m.start(3);
        int orderStart = m.start(4);
        int end = groupStart == -1 ? orderStart : groupStart;

        if (countStart != -1) {
            countJpql.append(jpql.substring(0, countStart));
            countJpql.append("count(");
            countJpql.append(getCountProjection() != null ? getCountProjection() : "*");
            countJpql.append(") ");
            if (end != -1) {
                countJpql.append(jpql.substring(countEnd, end));
            } else {
                countJpql.append(jpql.substring(countEnd));
            }
        } else {
            countJpql.append("select count(");
            countJpql.append(getCountProjection() != null ? getCountProjection() : "*");
            countJpql.append(") ");
            if (end != -1) {
                countJpql.append(jpql.substring(0, end));
            } else {
                countJpql.append(jpql);
            }
        }
        // 清除fetch关键字
        String queryCount = StringUtils.replace(countJpql.toString(), " fetch ", " ");
        return queryCount;
    }

    @SuppressWarnings("rawtypes")
    public List list(EntityManager em) {
        return createQuery(em).getResultList();
    }

    public <T> List<T> list(EntityManager em, Class<T> resultClass) {
        return createQuery(em, resultClass).getResultList();
    }

    @SuppressWarnings("rawtypes")
    public List list(EntityManager em, Sort sort) {
        return createQuery(em, sort).getResultList();
    }

    public <T> List<T> list(EntityManager em, Class<T> resultClass, Sort sort) {
        return createQuery(em, resultClass, sort).getResultList();
    }

    @SuppressWarnings("rawtypes")
    public List list(EntityManager em, Limitable limitable) {
        Sort sort = null;
        if (limitable != null) {
            sort = limitable.getSort();
        }
        Query query = createQuery(em, sort);
        if (limitable != null) {
            Integer firstResult = limitable.getFirstResult();
            if (firstResult != null && firstResult > 0) {
                query.setFirstResult(firstResult);
            }
            Integer maxResults = limitable.getMaxResults();
            if (maxResults != null && maxResults > 0) {
                query.setMaxResults(maxResults);
            }
        }
        return query.getResultList();
    }

    public <T> List<T> list(EntityManager em, Class<T> resultClass, Limitable limitable) {
        Sort sort = null;
        if (limitable != null) {
            sort = limitable.getSort();
        }
        TypedQuery<T> query = createQuery(em, resultClass, sort);
        if (limitable != null) {
            Integer firstResult = limitable.getFirstResult();
            if (firstResult != null && firstResult > 0) {
                query.setFirstResult(firstResult);
            }
            Integer maxResults = limitable.getMaxResults();
            if (maxResults != null && maxResults > 0) {
                query.setMaxResults(maxResults);
            }
        }
        return query.getResultList();
    }

    public <T> Page<T> page(EntityManager em, Class<T> resultClass, Pageable pageable) {
        Query countQuery = this.createCountQuery(em);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        List<T> content;
        if (total > pageable.getOffset()) {
            TypedQuery<T> query = this.createQuery(em, resultClass, pageable.getSort());
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            content = query.getResultList();
        } else {
            content = Collections.emptyList();
        }
        return new PageImpl<T>(content, pageable, total);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Page page(EntityManager em, Pageable pageable) {
        Query countQuery = this.createCountQuery(em);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        List content;
        if (total > pageable.getOffset()) {
            Query query = this.createQuery(em, pageable.getSort());
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            content = query.getResultList();
        } else {
            content = Collections.EMPTY_LIST;
        }
        return new PageImpl(content, pageable, total);
    }

    @SuppressWarnings("rawtypes")
    public List nativeList(EntityManager em) {
        return createNativeQuery(em).getResultList();
    }

    @SuppressWarnings("rawtypes")
    public List nativeList(EntityManager em, Sort sort) {
        return createNativeQuery(em, sort).getResultList();
    }

    @SuppressWarnings("rawtypes")
    public List nativeList(EntityManager em, Limitable limitable) {
        Query query = createNativeQuery(em, limitable.getSort());
        applyScalar(query);
        Integer firstResult = limitable.getFirstResult();
        if (firstResult != null && firstResult > 0) {
            query.setFirstResult(firstResult);
        }
        Integer maxResults = limitable.getMaxResults();
        if (maxResults != null && maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    @SuppressWarnings("rawtypes")
    public <T> List<T> nativeList(EntityManager em, Class<T> resultClass, Limitable limitable) {
        Query query = createNativeQuery(em, resultClass, limitable.getSort());
        applyScalar(query);
        Integer firstResult = limitable.getFirstResult();
        if (firstResult != null && firstResult > 0) {
            query.setFirstResult(firstResult);
        }
        Integer maxResults = limitable.getMaxResults();
        if (maxResults != null && maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> Page<T> nativePage(EntityManager em, Class<T> resultClass, Pageable pageable) {
        Query countQuery = this.createNativeCountQuery(em);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        List<T> content;
        if (total > pageable.getOffset()) {
            Query query = this.createNativeQuery(em, resultClass, pageable.getSort());
            applyScalar(query);
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            content = (List<T>) query.getResultList();
        } else {
            content = Collections.emptyList();
        }
        return new PageImpl<T>(content, pageable, total);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Page nativePage(EntityManager em, Pageable pageable) {
        Query countQuery = this.createNativeCountQuery(em);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        List content;
        if (total > pageable.getOffset()) {
            Query query = this.createNativeQuery(em, pageable.getSort());
            applyScalar(query);
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            content = query.getResultList();
        } else {
            content = Collections.EMPTY_LIST;
        }
        return new PageImpl(content, pageable, total);
    }

    private void applyScalar(Query query) {
        if (scalars.isEmpty()) {
            return;
        }
        SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
        for (Map.Entry<String, Type> entry : scalars.entrySet()) {
            if (entry.getValue() != null) {
                sqlQuery.addScalar(entry.getKey(), entry.getValue());
            } else {
                sqlQuery.addScalar(entry.getKey());
            }
        }
    }

    public String getCountQueryString() {
        return countQueryString;
    }

    public void setCountQueryString(String countQueryString) {
        this.countQueryString = countQueryString;
    }

    public String getCountProjection() {
        return countProjection;
    }

    public void setCountProjection(String countProjection) {
        this.countProjection = countProjection;
    }

    private static class Parameter {
        private String name;
        private Object value;
        private TemporalType temporalType;

        public Parameter(String name, Object val, TemporalType temporalType) {
            this.name = name;
            this.value = val;
            this.temporalType = temporalType;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

        public TemporalType getTemporalType() {
            return temporalType;
        }
    }
}

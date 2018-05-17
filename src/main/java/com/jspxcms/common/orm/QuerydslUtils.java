package com.jspxcms.common.orm;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.querydsl.core.SimpleQuery;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Querydsl工具类
 * 
 * @author liufang
 * 
 */
public abstract class QuerydslUtils {

	public static <T> List<T> list(JPAQuery<T> query,
			EntityPathBase<T> entityPath, Sort sort) {
		applySorting(query, entityPath, sort);
		return query.fetch();
	}

	public static <T> List<T> list(JPAQuery<T> query,
			EntityPathBase<T> entityPath, Limitable limitable) {
		applySorting(query, entityPath, limitable.getSort());
		Integer firstResult = limitable.getFirstResult();
		if (firstResult != null && firstResult > 0) {
			query.offset(firstResult);
		}
		Integer maxResults = limitable.getMaxResults();
		if (maxResults != null && maxResults > 0) {
			query.limit(maxResults);
		}
		return query.fetch();
	}

	public static <T> Page<T> page(JPAQuery<T> query,
			EntityPathBase<T> entityPath, Pageable pageable) {
		long total = query.fetchCount();
		List<T> content;
		if (total > pageable.getOffset()) {
			query.offset(pageable.getOffset());
			query.limit(pageable.getPageSize());
			applySorting(query, entityPath, pageable.getSort());
			content = query.fetch();
		} else {
			content = Collections.emptyList();
		}
		Page<T> page = new PageImpl<T>(content, pageable, total);
		return page;
	}

	public static <T, Q extends SimpleQuery<Q>> void applySorting(
			SimpleQuery<Q> query, EntityPath<T> entityPath, Sort sort) {
		if (sort == null) {
			return;
		}
		PathBuilder<T> builder = new PathBuilder<T>(entityPath.getType(),
				entityPath.getMetadata());
		for (Order order : sort) {
			query.orderBy(toOrder(builder, order));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static OrderSpecifier<?> toOrder(PathBuilder builder, Order order) {
		Expression<Object> property = builder.get(order.getProperty());
		return new OrderSpecifier(
				order.isAscending() ? com.querydsl.core.types.Order.ASC
						: com.querydsl.core.types.Order.DESC, property);
	}
}

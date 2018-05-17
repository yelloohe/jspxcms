package com.jspxcms.common.orm;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.querydsl.core.types.Predicate;

/**
 * QueryDsl Predicate Executor
 * 
 * @author liufang
 * 
 * @param <T>
 */
public interface MyQueryDslPredicateExecutor<T> extends
		QueryDslPredicateExecutor<T> {

	List<T> findAll(Predicate predicate, Sort sort);

	List<T> findAll(Predicate predicate, Limitable limitable);
}

package com.jspxcms.common.orm;

import java.io.Serializable;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * 数据库取数及排序实现
 * 
 * @author liufang
 * 
 */
public class LimitRequest implements Limitable, Serializable {
	private static final long serialVersionUID = 1L;

	private Integer firstResult;
	private Integer maxResults;
	private Sort sort;

	public LimitRequest(Integer firstResult, Integer maxResults) {
		this(firstResult, maxResults, null);
	}

	public LimitRequest(Integer firstResult, Integer maxResults,
			Direction direction, String... properties) {
		this(firstResult, maxResults, new Sort(direction, properties));
	}

	public LimitRequest(Integer firstResult, Integer maxResults, Sort sort) {
		if (firstResult != null && firstResult > 0) {
			this.firstResult = firstResult;
		}
		if (maxResults != null && maxResults > 0) {
			this.maxResults = maxResults;
		}
		this.sort = sort;
	}

	public static Limitable defaultSort(Limitable limitable, Sort sort) {
		if (limitable == null) {
			limitable = new LimitRequest(null, null, sort);
		} else if (limitable.getSort() == null) {
			limitable.setSort(sort);
		}
		return limitable;
	}

	public Integer getFirstResult() {
		return this.firstResult;
	}

	public Integer getMaxResults() {
		return this.maxResults;
	}

	public int getLastResult() {
		int lastResult = 0;
		Integer firstResult = getFirstResult();
		if (firstResult != null && firstResult > 0) {
			lastResult += firstResult;
		}
		Integer maxResults = getMaxResults();
		if (maxResults != null && maxResults > 0) {
			lastResult += maxResults;
		}
		return lastResult;
	}

	public Sort getSort() {
		return this.sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
}

package com.jspxcms.common.orm;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;


/**
 * 上一条、下一条
 * 
 * @author liufang
 * 
 * @param <T>
 */
public class RowSide<T> {
	private T prev;
	private T next;
	private T self;

	public RowSide() {
	}

	public RowSide(T prev, T self, T next) {
		this.prev = prev;
		this.self = self;
		this.next = next;
	}

	public static <T> RowSide<T> create(List<T> list, T bean) {
		if (CollectionUtils.isEmpty(list) || bean == null) {
			return new RowSide<T>();
		}
		int len = list.size();
		if (len == 3 && list.get(1).equals(bean)) {
			return new RowSide<T>(list.get(0), list.get(1), list.get(2));
		} else if (len == 2) {
			if (list.get(0).equals(bean)) {
				return new RowSide<T>(null, list.get(0), list.get(1));
			} else if (list.get(1).equals(bean)) {
				return new RowSide<T>(list.get(0), list.get(1), null);
			}
		} else if (len == 1 && list.get(0).equals(bean)) {
			return new RowSide<T>(null, list.get(0), null);
		}
		return new RowSide<T>();
	}

	public static Limitable limitable(int position, Sort sort) {
		return new LimitRequest(position == 0 ? position : position - 1,
				position == 0 ? 2 : 3, sort);
	}

	public T getSelf() {
		return self;
	}

	public void setSelf(T self) {
		this.self = self;
	}

	public T getPrev() {
		return prev;
	}

	public void setPrev(T prev) {
		this.prev = prev;
	}

	public T getNext() {
		return next;
	}

	public void setNext(T next) {
		this.next = next;
	}
}

package com.jspxcms.common.orm;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 * 把空字符串转换成null，使程序在判断上更为方便。
 * 另外由于oracle的varchar2会自动把空字符串转换成null，所以加上这个转换可以让使数据库行为更统一。
 * 
 * @author liufang
 * 
 */
public class EmptyToNullInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		// 空字符串转换成null
		boolean modified = false;
		for (int i = 0, len = state.length; i < len; i++) {
			if (state[i] instanceof String) {
				if ("".equals(state[i])) {
					state[i] = null;
					modified = true;
				}
			}
		}
		return modified;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		// 空字符串转换成null
		boolean modified = false;
		for (int i = 0, len = currentState.length; i < len; i++) {
			if (currentState[i] instanceof String) {
				if ("".equals(currentState[i])) {
					currentState[i] = null;
					modified = true;
				}
			}
		}
		return modified;
	}
}

package com.jspxcms.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

public abstract class Reflections {
	public static List<Object> getPropertyList(Collection<?> coll, String field) {
		List<Object> properties = new ArrayList<Object>();
		if (CollectionUtils.isEmpty(coll)) {
			return properties;
		}
		for (Object obj : coll) {
			properties.add(getPerperty(obj, field));
		}
		return properties;
	}

	public static boolean contains(Collection<?> coll, Object value,
			String field) {
		if (CollectionUtils.isEmpty(coll) || value == null || field == null) {
			return false;
		}
		Object value1;
		for (Object obj : coll) {
			value1 = getPerperty(obj, field);
			if (value.equals(value1)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsAny(Collection<?> coll1, Collection<?> coll2,
			String field) {
		if (CollectionUtils.isEmpty(coll1) || CollectionUtils.isEmpty(coll2)
				|| StringUtils.isBlank(field)) {
			return false;
		}
		Object val1 = null, val2 = null;
		if (coll1.size() > coll2.size()) {
			Collection<?> temp = coll1;
			coll1 = coll2;
			coll2 = temp;
		}
		for (Object obj2 : coll2) {
			val2 = getPerperty(obj2, field);
			if (val2 == null) {
				continue;
			}
			for (Object obj1 : coll1) {
				val1 = getPerperty(obj1, field);
				if (val2.equals(val1)) {
					return true;
				}
			}
		}
		return false;
	}

	public static Object getPerperty(Object obj, String field) {
		if (obj == null || field == null) {
			return null;
		}
		PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(
				obj.getClass(), field);
		Method readMethod = descriptor.getReadMethod();
		return invoke(readMethod, obj);
	}

	public static Object invoke(Method method, Object obj, Object... args) {
		try {
			return method.invoke(obj, args);
		} catch (Throwable ex) {
			throw new FatalBeanException("Could not read properties", ex);
		}
	}
}

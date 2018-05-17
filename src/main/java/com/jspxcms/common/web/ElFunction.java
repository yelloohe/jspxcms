package com.jspxcms.common.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * el调用方法
 * 
 * @author liufang
 * 
 */
public abstract class ElFunction {
	/**
	 * 当前时间
	 * 
	 * @return
	 */
	public static Date now() {
		return new Date();
	}

	/**
	 * 获取布尔值
	 * 
	 * EL表达式在获取spring-data-commons Chunk.isFirst,Chunk.isLast时出现错误：Class
	 * javax.el.BeanELResolver can not access a member of class
	 * org.springframework.data.domain.Chunk with modifiers "public"。
	 * 
	 * jdk8没有这个问题，最新的jdk6、jdk7配合最新的tomcat6、tomcat7可能也没有这个问题。但是旧一点的jdk6、
	 * jdk7或者旧一点tomcat6、 tomcat7有这个问题，如6.0.37、7.0.47。
	 * 
	 * spring-data-commons 1.8.0以上版本有这个问题，对应spring-data-jpa
	 * 1.6.0以上版本。可以使用spring-data-jpa 1.5.3及以下版本。
	 * 
	 * @param obj
	 * @param method
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static boolean bool(Object obj, String method)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if (obj == null || StringUtils.isBlank(method)) {
			return false;
		}
		Class<?> clazz = obj.getClass();
		Object result = clazz.getMethod(method).invoke(obj);
		return Boolean.TRUE.equals(result);
	}

	public static boolean contains(Collection<?> container, Object element) {
		if (container == null || element == null) {
			return false;
		}
		return container.contains(element);
	}

	public static boolean contains(Collection<?> container, String property,
			Object element) {
		if (container == null || StringUtils.isBlank(property)
				|| element == null) {
			return false;
		}
		boolean contains = false;
		try {
			for (Object obj : container) {
				Object value = PropertyUtils.getProperty(obj, property);
				if (element.equals(value)) {
					return true;
				}
			}
		} catch (Exception e) {
			return contains;
		}
		return contains;
	}

	public static Object invoke(Object obj, String method)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (obj == null || StringUtils.isBlank(method)) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		return clazz.getMethod(method).invoke(obj);
	}

	public static Object invoke(Object obj, String methodName, Object arg0)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (obj == null || StringUtils.isBlank(methodName)) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		Method method;
		if (arg0 != null) {
			method = clazz.getMethod(methodName, arg0.getClass());
		} else {
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.getName().equals(methodName)
						&& m.getParameterTypes().length == 1) {
					method = m;
				}
			}
			throw new NoSuchMethodException(clazz.getName() + "." + methodName
					+ "(Object arg0)");
		}
		return method.invoke(obj, arg0);
	}

	public static Object invoke(Object obj, String methodName, Object arg0,
			Object arg1) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (obj == null || StringUtils.isBlank(methodName)) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		Method method;
		if (arg0 != null && arg1 != null) {
			method = clazz.getMethod(methodName, arg0.getClass(),
					arg1.getClass());
		} else {
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.getName().equals(methodName)
						&& m.getParameterTypes().length == 2) {
					method = m;
				}
			}
			throw new NoSuchMethodException(clazz.getName() + "." + methodName
					+ "(Object arg0,Object arg1)");
		}
		return method.invoke(obj, arg0, arg1);
	}

	public static Object invoke(Object obj, String methodName, Object arg0,
			Object arg1, Object arg2) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (obj == null || StringUtils.isBlank(methodName)) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		Method method;
		if (arg0 != null && arg1 != null && arg2 != null) {
			method = clazz.getMethod(methodName, arg0.getClass(),
					arg1.getClass(), arg2.getClass());
		} else {
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.getName().equals(methodName)
						&& m.getParameterTypes().length == 3) {
					method = m;
				}
			}
			throw new NoSuchMethodException(clazz.getName() + "." + methodName
					+ "(Object arg0,Object arg1,Object arg2)");
		}
		return method.invoke(obj, arg0, arg1, arg2);
	}

	public static Object invoke(Object obj, String methodName, Object arg0,
			Object arg1, Object arg2, Object arg3) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (obj == null || StringUtils.isBlank(methodName)) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		Method method;
		if (arg0 != null && arg1 != null && arg2 != null) {
			method = clazz.getMethod(methodName, arg0.getClass(),
					arg1.getClass(), arg2.getClass(), arg3.getClass());
		} else {
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.getName().equals(methodName)
						&& m.getParameterTypes().length == 4) {
					method = m;
				}
			}
			throw new NoSuchMethodException(clazz.getName() + "." + methodName
					+ "(Object arg0,Object arg1,Object arg2,Object arg3)");
		}
		return method.invoke(obj, arg0, arg1, arg2, arg3);
	}
}

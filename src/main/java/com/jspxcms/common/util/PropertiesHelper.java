package com.jspxcms.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Spring properties工具类
 * 
 * @author liufang
 * 
 */
public class PropertiesHelper implements BeanFactoryAware {
	private BeanFactory beanFactory;
	private Properties properties;

	public static Map<String, String> getMap(Properties properties, String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyMap();
		}
		Map<String, String> map = new HashMap<String, String>();
		int len = prefix.length();
		for (String key : properties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				map.put(key.substring(len), properties.getProperty(key));
			}
		}
		return map;
	}

	/**
	 * 获得property list
	 * 
	 * @param prefix
	 *            前缀
	 * @return
	 */
	public static List<String> getList(Properties properties, String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<String>();
		for (String key : properties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				list.add(properties.getProperty(key));
			}
		}
		return list;
	}

	/**
	 * 获取有序的Map
	 * 
	 * eg:List<String> types = getSortedMap("chain");
	 * 
	 * <ul>
	 * <li>chain[100]/ = anon
	 * <li>chain[200]*.jspx = anon
	 * <li>chain[300]/login.jspx = authc
	 * <li>chain[400]/cmscp/** = backSite,user
	 * </ul>
	 * 
	 * @param prefix
	 * @return
	 */
	public static Map<String, String> getSortedMap(Properties properties, String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyMap();
		}
		if (prefix.endsWith(".")) {
			prefix = prefix.substring(0, prefix.length() - 1) + "[";
		} else {
			prefix = prefix + "[";
		}
		Map<Integer, Map<String, String>> map = new TreeMap<Integer, Map<String, String>>();
		int len = prefix.length();
		for (String key : properties.stringPropertyNames()) {
			int end = key.indexOf(']', len);
			if (key.startsWith(prefix) && end != -1) {
				Map<String, String> m = new HashMap<String, String>();
				m.put(key.substring(end + 1), properties.getProperty(key));
				map.put(Integer.parseInt(key.substring(len, key.indexOf(']', len))), m);
			}
		}
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (Map<String, String> m : map.values()) {
			result.putAll(m);
		}
		return result;
	}

	/**
	 * 获取有序的List
	 * 
	 * eg:List<String> types = getSortedList("modelType");
	 * 
	 * <ul>
	 * <li>modelType[100]=info
	 * <li>modelType[200]=node
	 * <li>modelType[300]=node_home
	 * </ul>
	 * 
	 * @param prefix
	 * @return
	 */
	public static List<String> getSortedList(Properties properties, String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptyList();
		}
		if (prefix.endsWith(".")) {
			prefix = prefix.substring(0, prefix.length() - 1) + "[";
		} else {
			prefix = prefix + "[";
		}
		Map<Integer, String> map = new TreeMap<Integer, String>();
		int len = prefix.length();
		for (String key : properties.stringPropertyNames()) {
			if (key.startsWith(prefix) && key.endsWith("]")) {
				map.put(Integer.parseInt(key.substring(len, key.length() - 1)), properties.getProperty(key));
			}
		}
		List<String> list = new ArrayList<String>(map.values());
		return list;
	}

	public static Properties getProperties(Properties properties, String prefix) {
		Properties props = new Properties();
		if (properties == null || prefix == null) {
			return props;
		}
		int len = prefix.length();
		for (String key : properties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				props.put(key.substring(len), properties.getProperty(key));
			}
		}
		return props;
	}

	/**
	 * 获得property list
	 * 
	 * @param prefix
	 *            前缀
	 * @return
	 */
	public List<String> getList(String prefix) {
		return getList(properties, prefix);
	}

	/**
	 * 获取有序的列表
	 * 
	 * eg:List<String> types = getSortedList("modelType.");
	 * 
	 * <ul>
	 * <li>modelType.100=info
	 * <li>modelType.200=node
	 * <li>modelType.300=node_home
	 * </ul>
	 * 
	 * @param prefix
	 * @return
	 */
	public List<String> getSortedList(String prefix) {
		return getSortedList(properties, prefix);
	}

	public Map<String, String> getMap(String prefix) {
		return getMap(properties, prefix);
	}

	public Map<String, String> getSortedMap(String prefix) {
		return getSortedMap(properties, prefix);
	}

	public Properties getProperties(String prefix) {
		return getProperties(properties, prefix);
	}

	public <T> Map<String, T> getBeanMap(String prefix, Class<T> requiredType) {
		Map<String, String> nameMap = getMap(prefix);
		if (nameMap.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, T> objectMap = new HashMap<String, T>(nameMap.size());
		String key, value;
		for (Map.Entry<String, String> entry : nameMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			objectMap.put(key, beanFactory.getBean(value, requiredType));
		}
		return objectMap;
	}

	public Map<String, Object> getBeanMap(String prefix) {
		return getBeanMap(prefix, Object.class);
	}

	public Map<String, ?> getBeanMap(String prefix, String className) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className);
		return getBeanMap(prefix, clazz);
	}

	public <T> List<T> getBeanList(String prefix, Class<T> requiredType) {
		List<String> nameList = getList(prefix);
		if (nameList.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> objectList = new ArrayList<T>(nameList.size());
		for (String name : nameList) {
			objectList.add(beanFactory.getBean(name, requiredType));
		}
		return objectList;
	}

	public List<Object> getBeanList(String prefix) {
		return getBeanList(prefix, Object.class);
	}

	public List<?> getBeanList(String prefix, String className) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className);
		return getBeanList(prefix, clazz);
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}

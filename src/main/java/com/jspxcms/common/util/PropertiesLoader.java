package com.jspxcms.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderSupport;

/**
 * 使用非工厂模式加载Properties，方便直接在代码中使用。{@link org.springframework.beans.factory.config.PropertiesFactoryBean}适合在xml配置文件中使用。
 * <p>
 * 使用PathMatchingResourcePatternResolver加载文件，不依赖{@link org.springframework.context.ApplicationContext}，
 * 避免在SpringBoot环境下出现很晚才能初始化ApplicationContext的问题。如在加载Filter时，
 * {@link org.springframework.beans.factory.config.PropertiesFactoryBean}不能被初始化。
 * 
 * @author liufang
 *
 */
public class PropertiesLoader extends PropertiesLoaderSupport {
	private String[] value;

	public String[] getValue() {
		return value;
	}

	/**
	 * 同时转换为Resource，并setLocations。支持通配符
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void setValue(String... value) throws IOException {
		if (value != null) {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			List<Resource> resources = new ArrayList<Resource>(value.length);
			for (String v : value) {
				resources.addAll(Arrays.asList(resolver.getResources(v)));
			}
			setLocations(resources.toArray(new Resource[resources.size()]));
		}
		this.value = value;
	}

	public Properties createProperties() throws IOException {
		return mergeProperties();
	}

}

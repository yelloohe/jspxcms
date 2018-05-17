package com.jspxcms.core;

import com.jspxcms.common.security.SHA1CredentialsDigest;
import com.jspxcms.common.util.PropertiesHelper;
import com.jspxcms.common.util.PropertiesLoader;
import com.jspxcms.common.web.JspDispatcherFilter;
import com.jspxcms.core.security.CmsAuthenticationFilter;
import com.jspxcms.core.security.CmsLogoutFilter;
import com.jspxcms.core.security.CmsUserFilter;
import com.jspxcms.core.security.ShiroDbRealm;
import com.jspxcms.core.support.BackSiteFilter;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfig {
	@Primary
	@Bean("properties")
	public Properties properties() throws IOException {
		PropertiesLoader loader = new PropertiesLoader();
		loader.setFileEncoding("UTF-8");
		loader.setValue("classpath:conf/plugin/**/conf*.properties", "classpath:conf/conf.properties");
		Properties properties = loader.createProperties();
		return properties;
	}

	@Bean("propertiesHelper")
	public PropertiesHelper propertiesHelper() throws IOException {
		PropertiesHelper propertiesHelper = new PropertiesHelper();
		propertiesHelper.setProperties(properties());
		return propertiesHelper;
	}

	@Bean("credentialsDigest")
	public SHA1CredentialsDigest credentialsDigest() {
		return new SHA1CredentialsDigest();
	}

	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean("shiroRealm")
	public Realm shiroRealm() {
		ShiroDbRealm realm = new ShiroDbRealm();
		realm.setAuthorizationCachingEnabled(false);
		return realm;
	}

	@Bean("shiroEhcacheManager")
	public EhCacheManager shiroEhCacheManager() throws IOException {
		EhCacheManager ehCacheManager = new EhCacheManager();
		String cacheManagerConfigFile = properties().getProperty("shiroCacheManagerConfigFile");
		ehCacheManager.setCacheManagerConfigFile(cacheManagerConfigFile);
		// ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/shiro-ehcache.xml");
		return ehCacheManager;
	}

	@Bean("securityManager")
	public DefaultWebSecurityManager securityManager() throws IOException {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		securityManager.setCacheManager(shiroEhCacheManager());
		return securityManager;
	}

	@Bean("shiroFilter")
	@DependsOn("propertiesHelper")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(BeanFactory beanFactory) throws IOException {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager());
		factoryBean.setLoginUrl("/login");
		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		filters.put("backSite", new BackSiteFilter(beanFactory));
		filters.put("authc", new CmsAuthenticationFilter(beanFactory));
		filters.put("user", new CmsUserFilter());
		filters.put("logout", new CmsLogoutFilter(beanFactory));
		factoryBean.setFilters(filters);
		Map<String, String> filterChainDefinitionMap = propertiesHelper()
				.getSortedMap("shiroFilterChainDefinitionMap.");
		// Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// filterChainDefinitionMap.put("/*", "anon");
		// filterChainDefinitionMap.put("*", "anon");
		// filterChainDefinitionMap.put("*.jsp", "anon");
		// filterChainDefinitionMap.put("/login", "authc");
		// filterChainDefinitionMap.put("/logout", "logout");
		// filterChainDefinitionMap.put("/my", "user");
		// filterChainDefinitionMap.put("/my/**", "user");
		// filterChainDefinitionMap.put("/cmscp/", "backSite,anon");
		// filterChainDefinitionMap.put("/cmscp/index.do", "backSite,anon");
		// filterChainDefinitionMap.put("/cmscp/login.do", "backSite,authc");
		// filterChainDefinitionMap.put("/cmscp/logout.do", "backSite,logout");
		// filterChainDefinitionMap.put("/cmscp/**", "backSite,user");
		factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return factoryBean;
	}

	// @Bean
	// public FilterRegistrationBean timerFilterRegistrationBean() {
	// FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
	// filterRegistration.setFilter(new TimerFilter());
	// filterRegistration.setEnabled(true);
	// filterRegistration.addUrlPatterns("/*");
	// filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
	// return filterRegistration;
	// }

	@Bean
	public FilterRegistrationBean jspDispatcherFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new JspDispatcherFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addInitParameter("prefix", "/jsp");
		filterRegistration.addUrlPatterns("*.jsp");
		filterRegistration.addUrlPatterns("*.jspx");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistration;
	}

	@Bean
	public FilterRegistrationBean openEntityManagerInViewFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new OpenEntityManagerInViewFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Bean
	public FilterRegistrationBean shiroFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.setEnabled(true);
		filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}
}

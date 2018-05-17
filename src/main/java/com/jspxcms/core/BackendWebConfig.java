package com.jspxcms.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.jspxcms.common.web.BindingInitializer;
import com.jspxcms.common.web.PageableArgumentResolver;
import com.jspxcms.common.web.TimerInterceptor;
import com.jspxcms.core.support.BackInterceptor;

/**
 * 后台Web配置类
 */
@Configuration
@ComponentScan({ "com.jspxcms.core.web.back", "com.jspxcms.ext.web.back" })
@ImportResource({ "classpath:conf/**/backend*.xml" })
public class BackendWebConfig extends DelegatingWebMvcConfiguration {
	/**
	 * 覆盖父类方法，避免调用{@link Application.WebConfig}的配置信息
	 */
	@Override
	public void setConfigurers(List<WebMvcConfigurer> configurers) {
		// do nothing.
	}

	/**
	 * 注册{@link BindingInitializer}和{@link PageableArgumentResolver}
	 */
	@Bean
	@Override
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
		adapter.setWebBindingInitializer(new BindingInitializer());
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
		argumentResolvers.add(new PageableArgumentResolver());
		adapter.setCustomArgumentResolvers(argumentResolvers);
		return adapter;
	}

	/**
	 * 后台拦截器
	 * 
	 * @return
	 */
	@Bean
	public BackInterceptor backInterceptor() {
		return new BackInterceptor();
	}

	/**
	 * 注册拦截器。包括执行计时拦截器和后台拦截器。
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TimerInterceptor());
		registry.addInterceptor(backInterceptor());
		super.addInterceptors(registry);
	}

	/**
	 * Shiro方法级权限验证代理。必须定义在BackendWebConfig里面，否则无效。
	 * 
	 * @return
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}

	/**
	 * Shiro方法级权限验证Advisor。必须定义在BackendWebConfig里面，否则无效。
	 * 
	 * @param securityManager
	 * @return
	 * @throws IOException
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager securityManager) throws IOException {
		AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		sourceAdvisor.setSecurityManager(securityManager);
		return sourceAdvisor;
	}
}

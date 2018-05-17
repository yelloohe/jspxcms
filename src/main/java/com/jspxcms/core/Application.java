package com.jspxcms.core;

import java.util.Date;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.DeviceResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jspxcms.common.web.DateEditor;
import com.jspxcms.common.web.StringEmptyEditor;
import com.jspxcms.common.web.TimerInterceptor;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.support.ForeInterceptor;

/**
 * 应用启动的主入口
 */
@Configuration
// 此三项也未用到，但二次开发时有可能会用到，暂时不屏蔽：JdbcTemplateAutoConfiguration.class, JtaAutoConfiguration.class,
// WebSocketAutoConfiguration.class
// 暂时不用Mobile的自动配置：DeviceResolverAutoConfiguration.class, SitePreferenceAutoConfiguration.class
@EnableAutoConfiguration(exclude = { MessageSourceAutoConfiguration.class, JmxAutoConfiguration.class,
		CacheAutoConfiguration.class, DeviceResolverAutoConfiguration.class, SitePreferenceAutoConfiguration.class })
@Import({ ContextConfig.class, ShiroConfig.class, MenuConfig.class })
@PropertySource({ "classpath:conf/spring.jpa.properties", "${weixin.config.file}" })
@ImportResource({ "classpath:conf/**/context*.xml", "classpath:custom.xml" })
// 创建war需要继承SpringBootServletInitializer
// Weblogic要求直接实现WebApplicationInitializer接口，即使SpringBootServletInitializer已经实现了这个接口
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer {
	/**
	 * war方式启动的处理方法
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}

	/**
	 * jar方式启动的处理方法
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		configureApplication(new SpringApplicationBuilder()).run(args);
	}

	/**
	 * war方式启动和jar方式启动共用的配置
	 * 
	 * @param builder
	 * @return
	 */
	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		return builder.sources(Application.class).listeners(
				new ApplicationListener<ApplicationEnvironmentPreparedEvent>() {
					// 在应用环境准备好后执行（Application.properties和PoropertySource已读取），此时BeanFactory还未准备好（Bean还未创建）
					@Override
					public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
						ConfigurableEnvironment env = event.getEnvironment();
						// 用配置文件中的内容覆盖替代Constants的内容
						Constants.loadEnvironment(env);
					}
				});
	}

	/**
	 * 后台DispatcherServlet。前后台使用不同的DispatcherServlet
	 * 
	 * @return
	 */
	@Bean(name = "backendDispatcherServlet")
	public DispatcherServlet backendDispatcherServlet() {
		AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
		// 使用BackendWebConfig作为配置类
		servletAppContext.register(BackendWebConfig.class);
		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		return dispatcherServlet;
	}

	/**
	 * 注册后台DispatcherServlet。只处理`/cmscp/*`相关请求
	 * 
	 * @param multipartConfigProvider
	 *            获取springboot自动定义的上传配置对象，实现前后台统一的上传配置
	 * @return
	 */
	@Bean(name = "backendDispatcherServletRegistration")
	public ServletRegistrationBean backendDispatcherServletRegistration(
			ObjectProvider<MultipartConfigElement> multipartConfigProvider) {
		ServletRegistrationBean registration = new ServletRegistrationBean(backendDispatcherServlet(), "/cmscp/*");
		// 必须指定启动优先级，否则无法生效
		registration.setLoadOnStartup(1);
		registration.setName("backendDispatcherServlet");
		// 注册上传配置对象，否则后台不能处理上传
		MultipartConfigElement multipartConfig = multipartConfigProvider.getIfAvailable();
		if (multipartConfig != null) {
			registration.setMultipartConfig(multipartConfig);
		}
		return registration;
	}

	/**
	 * 注册类型转换器。主要包括日期转换器和字符串转换器。
	 * 
	 * @return
	 */
	@Bean
	public ConfigurableWebBindingInitializer configurableWebBindingInitializer() {
		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		PropertyEditorRegistrar registrar = new PropertyEditorRegistrar() {
			@Override
			public void registerCustomEditors(PropertyEditorRegistry registry) {
				registry.registerCustomEditor(Date.class, new DateEditor());
				registry.registerCustomEditor(String.class, new StringEmptyEditor());
			}
		};
		initializer.setPropertyEditorRegistrar(registrar);
		return initializer;
	}

	@Configuration
	protected static class WebConfig extends WebMvcConfigurerAdapter {
		@Bean
		public ForeInterceptor foreInterceptor() {
			return new ForeInterceptor();
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new TimerInterceptor());
			registry.addInterceptor(foreInterceptor()).excludePathPatterns("/error/**");
			super.addInterceptors(registry);
		}
	}
}
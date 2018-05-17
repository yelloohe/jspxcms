package com.jspxcms.common.freemarker;

import java.util.List;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.TemplateLoader;

/**
 * FreeMarker配置类
 * 
 * @author liufang
 * 
 */
public class AdapterFreeMarkerConfigurer extends FreeMarkerConfigurer {
	@Override
	protected void postProcessTemplateLoaders(
			List<TemplateLoader> templateLoaders) {
		for (int i = 0, len = templateLoaders.size(); i < len; i++) {
			templateLoaders.set(i,
					new AdapterTemplateLoader(templateLoaders.get(i)));
		}
		super.postProcessTemplateLoaders(templateLoaders);
	}
}

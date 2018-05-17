package com.jspxcms.common.freemarker;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

/**
 * FreeMarker视图
 * 
 * @author liufang
 * 
 */
public class MyFreeMarkerViewResolver extends AbstractTemplateViewResolver {
	public MyFreeMarkerViewResolver() {
		setViewClass(requiredViewClass());
	}

	protected Class<MyFreeMarkerView> requiredViewClass() {
		return MyFreeMarkerView.class;
	}
}

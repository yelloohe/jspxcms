package com.jspxcms.common.freemarker;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.jspxcms.common.web.ImageAnchor;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * FreeMarker ImageAnchor标签
 * 
 * 生成html img标签
 * 
 * @author liufang
 * 
 */
public class ImageAnchorDirective implements TemplateDirectiveModel {
	public static final String BEAN = "bean";
	public static final String TARGET = "target";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String CLAZZ = "class";
	public static final String STYLE = "style";
	public static final String IMG_CLAZZ = "imgClass";
	public static final String IMG_STYLE = "imgStyle";
	public static final String ATTRS = "attrs";
	public static final String IMG_ATTRS = "imgAttrs";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		ImageAnchor img = Freemarkers
				.getObject(params, BEAN, ImageAnchor.class);

		String target = Freemarkers.getString(params, TARGET);
		if (StringUtils.isBlank(target) && img.getNewWindow() != null
				&& img.getNewWindow()) {
			target = "_blank";
		}
		Integer width = Freemarkers.getInteger(params, WIDTH);
		if (width == null) {
			width = img.getWidth();
		}
		Integer height = Freemarkers.getInteger(params, HEIGHT);
		if (height == null) {
			height = img.getHeight();
		}
		String clazz = Freemarkers.getString(params, CLAZZ);
		String style = Freemarkers.getString(params, STYLE);
		String imgClazz = Freemarkers.getString(params, IMG_CLAZZ);
		String imgStyle = Freemarkers.getString(params, IMG_STYLE);

		String attrs = Freemarkers.getString(params, ATTRS);
		String imgAttrs = Freemarkers.getString(params, IMG_ATTRS);
		String src = img.getSrc();
		String title = HtmlUtils.htmlEscape(img.getTitle());

		StringBuilder buff = new StringBuilder();
		buff.append("<a href=\"").append(img.getUrl()).append("\"");
		buff.append(" title=\"").append(title).append("\"");
		if (StringUtils.isNotBlank(target)) {
			buff.append(" target=\"").append(target).append("\"");
		}
		if (StringUtils.isNotBlank(clazz)) {
			buff.append(" class=\"").append(clazz).append("\"");
		}
		if (StringUtils.isNotBlank(style)) {
			buff.append(" style=\"").append(style).append("\"");
		}
		if (StringUtils.isNotBlank(attrs)) {
			buff.append(" ").append(attrs);
		}
		buff.append(">");
		buff.append("<img src=\"").append(src).append("\"");
		buff.append(" alt=\"").append(title).append("\"");
		if (width != null) {
			buff.append(" width=\"").append(width).append("\"");
		}
		if (height != null) {
			buff.append(" height=\"").append(height).append("\"");
		}
		if (StringUtils.isNotBlank(imgClazz)) {
			buff.append(" class=\"").append(imgClazz).append("\"");
		}
		if (StringUtils.isNotBlank(imgStyle)) {
			buff.append(" style=\"").append(imgStyle).append("\"");
		}
		if (StringUtils.isNotBlank(imgAttrs)) {
			buff.append(" ").append(imgAttrs);
		}
		buff.append("/>");
		buff.append("</a>");
		env.getOut().write(buff.toString());
	}
}

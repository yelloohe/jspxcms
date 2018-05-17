package com.jspxcms.common.freemarker;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.jspxcms.common.util.Strings;
import com.jspxcms.common.web.Anchor;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * Anchor FreeMarker标签
 * 
 * 生成html a标签
 * 
 * @author liufang
 * 
 */
public class AnchorDirective implements TemplateDirectiveModel {
	public static final String BEAN = "bean";
	public static final String TARGET = "target";
	public static final String LENGTH = "length";
	public static final String APPEND = "append";
	public static final String CLAZZ = "class";
	public static final String STYLE = "style";
	public static final String ATTRS = "attrs";
	public static final String IS_STYLE = "isStyle";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Anchor a = Freemarkers.getObject(params, BEAN, Anchor.class);

		String target = Freemarkers.getString(params, TARGET);
		if (StringUtils.isBlank(target) && a.getNewWindow() != null
				&& a.getNewWindow()) {
			target = "_blank";
		}
		String append = Freemarkers.getString(params, APPEND);
		if (append == null) {
			append = "...";
		}
		Integer length = Freemarkers.getInteger(params, LENGTH);
		String clazz = Freemarkers.getString(params, CLAZZ);
		String style = Freemarkers.getString(params, STYLE);
		String attrs = Freemarkers.getString(params, ATTRS);
		boolean isStyle = Freemarkers.getBoolean(params, IS_STYLE, true);

		if (isStyle) {
			boolean isStrong = a.getStrong() != null && a.getStrong();
			boolean isEm = a.getEm() != null && a.getEm();
			String color = a.getColor();
			StringBuilder clazzBuff = new StringBuilder();
			if (isStrong) {
				clazzBuff.append("strong ");
			}
			if (isEm) {
				clazzBuff.append("em ");
			}
			if (StringUtils.isNotBlank(clazz)) {
				clazzBuff.append(clazz);
			}
			clazz = clazzBuff.toString();
			if (StringUtils.isNotBlank(color)) {
				StringBuilder styleBuff = new StringBuilder();
				styleBuff.append("color:" + color + ";");
				if (StringUtils.isNotBlank(style)) {
					styleBuff.append(style);
				}
				style = styleBuff.toString();
			}
		}

		StringBuilder buff = new StringBuilder();
		buff.append("<a href=\"").append(a.getUrl()).append("\"");
		String title = HtmlUtils.htmlEscape(a.getTitle());
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
		if (length != null && length > 0) {
			title = HtmlUtils.htmlEscape(Strings.substring(a.getTitle(),
					length, append));
		}
		buff.append(title);
		buff.append("</a>");
		env.getOut().write(buff.toString());
	}
}

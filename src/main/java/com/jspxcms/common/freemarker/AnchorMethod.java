package com.jspxcms.common.freemarker;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.jspxcms.common.util.Strings;
import com.jspxcms.common.web.Anchor;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Anchor FreeMarker方法
 * 
 * 生成html a标签
 * 
 * @author liufang
 * 
 */
@Deprecated
public class AnchorMethod implements TemplateMethodModelEx {
	private static class Params {
		public Integer length;
		public String append;
		public String target;
		public String attrs;
	}

	// anchor,params(length,append,target)
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		int argsSize = args.size();
		if (argsSize < 1) {
			throw new TemplateModelException("Wrong arguments");
		}
		TemplateModel arg0 = (TemplateModel) args.get(0);
		Anchor a = Freemarkers.getObject(arg0, "arg0", Anchor.class);
		Params params;
		if (argsSize > 1) {
			TemplateModel arg1 = (TemplateModel) args.get(1);
			params = Freemarkers.getParams(arg1, "arg1", Params.class);
		} else {
			params = new Params();
		}
		String target = params.target;
		if (StringUtils.isBlank(target) && a.getNewWindow() != null
				&& a.getNewWindow()) {
			target = "_blank";
		}
		String append = params.append;
		if (append == null) {
			append = "...";
		}
		Integer length = params.length;

		StringBuilder buff = new StringBuilder();
		buff.append("<a href=\"").append(a.getUrl()).append("\"");
		String title = HtmlUtils.htmlEscape(a.getTitle());
		buff.append(" title=\"").append(title).append("\"");
		if (StringUtils.isNotBlank(target)) {
			buff.append(" target=\"").append(target).append("\"");
		}
		if (StringUtils.isNotBlank(params.attrs)) {
			buff.append(" ").append(params.attrs);
		}
		buff.append(">");
		boolean isStrong = a.getStrong() != null && a.getStrong();
		boolean isEm = a.getEm() != null && a.getEm();
		String color = a.getColor();
		if (isStrong) {
			buff.append("<strong>");
		}
		if (isEm) {
			buff.append("<em>");
		}
		if (StringUtils.isNotBlank(color)) {
			buff.append("<span style=\"color:").append(color).append(";\">");
		}
		if (length != null && length > 0) {
			title = HtmlUtils.htmlEscape(Strings.substring(a.getTitle(),
					length, append));
		}
		buff.append(title);
		if (StringUtils.isNotBlank(color)) {
			buff.append("</span>");
		}
		if (isEm) {
			buff.append("</em>");
		}
		if (isStrong) {
			buff.append("</strong>");
		}
		buff.append("</a>");

		return buff.toString();
	}
}

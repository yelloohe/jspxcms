package com.jspxcms.common.freemarker;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 社会化日期显示
 * 
 * <li>arg0：日期，必选。
 * <li>arg1：语言，可选。默认值为zh_CN。
 * 
 * @author liufang
 *
 */
public class PrettyTimeMethod implements TemplateMethodModelEx {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		Date date;
		if (args.size() > 0) {
			TemplateModel arg0 = (TemplateModel) args.get(0);
			date = Freemarkers.getDate(arg0, "arg0");
		} else {
			throw new TemplateModelException("arg0 is missing!");
		}
		if (date == null) {
			return null;
		}
		String language = "zh_CN";
		if (args.size() > 1) {
			TemplateModel arg1 = (TemplateModel) args.get(1);
			language = Freemarkers.getString(arg1, "arg1");
		}
		String result = null;
		PrettyTime pt = new PrettyTime(new Locale(language));
		result = pt.format(date);
		// 中文需要去除空格
		if ("zh_CN".equalsIgnoreCase(language)) {
			result = result.replace(" ", "");
		}
		return result;
	}
}
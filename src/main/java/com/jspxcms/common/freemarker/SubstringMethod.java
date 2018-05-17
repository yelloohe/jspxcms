package com.jspxcms.common.freemarker;

import java.util.List;

import com.jspxcms.common.util.Strings;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * FreeMarker字符串截断方法
 * 
 * 英文字符算半个，中文字符算一个。
 * 
 * @author liufang
 * 
 */
public class SubstringMethod implements TemplateMethodModelEx {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		String text;
		if (args.size() > 0) {
			TemplateModel arg0 = (TemplateModel) args.get(0);
			text = Freemarkers.getString(arg0, "arg0");
		} else {
			throw new TemplateModelException("arg0 is missing!");
		}
		Integer length;
		if (args.size() > 1) {
			TemplateModel arg1 = (TemplateModel) args.get(1);
			length = Freemarkers.getInteger(arg1, "arg1");
		} else {
			throw new TemplateModelException("arg1 is missing!");
		}
		String append = null;
		if (args.size() > 2) {
			TemplateModel arg2 = (TemplateModel) args.get(2);
			append = Freemarkers.getString(arg2, "arg2");
		}
		String result = Strings.substring(text, length, append);
		return result;
	}

}

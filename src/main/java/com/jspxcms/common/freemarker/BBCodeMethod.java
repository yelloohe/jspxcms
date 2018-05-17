package com.jspxcms.common.freemarker;

import java.util.List;

import com.jspxcms.common.web.BBCode;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * bbcode转换成html
 * 
 * @author liufang
 * 
 */
public class BBCodeMethod implements TemplateMethodModelEx {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		String text;
		if (args.size() > 0) {
			TemplateModel arg0 = (TemplateModel) args.get(0);
			text = Freemarkers.getString(arg0, "arg0");
		} else {
			throw new TemplateModelException("arg0 is missing!");
		}
		String result = BBCode.bbcode(text);
		return result;
	}

}

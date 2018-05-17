package com.jspxcms.core.web.method;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.service.InfoQueryService;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * GetInfoMethod
 * 
 * @author liufang
 * 
 */
public class GetInfoMethod implements TemplateMethodModelEx {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		if (args.size() < 1) {
			throw new TemplateModelException("Wrong arguments");
		}
		TemplateModel arg0 = (TemplateModel) args.get(0);
		Integer id = Freemarkers.getIntegerRequired(arg0, "arg0");
		Info info = query.get(id);
		return info;
	}

	@Autowired
	private InfoQueryService query;
}

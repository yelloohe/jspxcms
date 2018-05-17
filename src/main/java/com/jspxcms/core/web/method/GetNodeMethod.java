package com.jspxcms.core.web.method;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.support.ForeContext;

import freemarker.core.Environment;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * GetNodeMethod
 * 
 * @author liufang
 * 
 */
public class GetNodeMethod implements TemplateMethodModelEx {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		if (args.size() > 1) {
			throw new TemplateModelException("Wrong arguments");
		}
		if (args.size() < 1) {
			Environment env = Environment.getCurrentEnvironment();
			Integer siteId = ForeContext.getSiteId(env);
			return query.findRoot(siteId);
		}
		TemplateModel model = (TemplateModel) args.get(0);
		if (model instanceof TemplateNumberModel) {
			Integer id = ((TemplateNumberModel) model).getAsNumber().intValue();
			return query.get(id);
		} else if (model instanceof TemplateScalarModel) {
			String number = ((TemplateScalarModel) model).getAsString();
			Environment env = Environment.getCurrentEnvironment();
			Integer siteId = ForeContext.getSiteId(env);
			return query.findByNumber(siteId, number);
		} else {
			throw new TemplateModelException(
					"The arg0 must be a number or a string.");
		}
	}

	@Autowired
	private NodeQueryService query;
}

package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.service.SQLService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * SQLUpdateDirective
 * 
 * @author liufang
 * 
 */
public class SQLUpdateDirective implements TemplateDirectiveModel {
	public static final String SQL = "sql";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		String sql = Freemarkers.getString(params, SQL);
		int result = service.update(sql);
		loopVars[0] = env.getObjectWrapper().wrap(result);
		if (body != null) {
			body.render(env.getOut());
		}
	}

	@Autowired
	private SQLService service;
}

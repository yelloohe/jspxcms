package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.SiteService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 站点标签
 * 
 * @author liufang
 * 
 */
public class SiteDirective implements TemplateDirectiveModel {

	public static final String ID = "id";
	public static final String NUMBER = "number";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		if (body == null) {
			throw new RuntimeException("missing body");
		}
		Integer id = Freemarkers.getInteger(params, ID);
		String number = Freemarkers.getString(params, NUMBER);
		Site site = null;
		if (id != null) {
			site = service.get(id);
		} else if (StringUtils.isNotBlank(number)) {
			site = service.findByNumber(number);
		} else {
			throw new TemplateModelException("The required \"" + ID + "\" or \"" + NUMBER + "\" paramter is missing.");
		}
		loopVars[0] = env.getObjectWrapper().wrap(site);
		body.render(env.getOut());
	}

	@Autowired
	private SiteService service;
}

package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.domain.ScoreGroup;
import com.jspxcms.core.service.ScoreGroupService;
import com.jspxcms.core.support.ForeContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * SpecialCategoryListDirective
 * 
 * @author liufang
 * 
 */
public class ScoreGroupDirective implements TemplateDirectiveModel {
	public static final String SITE_ID = "siteId";
	public static final String ID = "id";
	public static final String NUMBER = "number";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		if (body == null) {
			throw new RuntimeException("missing body");
		}

		Integer siteId = Freemarkers.getInteger(params, SITE_ID);
		if (siteId == null) {
			siteId = ForeContext.getSiteId(env);
		}
		Integer id = Freemarkers.getInteger(params, ID);
		String number = Freemarkers.getString(params, NUMBER);
		ScoreGroup bean;
		if (id != null) {
			bean = service.get(id);
		} else if (StringUtils.isNotBlank(number)) {
			bean = service.findByNumber(siteId, number);
		} else {
			bean = service.findTopOne(siteId);
		}

		loopVars[0] = env.getObjectWrapper().wrap(bean);
		body.render(env.getOut());
	}

	@Autowired
	private ScoreGroupService service;
}

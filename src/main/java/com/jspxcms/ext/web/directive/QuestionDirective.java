package com.jspxcms.ext.web.directive;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.service.QuestionService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * QuestionDirective
 * 
 * @author liufang
 * 
 */
public class QuestionDirective implements TemplateDirectiveModel {
	public static final String SITE_ID = "siteId";
	public static final String ID = "id";

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		Question question = null;
		if (id != null) {
			question = service.get(id);
		} else {
			question = service.findLatest(
					new Integer[] { Question.NOMAL_STATUS }, siteId);
		}
		loopVars[0] = env.getObjectWrapper().wrap(question);
		body.render(env.getOut());
	}

	@Autowired
	private QuestionService service;
}

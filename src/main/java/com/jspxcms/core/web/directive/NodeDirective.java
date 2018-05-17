package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.support.ForeContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * NodeDirective
 * 
 * @author liufang
 * 
 */
public class NodeDirective implements TemplateDirectiveModel {

	public static final String ID = "id";
	public static final String NUMBER = "number";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		if (body == null) {
			throw new RuntimeException("missing body");
		}
		Integer id = Freemarkers.getInteger(params, ID);
		String number = Freemarkers.getString(params, NUMBER);
		Node node = null;
		if (id != null) {
			node = query.get(id);
		} else if (StringUtils.isNotBlank(number)) {
			Integer siteId = ForeContext.getSiteId(env);
			node = query.findByNumber(siteId, number);
		} else {
			throw new TemplateModelException("The required \"" + ID
					+ "\" or \"" + NUMBER + "\" paramter is missing.");
		}
		loopVars[0] = env.getObjectWrapper().wrap(node);
		body.render(env.getOut());
	}

	@Autowired
	private NodeQueryService query;
}

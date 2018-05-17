package com.jspxcms.ext.web.directive;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.ext.domain.Vote;
import com.jspxcms.ext.service.VoteService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/**
 * VoteDirective
 * 
 * @author liufang
 * 
 */
public class VoteDirective implements TemplateDirectiveModel {
	public static final String SITE_ID = "siteId";
	public static final String ID = "id";
	public static final String NUMBER = "number";
	public static final String STATUS = "status";

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
		String number = Freemarkers.getString(params, NUMBER);
		Integer[] status = Freemarkers.getIntegers(params, STATUS);
		if (status == null) {
			status = new Integer[] { Vote.NORMAL_STATUS};
		}

		Vote vote;
		if (id != null) {
			vote = service.get(id);
		} else if (StringUtils.isNotBlank(number)) {
			vote = service.findByNumber(number, status, siteId);
		} else {
			vote = service.findLatest(status, siteId);
		}
		loopVars[0] = env.getObjectWrapper().wrap(vote);
		body.render(env.getOut());
	}

	@Autowired
	private VoteService service;
}

package com.jspxcms.ext.web.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.ext.domain.GuestbookType;
import com.jspxcms.ext.service.GuestbookTypeService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class GuestbookTypeListDirective implements TemplateDirectiveModel {
	public static final String SITE_ID = "siteId";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
		TemplateDirectiveBody body) throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		if (body == null) {
			throw new RuntimeException("missing body");
		}
		Integer[] siteId = Freemarkers.getIntegers(params, SITE_ID);
		if (siteId == null && params.get(SITE_ID) == null) {
			siteId = new Integer[] { ForeContext.getSiteId(env)};
		}
		Sort defSort = new Sort(Direction.ASC, "seq", "id");
		Limitable limitable = Freemarkers.getLimitable(params, defSort);
		List<GuestbookType> list = service.findList(siteId, limitable);
		loopVars[0] = env.getObjectWrapper().wrap(list);
		body.render(env.getOut());
	}
	@Autowired
	private GuestbookTypeService service;
}

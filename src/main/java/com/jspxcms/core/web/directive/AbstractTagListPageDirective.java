package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Tag;
import com.jspxcms.core.service.TagService;
import com.jspxcms.core.support.ForeContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * AbstractTagListPageDirective
 * 
 * @author liufang
 * 
 */
public abstract class AbstractTagListPageDirective {
	public static final String SITE_ID = "siteId";
	public static final String NODE = "node";
	public static final String NODE_ID = "nodeId";
	public static final String REFERS = "refers";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doExecute(Environment env, Map params,
			TemplateModel[] loopVars, TemplateDirectiveBody body, boolean isPage)
			throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		if (body == null) {
			throw new RuntimeException("missing body");
		}

		Integer[] siteId = Freemarkers.getIntegers(params, SITE_ID);
		if (siteId == null && params.get(SITE_ID) == null) {
			siteId = new Integer[] { ForeContext.getSiteId(env) };
		}
		Integer[] nodeId = Freemarkers.getIntegers(params, NODE_ID);
		String[] node = Freemarkers.getStrings(params, NODE);
		Integer refers = Freemarkers.getInteger(params, REFERS, 1);
		Sort defSort = new Sort(Direction.DESC, "refers", "id");
		if (isPage) {
			Pageable pageable = Freemarkers.getPageable(params, env, defSort);
			Page<Tag> pagedList = service.findPage(siteId, node, nodeId,
					refers, pageable);
			ForeContext.setTotalPages(pagedList.getTotalPages());
			loopVars[0] = env.getObjectWrapper().wrap(pagedList);
		} else {
			Limitable limitable = Freemarkers.getLimitable(params, defSort);
			List<Tag> list = service.findList(siteId, node, nodeId, refers,
					limitable);
			loopVars[0] = env.getObjectWrapper().wrap(list);
		}
		body.render(env.getOut());
	}

	@Autowired
	private TagService service;
}

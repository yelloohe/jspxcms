package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.SiteService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 站点列表、分页标签父类
 * 
 * @author liufang
 * 
 */
public class SiteListDirective implements TemplateDirectiveModel {
	/**
	 * 父节点ID。整型。
	 */
	public static final String PARENT_ID = "parentId";
	/**
	 * 父节点编码。字符串。
	 */
	public static final String PARENT = "parent";
	/**
	 * 状态。int。
	 */
	public static final String STATUS = "status";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		if (loopVars.length < 1) {
			throw new TemplateModelException("Loop variable is required.");
		}
		if (body == null) {
			throw new RuntimeException("missing body");
		}
		Integer parentId = Freemarkers.getInteger(params, PARENT_ID);
		String parent = Freemarkers.getString(params, PARENT);

		Integer[] status = Freemarkers.getIntegers(params, STATUS);
		if (status == null) {
			status = new Integer[] { Site.NORMAL };
		}
		Sort defSort = new Sort("treeNumber");
		Limitable limitable = Freemarkers.getLimitable(params, defSort);
		List<Site> list = service.findList(parentId, parent, status, limitable);
		loopVars[0] = env.getObjectWrapper().wrap(list);
		body.render(env.getOut());
	}

	@Autowired
	private SiteService service;
}

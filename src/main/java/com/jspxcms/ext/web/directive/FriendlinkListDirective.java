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
import com.jspxcms.ext.domain.Friendlink;
import com.jspxcms.ext.service.FriendlinkService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class FriendlinkListDirective implements TemplateDirectiveModel {
	/**
	 * 站点ID。整型。
	 */
	public static final String SITE_ID = "siteId";
	/**
	 * 所属类型编码。字符串。
	 */
	public static final String TYPE = "type";
	/**
	 * 所属类型ID。整型。
	 */
	public static final String TYPE_ID = "typeId";
	/**
	 * 是否带LOGO。布尔型。
	 */
	public static final String IS_WITH_LOGO = "isWithLogo";
	/**
	 * 是否为推荐。布尔型。
	 */
	public static final String IS_RECOMMEND = "isRecommend";
	/**
	 * 状态。布尔型。
	 */
	public static final String STATUS = "status";
	
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
		Integer[] typeId = Freemarkers.getIntegers(params, TYPE_ID);
		String[] type = Freemarkers.getStrings(params, TYPE);
		Boolean isWithLogo = Freemarkers.getBoolean(params, IS_WITH_LOGO);
		Boolean isRecommend = Freemarkers.getBoolean(params, IS_RECOMMEND);
		Integer[] status = Freemarkers.getIntegers(params, STATUS);
		if (status == null) {
			status = new Integer[] {Friendlink.AUDITED};
		}

		Sort defSort = new Sort(Direction.ASC, "seq", "id");
		
		Limitable limitable = Freemarkers.getLimitable(params, defSort);
		List<Friendlink> list = service.findList(siteId, type,
				typeId, isWithLogo, isRecommend, status, limitable);
		loopVars[0] = env.getObjectWrapper().wrap(list);
		body.render(env.getOut());
	}
	@Autowired
	private FriendlinkService service;
}

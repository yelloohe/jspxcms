package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Special;
import com.jspxcms.core.service.SpecialService;
import com.jspxcms.core.support.ForeContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * AbstractSpecialListPageDirective
 * 
 * @author liufang
 * 
 */
public abstract class AbstractSpecialListPageDirective {
	public static final String SITE_ID = "siteId";
	public static final String CATEGORY_ID = "categoryId";
	public static final String BEGIN_DATE = "beginDate";
	public static final String END_DATE = "endDate";
	public static final String IS_WITH_IMAGE = "isWithImage";
	public static final String IS_RECOMMEND = "isRecommend";

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
		Integer[] categoryId = Freemarkers.getIntegers(params, CATEGORY_ID);
		Date beginDate = Freemarkers.getDate(params, BEGIN_DATE);
		Date endDate = Freemarkers.getEndDate(params, END_DATE);
		Boolean isWithImage = Freemarkers.getBoolean(params, IS_WITH_IMAGE);
		Boolean isRecommend = Freemarkers.getBoolean(params, IS_RECOMMEND);

		Sort defSort = new Sort(Direction.DESC, "creationDate", "id");
		if (isPage) {
			Pageable pageable = Freemarkers.getPageable(params, env, defSort);
			Page<Special> pagedList = service.findPage(siteId, categoryId,
					beginDate, endDate, isWithImage, isRecommend, pageable);
			ForeContext.setTotalPages(pagedList.getTotalPages());
			loopVars[0] = env.getObjectWrapper().wrap(pagedList);
		} else {
			Limitable limitable = Freemarkers.getLimitable(params, defSort);
			List<Special> list = service.findList(siteId, categoryId,
					beginDate, endDate, isWithImage, isRecommend, limitable);
			loopVars[0] = env.getObjectWrapper().wrap(list);
		}

		body.render(env.getOut());
	}

	@Autowired
	private SpecialService service;
}

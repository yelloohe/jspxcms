package com.jspxcms.core.web.directive;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.freemarker.Freemarkers;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.fulltext.InfoFulltextService;
import com.jspxcms.core.support.ForeContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * AbstractInfoFulltextListPageDirective
 * 
 * @author liufang
 * 
 */
public abstract class AbstractInfoFulltextListPageDirective {
	public static final String SITE_ID = "siteId";

	public static final String NODE_ID = "nodeId";
	// public static final String NODE = "node";
	// public static final String NODE_NUMBER = "nodeNumber";

	// public static final String ATTR_ID = "attrId";
	// public static final String ATTR = "attr";

	// public static final String SPECIAL_ID = "specialId";
	// public static final String SPECIAL_TITLE = "specialTitle";

	// public static final String TAG = "tag";
	// public static final String TAG_ID = "tagId";
	// public static final String TAG_NAME = "tagName";

	// public static final String USER = "user";
	// public static final String USER_ID = "userId";

	// public static final String PRIORITY = "priority";
	public static final String BEGIN_DATE = "beginDate";
	public static final String END_DATE = "endDate";
	public static final String Q = "q";
	public static final String TITLE = "title";
	public static final String KEYWORD = "keyword";
	public static final String DESCRIPTION = "description";
	public static final String TEXT = "text";
	public static final String CREATOR = "creator";
	public static final String AUTHOR = "author";
	// public static final String INCLUDE_ID = "includeId";
	public static final String EXCLUDE_ID = "excludeId";
	public static final String STATUS = "status";
	public static final String FRAGMENT_SIZE = "fragmentSize";

	// public static final String IS_INCLUDE_CHILDREN = "isIncludeChildren";
	// public static final String IS_MAIN_NODE_ONLY = "isMainNodeOnly";
	// public static final String IS_WITH_IMAGE = "isWithImage";

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
		if (siteId == null) {
			siteId = new Integer[] { ForeContext.getSiteId(env) };
		}

		Integer[] nodeId = Freemarkers.getIntegers(params, NODE_ID);
		Date beginDate = Freemarkers.getDate(params, BEGIN_DATE);
		Date endDate = Freemarkers.getEndDate(params, END_DATE);
		String q = Freemarkers.getString(params, Q);
		String title = Freemarkers.getString(params, TITLE);
		String[] keyword = Freemarkers.getStrings(params, KEYWORD);
		String description = Freemarkers.getString(params, DESCRIPTION);
		String text = Freemarkers.getString(params, TEXT);
		String[] creator = Freemarkers.getStrings(params, CREATOR);
		String[] author = Freemarkers.getStrings(params, AUTHOR);

		Integer[] excludeId = Freemarkers.getIntegers(params, EXCLUDE_ID);
		String[] status = Freemarkers.getStrings(params, STATUS);
		// 空串取所有状态
		if (status == null) {
			// null则取正常状态
			status = new String[] { Info.NORMAL };
		}
		Integer fragmentSize = Freemarkers.getInteger(params, FRAGMENT_SIZE);
		Sort sort = Freemarkers.getFulltextSort(params);
		if (isPage) {
			Pageable pageable = Freemarkers.getFulltextPageable(params, env);
			Page<Info> pagedList = fulltext.page(siteId, nodeId, null,
					beginDate, endDate, status, excludeId, q, title, keyword,
					description, text, creator, author, fragmentSize, pageable,
					sort);
			ForeContext.setTotalPages(pagedList.getTotalPages());
			loopVars[0] = env.getObjectWrapper().wrap(pagedList);
		} else {
			Limitable limitable = Freemarkers.getFulltextLimitable(params);
			List<Info> list = fulltext.list(siteId, nodeId, null, beginDate,
					endDate, status, excludeId, q, title, keyword, description,
					text, creator, author, fragmentSize, limitable, sort);
			loopVars[0] = env.getObjectWrapper().wrap(list);
		}

		body.render(env.getOut());
	}

	@Autowired
	private InfoFulltextService fulltext;
}

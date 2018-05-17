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
import com.jspxcms.core.domain.Comment;
import com.jspxcms.core.service.CommentService;
import com.jspxcms.core.support.ForeContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * AbstractCommentListPageDirective
 * 
 * @author liufang
 * 
 */
public abstract class AbstractCommentListPageDirective {
	public static final String FTYPE = "ftype";
	public static final String FID = "fid";

	public static final String SITE_ID = "siteId";
	// public static final String CREATOR = "creator";
	public static final String CREATOR_ID = "creatorId";
	// public static final String BEGIN_DATE = "beginDate";
	// public static final String END_DATE = "endDate";
	public static final String STATUS = "status";

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
		String ftype = Freemarkers.getString(params, FTYPE, "Info");
		Integer fid = Freemarkers.getInteger(params, FID);
		Integer creatorId = Freemarkers.getInteger(params, CREATOR_ID);

		Integer[] status = Freemarkers.getIntegers(params, STATUS);
		if (status == null) {
			status = new Integer[] { Comment.AUDITED, Comment.RECOMMEND };
		}

		Sort defSort = new Sort(Direction.DESC, "id");
		if (isPage) {
			Pageable pageable = Freemarkers.getPageable(params, env, defSort);
			Page<Comment> pagedList = service.findPage(ftype, fid, creatorId,
					status, siteId, pageable);
			ForeContext.setTotalPages(pagedList.getTotalPages());
			loopVars[0] = env.getObjectWrapper().wrap(pagedList);
		} else {
			Limitable limitable = Freemarkers.getLimitable(params, defSort);
			List<Comment> list = service.findList(ftype, fid, creatorId,
					status, siteId, limitable);
			loopVars[0] = env.getObjectWrapper().wrap(list);
		}

		body.render(env.getOut());
	}

	@Autowired
	private CommentService service;
}

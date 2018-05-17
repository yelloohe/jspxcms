package com.jspxcms.ext.web.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;

/**
 * 投票分页对象
 * 
 * @author liufang
 * 
 */
public class VotePageDirective extends AbstractVoteListPageDirective {
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		super.doExecute(env, params, loopVars, body, true);
	}
}

package com.jspxcms.common.freemarker;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 增加参数标签
 * 
 * <p>
 * 增加或替换参数，可以有多个参数，参数值可以是数组。同时去除分页参数。
 * <p>
 * 使用页面当前地址。
 * <p>
 * 可以传入新地址，判断当前地址是否为静态地址(.html,.htm)，是静态地址且传入新地址，则使用新地址。
 * <p>
 * 可以去除参数。
 * 
 * @author liufang
 * 
 */
public class AddParamDirective implements TemplateDirectiveModel {
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		// TODO Auto-generated method stub

	}
}

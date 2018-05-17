package com.jspxcms.common.freemarker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 增加参数方法
 * 
 * <p>
 * 增加或替换参数，可以有多个参数，参数值可以是数组。同时去除分页参数。
 * addParam('http://www.abc.com/news.jspx','abc',123);addParam('abc',123,456);
 * <p>
 * 使用页面当前地址。
 * 
 * @author liufang
 * 
 */
public class AddParamMethod implements TemplateMethodModelEx {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		int argsSize = args.size();
		if (argsSize < 2) {
			throw new TemplateModelException("Wrong arguments");
		}
		// 获取地址
		String dyUrl = Freemarkers.getString((TemplateModel) args.get(0),
				"arg0");
		// 获取参数名称
		String name = Freemarkers
				.getString((TemplateModel) args.get(1), "arg1");
		// 获取参数值，可以有多个。
		List<String> values = new ArrayList<String>(argsSize - 1);
		for (int i = 2; i < argsSize; i++) {
			String value = Freemarkers.getString((TemplateModel) args.get(i),
					"arg" + i);
			if (value != null) {
				values.add(value);
			}
		}

		Environment env = Environment.getCurrentEnvironment();
		String url = Freemarkers.getUrl(env);
		if (StringUtils.isBlank(url)) {
			url = "";
		}
		// 如果是静态页，则用dyUrl。
		if (StringUtils.isNotBlank(dyUrl)
				&& (url.endsWith(".html") || url.endsWith(".htm"))) {
			url = dyUrl;
		}

		String queryString = "";
		String uri = url;
		int pos = url.indexOf("?");
		if (pos != -1) {
			queryString = url.substring(pos + 1);
			uri = url.substring(0, pos);
		}

		// 去除uri中的分页/25_2.jspx
		Pattern p = Pattern.compile("/[^_/\\.\\?\\;]+(_\\d)\\.jspx");
		Matcher m = p.matcher(queryString);
		if (m.find()) {
			queryString = queryString.substring(0, m.start(1))
					+ queryString.substring(m.end(1));
		}

		// 去除参数分页
		if (StringUtils.isNotBlank(queryString)) {
			// 删除原有page。page=3&page=4&page=10&page=0
			p = Pattern.compile("\\&*\\s*" + Freemarkers.PAGE + "\\s*=[^\\&]*");
			m = p.matcher(queryString);
			queryString = m.replaceAll("");
			queryString = queryString.trim();
			if (queryString.startsWith("&")) {
				queryString = queryString.substring(1);
			}
		}

		// 去除参数
		if (StringUtils.isNotBlank(queryString)) {
			// 删除原有page。page=3&page=4&page=10&page=0
			p = Pattern.compile("\\&*\\s*" + name + "\\s*=[^\\&]*");
			m = p.matcher(queryString);
			queryString = m.replaceAll("");
			queryString = queryString.trim();
			if (queryString.startsWith("&")) {
				queryString = queryString.substring(1);
			}
		}
		// 增加参数
		for (String value : values) {
			if (StringUtils.isNotBlank(queryString)) {
				queryString += "&";
			}
			queryString += name + "=" + value;
		}
		if (StringUtils.isBlank(queryString)) {
			return uri;
		} else {
			return uri + "?" + queryString;
		}
	}
}

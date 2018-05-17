package com.jspxcms.common.freemarker;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.jspxcms.common.web.PageUrlResolver;

import freemarker.core.Environment;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * FreeMarker翻页方法
 * 
 * @author liufang
 * 
 */
public class PagingMethod implements TemplateMethodModelEx {
	// page,location,pageParam,pageMethod
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		Integer page = null;
		if (args.size() > 0) {
			TemplateModel arg0 = (TemplateModel) args.get(0);
			page = Freemarkers.getInteger(arg0, "arg0");
		}
		if (page == null || page < 1) {
			page = 1;
		}
		String anchor = null;
		if (args.size() > 1) {
			TemplateModel arg1 = (TemplateModel) args.get(1);
			anchor = Freemarkers.getString(arg1, "arg1");
		}
		Environment env = Environment.getCurrentEnvironment();

		String url = Freemarkers.getUrl(env);
		if (StringUtils.isBlank(url)) {
			url = "";
		}
		String param = Freemarkers.PAGE;

		String queryString = null;
		String uri = url;
		int pos = url.indexOf("?");
		if (pos != -1) {
			queryString = url.substring(pos + 1);
			uri = url.substring(0, pos);
		}

		PageUrlResolver urlResolver = Freemarkers.getPageUrlResolver(env);
		if (urlResolver != null) {
			String pageUrl = urlResolver.getPageUrl(page);
			// 增加地址中的param参数
			if (StringUtils.isNotBlank(queryString)) {
				pageUrl += "?" + queryString;
			}
			if (StringUtils.isNotBlank(anchor)) {
				pageUrl += anchor;
			}
			return pageUrl;
		}

		if (StringUtils.isNotBlank(queryString)) {
			// 删除原有page。page=3&page=4&page=10&page=0
			Pattern p = Pattern.compile("\\&*\\s*" + param + "\\s*=[^\\&]*");
			Matcher m = p.matcher(queryString);
			queryString = m.replaceAll("");
			queryString = queryString.trim();
			if (queryString.startsWith("&")) {
				queryString = queryString.substring(1);
			}
		}
		if (page > 1) {
			if (StringUtils.isNotBlank(queryString)) {
				queryString += "&" + param + "=" + page;
			} else {
				queryString = param + "=" + page;
			}
		}
		String result;
		if (StringUtils.isNotBlank(queryString)) {
			result = uri + "?" + queryString;
		} else {
			result = uri;
		}
		if (StringUtils.isNotBlank(anchor)) {
			result += anchor;
		}
		return result;
	}
}

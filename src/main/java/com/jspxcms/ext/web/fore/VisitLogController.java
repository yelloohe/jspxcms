package com.jspxcms.ext.web.fore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.SiteResolver;
import com.jspxcms.ext.service.VisitLogService;

/**
 * VisitLogController 访问日志控制器
 * 
 * @author liufang
 * 
 */
@Controller
public class VisitLogController {

	@RequestMapping("/visit_log")
	public void visitLog(HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		visitLog(null, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/visit_log")
	public void visitLog(@PathVariable String siteNumber,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		User user = Context.getCurrentUser();
		Site site = Context.getCurrentSite();
		String url = Servlets.getParam(request, "url");
		String referrer = Servlets.getParam(request, "referrer");
		String userAgent = request.getHeader("user-agent");
		// 不超过最大长度
		url = StringUtils.substring(url, 0, 255);
		referrer = StringUtils.substring(referrer, 0, 255);
		userAgent = StringUtils.substring(userAgent, 0, 450);
		String ip = Servlets.getRemoteAddr(request);
		String cookie = Site.getIdentityCookie(request, response);
		service.save(url, referrer, ip, cookie, userAgent, user, site);
	}

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private VisitLogService service;
}

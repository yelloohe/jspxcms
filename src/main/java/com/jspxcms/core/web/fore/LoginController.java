package com.jspxcms.core.web.fore;

import static com.jspxcms.core.security.CmsAuthenticationFilter.FALLBACK_URL_PARAM;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_USERNAME_PARAM;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;

/**
 * LoginController
 * 
 * @author liufang
 * 
 */
@Controller
public class LoginController {
	public static final String LOGIN_URL = "/login";
	public static final String LOGIN_TEMPLATE = "sys_member_login.html";
	public static final String LOGIN_INCLUDE_TEMPLATE = "sys_member_login_include.html";
	public static final String LOGIN_AJAX_TEMPLATE = "sys_member_login_ajax.html";

	@RequestMapping(value = { LOGIN_URL,
			Constants.SITE_PREFIX_PATH + "" + LOGIN_URL })
	public String login(String fallbackUrl, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		modelMap.addAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		return site.getTemplate(LOGIN_TEMPLATE);
	}

	@RequestMapping(value = { "/login_include",
			Constants.SITE_PREFIX_PATH + "/login_include" })
	public String loginInclude(String fallbackUrl, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		Servlets.setNoCacheHeader(response);
		modelMap.addAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		return site.getTemplate(LOGIN_INCLUDE_TEMPLATE);
	}

	@RequestMapping(value = { "/login_ajax",
			Constants.SITE_PREFIX_PATH + "/login_ajax" })
	public String loginAjax(String fallbackUrl, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		Servlets.setNoCacheHeader(response);
		modelMap.addAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		return site.getTemplate(LOGIN_AJAX_TEMPLATE);
	}

	@RequestMapping(value = { "/login",
			Constants.SITE_PREFIX_PATH + "/login" }, method = RequestMethod.POST)
	public String loginFail(
			@RequestParam(DEFAULT_USERNAME_PARAM) String username,
			String fallbackUrl, HttpServletRequest request,
			RedirectAttributes ra) {
		Object errorName = request
				.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (errorName != null) {
			ra.addFlashAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, errorName);
		}
		ra.addFlashAttribute(DEFAULT_USERNAME_PARAM, username);
		ra.addAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		return "redirect:login";
	}
}

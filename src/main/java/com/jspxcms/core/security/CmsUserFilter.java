package com.jspxcms.core.security;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import com.jspxcms.core.constant.Constants;

public class CmsUserFilter extends UserFilter {
	private String backUrl = Constants.CMSCP + "/";
	private String backLoginUrl = Constants.BACK_LOGIN_URL;

	@Override
	protected void redirectToLogin(ServletRequest req, ServletResponse resp)
			throws IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String loginUrl;
		if (request.getRequestURI().startsWith(
				request.getContextPath() + getBackUrl())) {
			loginUrl = getBackLoginUrl();
		} else {
			loginUrl = getLoginUrl();
		}
		WebUtils.issueRedirect(request, response, loginUrl);
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getBackLoginUrl() {
		return backLoginUrl;
	}

	public void setBackLoginUrl(String backLoginUrl) {
		this.backLoginUrl = backLoginUrl;
	}

}

package com.jspxcms.core.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.beans.factory.BeanFactory;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.service.OperationLogService;

public class CmsLogoutFilter extends LogoutFilter {
	/**
	 * 返回URL
	 */
	public static final String FALLBACK_URL_PARAM = "fallbackUrl";
	/**
	 * 后台路径
	 */
	private String backUrl = Constants.CMSCP + "/";
	private String backRedirectUrl = Constants.BACK_SUCCESS_URL;

	private BeanFactory beanFactory;

	public CmsLogoutFilter(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		Object principal = subject.getPrincipal();
		String ip = Servlets.getRemoteAddr(request);
		boolean result = super.preHandle(request, response);

		if (principal != null) {
			getOperationLogService().logout(ip, principal.toString());
		}
		return result;
	}

	protected String getRedirectUrl(ServletRequest req, ServletResponse resp, Subject subject) {
		HttpServletRequest request = (HttpServletRequest) req;
		String redirectUrl = request.getParameter(FALLBACK_URL_PARAM);
		if (StringUtils.isBlank(redirectUrl)) {
			if (request.getRequestURI().startsWith(request.getContextPath() + backUrl)) {
				redirectUrl = getBackRedirectUrl();
			} else {
				redirectUrl = getRedirectUrl();
			}
		}
		return redirectUrl;
	}

	public String getBackRedirectUrl() {
		return backRedirectUrl;
	}

	public void setBackRedirectUrl(String backRedirectUrl) {
		this.backRedirectUrl = backRedirectUrl;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	private OperationLogService operationLogService;

	public OperationLogService getOperationLogService() {
		if (operationLogService == null) {
			operationLogService = beanFactory.getBean(OperationLogService.class);
		}
		return operationLogService;
	}

	public void setOperationLogService(OperationLogService operationLogService) {
		this.operationLogService = operationLogService;
	}
}

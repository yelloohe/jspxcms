package com.jspxcms.core.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.jspxcms.common.captcha.Captchas;
import com.jspxcms.common.security.IncorrectCaptchaException;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Global;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.GlobalShiroService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.UserShiroService;
import com.octo.captcha.service.CaptchaService;

/**
 * CmsAuthenticationFilter
 * 
 * @author liufang
 * 
 */
public class CmsAuthenticationFilter extends FormAuthenticationFilter {
	private Logger logger = LoggerFactory.getLogger(CmsAuthenticationFilter.class);

	private BeanFactory beanFactory;

	public CmsAuthenticationFilter(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * 是否需要验证码
	 */
	public static final String CAPTCHA_REQUIRED_KEY = "shiroCaptchaRequired";
	/**
	 * 验证码错误次数
	 */
	public static final String CAPTCHA_ERROR_COUNT_KEY = "shiroCaptchaErrorCount";
	/**
	 * 验证码名称
	 */
	public static final String CAPTCHA_PARAM = "captcha";
	/**
	 * 返回URL
	 */
	public static final String FALLBACK_URL_PARAM = "fallbackUrl";
	/**
	 * 后台路径
	 */
	private String backUrl = Constants.CMSCP + "/";
	private String backSuccessUrl = Constants.BACK_SUCCESS_URL;

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		AuthenticationToken token = createToken(request, response);
		if (token == null) {
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
					+ "must be created in order to execute a login attempt.";
			throw new IllegalStateException(msg);
		}
		String username = (String) token.getPrincipal();
		User user = getUserShiroService().findByUsername(username);
		HttpServletRequest hsr = (HttpServletRequest) request;
		HttpServletResponse hsp = (HttpServletResponse) response;
		Global global = getGlobalShiroService().findUnique();
		int captchaErrors = global.getCaptchaErrors();
		if (isCaptchaSessionRequired(hsr, hsp) || captchaErrors <= 0
				|| (user != null && user.isCaptchaRequired(captchaErrors))) {
			String captcha = request.getParameter(CAPTCHA_PARAM);
			if (captcha == null || !Captchas.isValid(getCaptchaService(), hsr, captcha)) {
				return onLoginFailure(token, new IncorrectCaptchaException(), request, response);
			}
		}
		String ip = Servlets.getRemoteAddr(request);
		// 登录时，session会失效，先将错误次数取出
		Integer errorCount = (Integer) hsr.getSession().getAttribute(CAPTCHA_ERROR_COUNT_KEY);
		// 登录时，session会失效，先将SavedRequest取出
		SavedRequest savedRequest = (SavedRequest) hsr.getSession().getAttribute(WebUtils.SAVED_REQUEST_KEY);
		try {
			Subject subject = getSubject(request, response);
			// 防止session fixation attack(会话固定攻击)，让旧session失效
			if (subject.getSession(false) != null) {
				subject.logout();
			}
			subject.login(token);
			// 将SavedRequest放回session
			hsr.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);
			getOperationLogService().loginSuccess(ip, user.getId());
			return onLoginSuccess(token, subject, request, response);
		} catch (AuthenticationException e) {
			Object cred = token.getCredentials();
			String password = "";
			if (cred instanceof char[]) {
				password = new String((char[]) cred);
			}
			// 将错误次数放回session
			hsr.getSession().setAttribute(CAPTCHA_ERROR_COUNT_KEY, errorCount);
			// 将SavedRequest放回session
			hsr.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);
			getOperationLogService().loginFailure(username + ":" + password, ip);
			return onLoginFailure(token, e, request, response);
		}
	}

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		boolean isAllowed = isAccessAllowed(request, response, mappedValue);
		if (isAllowed && isLoginRequest(request, response)) {
			try {
				issueSuccessRedirect(request, response);
			} catch (Exception e) {
				logger.error("", e);
			}
			return false;
		}
		return isAllowed || onAccessDenied(request, response, mappedValue);
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
		String ip = Servlets.getRemoteAddr(request);
		getUserShiroService().updateLoginSuccess(shiroUser.id, ip);
		HttpServletRequest hsr = (HttpServletRequest) request;
		HttpServletResponse hsp = (HttpServletResponse) response;
		// 清除需要验证码的session
		removeCaptchaSession(hsr, hsp);
		return super.onLoginSuccess(token, subject, request, response);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		String username = (String) token.getPrincipal();
		HttpServletRequest hsr = (HttpServletRequest) request;
		HttpServletResponse hsp = (HttpServletResponse) response;
		HttpSession session = hsr.getSession();
		Integer errorCount = (Integer) session.getAttribute(CAPTCHA_ERROR_COUNT_KEY);
		if (errorCount != null) {
			errorCount++;
		} else {
			errorCount = 1;
		}
		session.setAttribute(CAPTCHA_ERROR_COUNT_KEY, errorCount);
		User user = getUserShiroService().updateLoginFailure(username);
		Global global = getGlobalShiroService().findUnique();
		int captchaErrors = global.getCaptchaErrors();
		if (errorCount >= captchaErrors || (user != null && user.isCaptchaRequired(captchaErrors))) {
			// 加入需要验证码的session
			addCaptchaSession(hsr, hsp);
		}
		return super.onLoginFailure(token, e, request, response);
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest req, ServletResponse resp) throws Exception {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String successUrl = request.getParameter(FALLBACK_URL_PARAM);
		if (StringUtils.isNotBlank(successUrl)) {
			WebUtils.issueRedirect(request, response, successUrl, null, false);
			return;
		}
		if (request.getRequestURI().startsWith(request.getContextPath() + backUrl)) {
			// 后台直接返回首页
			successUrl = getBackSuccessUrl();
			// 清除SavedRequest
			WebUtils.getAndClearSavedRequest(request);
			WebUtils.issueRedirect(request, response, successUrl, null, true);
		} else {
			successUrl = getSuccessUrl();
			WebUtils.redirectToSavedRequest(request, response, successUrl);
		}
	}

	@Override
	protected boolean isLoginRequest(ServletRequest req, ServletResponse resp) {
		return pathsMatch(getLoginUrl(), req) || pathsMatch(Constants.BACK_LOGIN_URL, req);
	}

	protected void removeCaptchaSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(CAPTCHA_REQUIRED_KEY);
			session.removeAttribute(CAPTCHA_ERROR_COUNT_KEY);
		}
	}

	protected void addCaptchaSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.setAttribute(CAPTCHA_REQUIRED_KEY, true);
	}

	protected boolean isCaptchaSessionRequired(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return session.getAttribute(CAPTCHA_REQUIRED_KEY) != null;
		}
		return false;
	}

	private CaptchaService captchaService;
	private UserShiroService userShiroService;
	private OperationLogService operationLogService;
	private GlobalShiroService globalShiroService;

	public CaptchaService getCaptchaService() {
		if (captchaService == null) {
			captchaService = beanFactory.getBean(CaptchaService.class);
		}
		return captchaService;
	}

	public UserShiroService getUserShiroService() {
		if (userShiroService == null) {
			userShiroService = beanFactory.getBean(UserShiroService.class);
		}
		return userShiroService;
	}

	public OperationLogService getOperationLogService() {
		if (operationLogService == null) {
			operationLogService = beanFactory.getBean(OperationLogService.class);
		}
		return operationLogService;
	}

	public GlobalShiroService getGlobalShiroService() {
		if (globalShiroService == null) {
			globalShiroService = beanFactory.getBean(GlobalShiroService.class);
		}
		return globalShiroService;
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	public void setOperationLogService(OperationLogService operationLogService) {
		this.operationLogService = operationLogService;
	}

	public void setUserShiroService(UserShiroService userShiroService) {
		this.userShiroService = userShiroService;
	}

	public void setGlobalShiroService(GlobalShiroService globalShiroService) {
		this.globalShiroService = globalShiroService;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getBackSuccessUrl() {
		return backSuccessUrl;
	}

	public void setBackSuccessUrl(String backSuccessUrl) {
		this.backSuccessUrl = backSuccessUrl;
	}

}

package com.jspxcms.core.web.fore;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject.Builder;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import weibo4j.Account;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.WeixinAccount;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.model.OauthToken;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.security.ShiroUser;
import com.jspxcms.core.security.oauth.AccessTokenNotFoundException;
import com.jspxcms.core.security.oauth.OAuthToken;
import com.jspxcms.core.security.oauth.OpenIDNotFoundException;
import com.jspxcms.core.security.oauth.SessionTokenNotFoundException;
import com.jspxcms.core.security.oauth.StateIllegalException;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.UserShiroService;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.oauth.Oauth;

/**
 * 公开认证平台登录
 * 
 * @author liufang
 * 
 */
@Controller
public class OAuthLoginController {
	private Logger logger = LoggerFactory.getLogger(OAuthLoginController.class);

	public static final String OAUTH_TOKEN_SESSION_NAME = "oauthToken";
	public static final String SUCCESS_URL = "/";
	public static final String ERROR_KEY_ATTRIBUTE_NAME = "shiroLoginFailure";
	public static final String FALLBACK_URL_PARAM = "fallbackUrl";
	public static final String SESSION_OAUTH_STATE = "oauthState";

	@Value("${oauth.weixin.authorize_url}")
	private String authorize_url;
	@Value("${oauth.weixin.appid}")
	private String appid;
	@Value("${oauth.weixin.secret}")
	private String secret;
	@Value("${oauth.weixin.redirect_uri}")
	private String redirect_uri;
	@Value("${oauth.weixin.response_type}")
	private String response_type;
	@Value("${oauth.weixin.scope}")
	private String scope;

	@RequestMapping(value = { "/oauth/login/weixin", Constants.SITE_PREFIX_PATH + "/oauth/login/weixin" })
	public void loginWeixin(String fallbackUrl, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException, WeiboException {
		String state = getState(request);
		// https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
		String url = authorize_url + "?" + "appid=" + appid + "&redirect_uri="
				+ URLEncoder.encode(redirect_uri, "UTF-8") + "&response_type=" + response_type + "&scope=" + scope
				+ "&state=" + state;
		logger.debug("WEIXIN authorize url:" + url);
		saveFallbackUrl(request);
		response.sendRedirect(url);
	}

	/**
	 * QQ登录地址。 重定向到QQ认证服务器登录地址。
	 * 
	 */
	@RequestMapping(value = { "/oauth/login/qq", Constants.SITE_PREFIX_PATH + "/oauth/login/qq" })
	public void loginQq(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException, QQConnectException {
		String url = new Oauth().getAuthorizeURL(request);
		logger.debug("QQ authorize url:" + url);
		saveFallbackUrl(request);
		response.sendRedirect(url);
	}

	/**
	 * QQ登录地址。 重定向到QQ认证服务器登录地址。
	 * 
	 * @deprecated URL地址不标准，将在以后版本删除。请使用/oauth/login/qq.jspx。
	 * @see #loginQq(HttpServletRequest, HttpServletResponse, org.springframework.ui.Model)
	 */
	@Deprecated
	@RequestMapping(value = { "/oauth/qq_login", Constants.SITE_PREFIX_PATH + "/oauth/qq_login" })
	public void qqLogin(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException, QQConnectException {
		loginQq(request, response, modelMap);
	}

	/**
	 * 微博登录地址。重定向到微博认证服务器登录地址。
	 * 
	 * @param fallbackUrl
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws IOException
	 * @throws WeiboException
	 */
	@RequestMapping(value = { "/oauth/login/weibo", Constants.SITE_PREFIX_PATH + "/oauth/login/weibo" })
	public void loginWeibo(String fallbackUrl, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException, WeiboException {
		String state = getState(request);
		String url = new weibo4j.Oauth().authorize("code", state);
		logger.debug("WEIBO authorize url:" + url);
		saveFallbackUrl(request);
		response.sendRedirect(url);
	}

	/**
	 * 微博登录地址。重定向到微博认证服务器登录地址。
	 * 
	 * @param fallbackUrl
	 * @param request
	 * @param response
	 * @param modelMap
	 * @throws IOException
	 * @throws WeiboException
	 * @deprecated URL地址不标准，将在以后版本删除。请使用/oauth/login/weibo.jspx。
	 * @see #loginWeibo(String, HttpServletRequest, HttpServletResponse, org.springframework.ui.Model)
	 * 
	 */
	@Deprecated
	@RequestMapping(value = { "/oauth/weibo_login", Constants.SITE_PREFIX_PATH + "/oauth/weibo_login" })
	public void weiboLogin(String fallbackUrl, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException, WeiboException {
		loginWeibo(fallbackUrl, request, response, modelMap);
	}

	private void saveFallbackUrl(HttpServletRequest request) {
		String fallbackUrl = request.getParameter(FALLBACK_URL_PARAM);
		if (StringUtils.isNotBlank(fallbackUrl)) {
			request.getSession().setAttribute(FALLBACK_URL_PARAM, fallbackUrl);
		}
	}

	private String getState(HttpServletRequest request) {
		String state = UUID.randomUUID().toString().replace("-", "");
		request.getSession().setAttribute(SESSION_OAUTH_STATE, state);
		return state;
	}

	private boolean validateState(String state, HttpServletRequest request) {
		String sessionState = (String) request.getSession().getAttribute(SESSION_OAUTH_STATE);
		request.getSession().removeAttribute(SESSION_OAUTH_STATE);
		return state != null && state.equals(sessionState);
	}

	@RequestMapping(value = { "/oauth/authc/weixin", Constants.SITE_PREFIX_PATH + "/oauth/authc/weixin" })
	public String authcWeixin(String code, String state, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes ra) throws IOException, WeixinException {
		if (!validateState(state, request)) {
			return authcFail(request, response, ra, StateIllegalException.class);
		}
		OauthToken ot = new OauthApi(new WeixinAccount(appid, secret)).getAuthorizationToken(code);
		String ticket = ot.getAccessToken();
		logger.debug("oauth weixin access token:" + ticket);
		if (StringUtils.isBlank(ticket)) {
			return authcFail(request, response, ra, AccessTokenNotFoundException.class);
		}
		String openid = ot.getOpenId();
		logger.debug("oauth qq open id:" + openid);
		if (StringUtils.isBlank(openid)) {
			return authcFail(request, response, ra, OpenIDNotFoundException.class);
		}
		OAuthToken token = new OAuthToken(OAuthToken.PROVIDER_WEIXIN, ticket, openid);
		User user = userShiroService.findByWeixinOpenid(openid);
		return authc(request, response, ra, token, user);
	}

	@RequestMapping(value = { "/oauth/authc/weixin/session", Constants.SITE_PREFIX_PATH + "/oauth/authc/weixin/session" })
	public String authcWeixinSession(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws QQConnectException, IOException {
		OAuthToken token = (OAuthToken) request.getSession().getAttribute(OAUTH_TOKEN_SESSION_NAME);
		if (token == null) {
			return authcFail(request, response, ra, SessionTokenNotFoundException.class);
		}
		User user = userShiroService.findByWeixinOpenid(token.getOpenid());
		return authc(request, response, ra, token, user);
	}

	@RequestMapping(value = { "/oauth/authc/qq", Constants.SITE_PREFIX_PATH + "/oauth/authc/qq" })
	public String authcQq(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws QQConnectException, IOException {
		String ticket = (new Oauth()).getAccessTokenByRequest(request).getAccessToken();
		if (StringUtils.isBlank(ticket)) {
			return authcFail(request, response, ra, AccessTokenNotFoundException.class);
		}
		logger.debug("oauth qq access token:" + ticket);
		String openid = new OpenID(ticket).getUserOpenID();
		logger.debug("oauth qq open id:" + openid);
		if (StringUtils.isBlank(openid)) {
			return authcFail(request, response, ra, OpenIDNotFoundException.class);
		}
		OAuthToken token = new OAuthToken(OAuthToken.PROVIDER_QQ, ticket, openid);
		User user = userShiroService.findByQqOpenid(openid);
		return authc(request, response, ra, token, user);
	}

	@RequestMapping(value = { "/oauth/authc/qq/session", Constants.SITE_PREFIX_PATH + "/oauth/authc/qq/session" })
	public String authcQqSession(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws QQConnectException, IOException {
		OAuthToken token = (OAuthToken) request.getSession().getAttribute(OAUTH_TOKEN_SESSION_NAME);
		if (token == null) {
			return authcFail(request, response, ra, SessionTokenNotFoundException.class);
		}
		User user = userShiroService.findByQqOpenid(token.getOpenid());
		return authc(request, response, ra, token, user);
	}

	@RequestMapping(value = { "/oauth/authc/weibo", Constants.SITE_PREFIX_PATH + "/oauth/authc/weibo" })
	public String authcWeibo(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws WeiboException, JSONException, IOException {
		if (!validateState(request.getParameter("state"), request)) {
			return authcFail(request, response, ra, StateIllegalException.class);
		}
		String ticket = new weibo4j.Oauth().getAccessTokenByCode(request.getParameter("code")).getAccessToken();
		if (StringUtils.isBlank(ticket)) {
			return authcFail(request, response, ra, AccessTokenNotFoundException.class);
		}
		logger.debug("oauth weibo access token:" + ticket);
		String openid = new Account(ticket).getUid().getString("uid");
		logger.debug("oauth weibo uid:" + openid);
		if (StringUtils.isBlank(openid)) {
			return authcFail(request, response, ra, OpenIDNotFoundException.class);
		}
		OAuthToken token = new OAuthToken(OAuthToken.PROVIDER_WEIBO, ticket, openid);
		User user = userShiroService.findByWeiboUid(openid);
		return authc(request, response, ra, token, user);
	}

	@RequestMapping(value = { "/oauth/authc/weibo/session", Constants.SITE_PREFIX_PATH + "/oauth/authc/weibo/session" })
	public String authcWeiboSession(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws Exception {
		OAuthToken token = (OAuthToken) request.getSession().getAttribute(OAUTH_TOKEN_SESSION_NAME);
		if (token == null) {
			return authcFail(request, response, ra, SessionTokenNotFoundException.class);
		}
		User user = userShiroService.findByWeiboUid(token.getOpenid());
		return authc(request, response, ra, token, user);
	}

	private String authc(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra,
			OAuthToken token, User user) throws IOException {
		if (user == null) {
			// openid对应的用户不存在。
			logger.debug("OAuth token:" + token);
			request.getSession().setAttribute(OAUTH_TOKEN_SESSION_NAME, token);
			return "redirect:" + OAuthRegisterController.OAUTH_REGISTER_URL;
		}
		token.setUserId(user.getId());
		token.setUsername(user.getUsername());
		logger.debug("OAuth token:" + token);
		SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute(WebUtils.SAVED_REQUEST_KEY);
		Subject subject = SecurityUtils.getSubject();
		// 防止session fixation attack(会话固定攻击)，让旧session失效
		if (subject.getSession(false) != null) {
			subject.logout();
		}
		Builder builder = new Builder(request, response);
		Object principal = new ShiroUser(token.getUserId(), token.getUsername());
		PrincipalCollection principals = new SimplePrincipalCollection(principal, "oauth_realm_" + token.getProvider());
		builder.principals(principals).authenticated(true);
		ThreadContext.bind(builder.buildSubject());
		// 将SavedRequest放回session
		request.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);
		// 将第三方认证信息保留在session中，以便使用。
		request.getSession().setAttribute(OAUTH_TOKEN_SESSION_NAME, token);
		String ip = Servlets.getRemoteAddr(request);
		userShiroService.updateLoginSuccess(token.getUserId(), ip);
		logService.loginSuccess(ip, token.getUserId());

		HttpSession session = request.getSession();
		String successUrl = (String) session.getAttribute(FALLBACK_URL_PARAM);
		session.removeAttribute(FALLBACK_URL_PARAM);
		if (StringUtils.isNotBlank(successUrl)) {
			WebUtils.issueRedirect(request, response, successUrl, null, false);
		} else {
			WebUtils.redirectToSavedRequest(request, response, SUCCESS_URL);
		}
		return null;
	}

	private String authcFail(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra,
			Class<? extends AuthenticationException> ae) throws IOException {
		ra.addFlashAttribute(ERROR_KEY_ATTRIBUTE_NAME, ae.getName());
		ra.addAttribute(FALLBACK_URL_PARAM, Servlets.getParam(request, FALLBACK_URL_PARAM));
		return "redirect:" + LoginController.LOGIN_URL;
	}

	@Autowired
	private UserShiroService userShiroService;
	@Autowired
	private OperationLogService logService;
}

package com.jspxcms.core.web.fore;

import com.jspxcms.common.captcha.Captchas;
import com.jspxcms.common.security.CredentialsDigest;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.GlobalRegister;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.security.oauth.OAuthToken;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.OrgService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import com.octo.captcha.service.CaptchaService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OAUTH公开认证平台注册
 * 
 * 第一次通过OAUTH登录的用户，需要在CMS中注册。
 * 
 * @author liufang
 * 
 */
@Controller
public class OAuthRegisterController {
	/**
	 * OAUTH注册地址
	 */
	public static final String OAUTH_REGISTER_URL = "/oauth/register";
	/**
	 * OAUTH注册模板
	 */
	public static final String OAUTH_REGISTER_TEMPLATE = "sys_oauth_register.html";

	/**
	 * 注册表单
	 * 
	 * @param fallbackUrl
	 *            注册成功后，返回的地址。
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = { OAUTH_REGISTER_URL,
			Constants.SITE_PREFIX_PATH + OAUTH_REGISTER_URL })
	public String oauthRegisterForm(String fallbackUrl,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		GlobalRegister registerConf = site.getGlobal().getRegister();
		if (registerConf.getMode() == GlobalRegister.MODE_OFF) {
			return resp.warning("register.off");
		}
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(OAUTH_REGISTER_TEMPLATE);
	}

	/**
	 * 注册提交
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param email
	 *            邮箱
	 * @param gender
	 *            性别
	 * @param birthDate
	 *            出生日期
	 * @param bio
	 *            自我介绍
	 * @param comeFrom
	 *            来自
	 * @param qq
	 * @param msn
	 * @param weixin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = { OAUTH_REGISTER_URL,
			Constants.SITE_PREFIX_PATH + OAUTH_REGISTER_URL }, method = RequestMethod.POST)
	public String oauthRegisterSubmit(String username, String password,
			String email, String gender, Date birthDate, String bio,
			String comeFrom, String qq, String msn, String weixin,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		GlobalRegister reg = site.getGlobal().getRegister();
		OAuthToken token = (OAuthToken) request.getSession().getAttribute(
				OAuthLoginController.OAUTH_TOKEN_SESSION_NAME);
		if (StringUtils.isBlank(password)) {
			password = RandomStringUtils.randomAscii(32);
		}
		String result = validateRegisterSubmit(request, resp, reg, token,
				username, password, email, gender);
		if (resp.hasErrors()) {
			return result;
		}

		String ip = Servlets.getRemoteAddr(request);
		int groupId = reg.getGroupId();
		int orgId = reg.getOrgId();
		int status = User.NORMAL;
		String qqOpenid = null;
		String weiboUid = null;
		String weixinOpenid = null;
		if (token.isQq()) {
			qqOpenid = token.getOpenid();
		} else if (token.isWeibo()) {
			weiboUid = token.getOpenid();
		} else if (token.isWeixin()) {
			weixinOpenid = token.getOpenid();
		}
		userService.register(ip, groupId, orgId, status, username, password,null,
				email, qqOpenid, weiboUid, weixinOpenid, gender, birthDate,
				bio, comeFrom, qq, msn, weixin);
		return "redirect:/oauth/authc/" + token.getProvider() + "/session";
	}

	@RequestMapping(value = { "/oauth/bind",
			Constants.SITE_PREFIX_PATH + "/oauth/bind"}, method = RequestMethod.POST)
	public String oauthBindSubmit(String captcha, String username,
			String password, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		OAuthToken token = (OAuthToken) request.getSession().getAttribute(
				OAuthLoginController.OAUTH_TOKEN_SESSION_NAME);

		List<String> messages = resp.getMessages();
		if (!Captchas.isValid(captchaService, request, captcha)) {
			return resp.post(100, "error.captcha");
		}
		if (!Validations.exist(token)) {
			return resp.post(501, "register.oauthTokenNotFound");
		}
		if (!Validations.notEmpty(username, messages, "username")) {
			return resp.post(401);
		}
		User user = userService.findByUsername(username);
		if (!credentialsDigest.matches(user.getPassword(), password,
				user.getSaltBytes())) {
			return resp.post(502, "member.passwordError");
		}

		if (token.isQq()) {
			user.setQqOpenid(token.getOpenid());
		} else if (token.isWeibo()) {
			user.setWeiboUid(token.getOpenid());
		} else if (token.isWeixin()) {
			user.setWeixinOpenid(token.getOpenid());
		}
		userService.update(user, user.getDetail());
		return "redirect:/oauth/authc/" + token.getProvider() + "/session";
	}

	private String validateRegisterSubmit(HttpServletRequest request,
			Response resp, GlobalRegister reg, OAuthToken token,
			String username, String password, String email, String gender) {
		List<String> messages = resp.getMessages();
		if (reg.getMode() == GlobalRegister.MODE_OFF) {
			return resp.post(501, "register.off");
		}
		Integer groupId = reg.getGroupId();
		if (groupService.get(groupId) == null) {
			return resp.post(502, "register.groupNotSet");
		}
		Integer orgId = reg.getOrgId();
		if (orgService.get(orgId) == null) {
			return resp.post(503, "register.orgNotSet");
		}
		if (!Validations.exist(token)) {
			return resp.post(504, "register.oauthTokenNotFound");
		}
		if (!Validations.notEmpty(username, messages, "username")) {
			return resp.post(401);
		}
		if (!Validations.length(username, reg.getMinLength(),
				reg.getMaxLength(), messages, "username")) {
			return resp.post(402);
		}
		if (!Validations.pattern(username, reg.getValidCharacter(), messages,
				"username")) {
			return resp.post(403);
		}
		if (!Validations.notEmpty(password, messages, "password")) {
			return resp.post(404);
		}
		if (!Validations.email(email, messages, "email")) {
			return resp.post(406);
		}
		if (!Validations.pattern(gender, "[F,M]", messages, "gender")) {
			return resp.post(407);
		}
		return null;
	}

	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private MemberGroupService groupService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsDigest credentialsDigest;
}

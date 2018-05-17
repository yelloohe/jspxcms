package com.jspxcms.core.support;

import com.jspxcms.common.util.JsonMapper;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Response
 * 
 * @author liufang
 * 
 */
public class Response {
	public static final String NEXT_URL = "nextUrl";
	public static final String REDIRECT_URL = "redirectUrl";
	public static final String MESSAGE_PREFIX = "status_";
	public static final String TIMEOUT = "timeout";
	public static final String MESSAGES = "messages";
	public static final String STATUS = "status";
	public static final String AJAX_RESPONSE = "ajax";
	public static final String PAGE_RESPONSE = "page";
	public static final String OPERATION_SUCCESS = "sys_operation_success.html";
	public static final String OPERATION_WARNING = "sys_operation_warning.html";
	public static final String OPERATION_ERROR = "sys_operation_error.html";

	public String warning(String code, String[] args, String defaultMessage) {
		addMessage(code, args, defaultMessage);
		return warning();
	}

	public String warning(String code, String[] args) {
		addMessage(code, args);
		return warning();
	}

	public String warning(String code) {
		addMessage(code);
		return warning();
	}

	public String warning() {
		if (getStatus() == 0) {
			setStatus(200);
		}
		modelMap.addAttribute(STATUS, getStatus());
		modelMap.addAttribute(MESSAGES, getMessages());
		Site site = Context.getCurrentSite();
		String template = site.getTemplate(OPERATION_WARNING);
		Map<String, Object> map = modelMap.asMap();
		ForeContext.setData(map, request);
		return template;
	}

	/**
	 * 请求参数有误。
	 */
	public String badRequest(String code, String[] args, String defaultMessage) {
		addMessage(code, args, defaultMessage);
		return badRequest();
	}

	/**
	 * 请求参数有误。
	 */
	public String badRequest(String code, String[] args) {
		addMessage(code, args);
		return badRequest();
	}

	/**
	 * 请求参数有误。
	 */
	public String badRequest(String code) {
		addMessage(code);
		return badRequest();
	}

	/**
	 * 请求参数有误。
	 */
	public String badRequest() {
		if (getStatus() == 0) {
			setStatus(400);
		}
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		modelMap.addAttribute(STATUS, getStatus());
		modelMap.addAttribute(MESSAGES, getMessages());
		Site site = Context.getCurrentSite();
		String template = site.getTemplate(OPERATION_ERROR);
		Map<String, Object> map = modelMap.asMap();
		ForeContext.setData(map, request);
		return template;
	}

	/**
	 * 未找到
	 */
	public String notFound(String code, String[] args, String defaultMessage) {
		addMessage(code, args, defaultMessage);
		return notFound();
	}

	/**
	 * 未找到
	 */
	public String notFound(String code, String[] args) {
		addMessage(code, args);
		return notFound();
	}

	/**
	 * 未找到
	 */
	public String notFound(String code) {
		addMessage(code);
		return notFound();
	}

	/**
	 * 未找到
	 * 
	 * @return
	 */
	public String notFound() {
		if (getStatus() == 0) {
			setStatus(404);
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		modelMap.addAttribute(STATUS, getStatus());
		modelMap.addAttribute(MESSAGES, getMessages());
		Site site = Context.getCurrentSite();
		String template = site.getTemplate(OPERATION_ERROR);
		Map<String, Object> map = modelMap.asMap();
		ForeContext.setData(map, request);
		return template;
	}

	/**
	 * 禁止访问
	 */
	public String forbidden(String code, String[] args, String defaultMessage) {
		addMessage(code, args, defaultMessage);
		return forbidden();
	}

	/**
	 * 禁止访问
	 */
	public String forbidden(String code, String[] args) {
		addMessage(code, args);
		return forbidden();
	}

	/**
	 * 禁止访问
	 */
	public String forbidden(String code) {
		addMessage(code);
		return forbidden();
	}

	/**
	 * 禁止访问
	 */
	public String forbidden() {
		if (getStatus() == 0) {
			setStatus(403);
		}
		if (CollectionUtils.isEmpty(getMessages())) {
			addMessage("error.forbidden");
		}
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		modelMap.addAttribute(STATUS, getStatus());
		modelMap.addAttribute(MESSAGES, getMessages());
		Site site = Context.getCurrentSite();
		String template = site.getTemplate(OPERATION_ERROR);
		Map<String, Object> map = modelMap.asMap();
		ForeContext.setData(map, request);
		return template;
	}

	/**
	 * 未登录
	 * 
	 * @return
	 */
	public String unauthorized() {
		setStatus(401);
		WebUtils.saveRequest(request);
		try {
			WebUtils.issueRedirect(request, response, Constants.LOGIN_URL);
		} catch (IOException e) {
			throw new IllegalArgumentException("redirect to login error: "
					+ Constants.LOGIN_URL, e);
		}
		return null;
	}
	
	//internalServerError

	private int status = 0;
	private List<String> messages = new ArrayList<String>();
	private Map<String, Object> data = new HashMap<String, Object>();

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Model modelMap;
	private WebApplicationContext wac;
	private Locale locale;

	public Response(HttpServletRequest request, HttpServletResponse response,
			Model modelMap) {
		this.request = request;
		this.response = response;
		this.modelMap = modelMap;
		this.locale = RequestContextUtils.getLocale(request);
		this.wac = RequestContextUtils.findWebApplicationContext(request);
	}

	public String post(int status, String code, String[] args) {
		setStatus(status);
		addMessage(code, args);
		return post();
	}

	public String post(int status, String code) {
		setStatus(status);
		addMessage(code);
		return post();
	}

	public String post(int status) {
		setStatus(status);
		return post();
	}

	public String post() {
		Site site = Context.getCurrentSite();
		String responseType = Servlets.getParam(request, "responseType");
		if (AJAX_RESPONSE.equals(responseType)) {
			JsonMapper mapper = new JsonMapper();
			getData().put(STATUS, getStatus());
			getData().put(MESSAGES, getMessages());
			String json = mapper.toJson(getData());
			Servlets.writeHtml(response, json);
			return null;
		} else {
			// 过滤所有的状态提示，nextUrl，redirectUrl
			String redirectUrl = Servlets.getParam(request, REDIRECT_URL);
			String nextUrl = Servlets.getParam(request, NEXT_URL);
			Set<String> validDomains = site.getGlobal().getValidDomains();
			if (!Servlets.validateUrl(redirectUrl, validDomains)) {
				return warning("error.redirectDomainIllegal",
						new String[] { redirectUrl });
			}
			if (!Servlets.validateUrl(nextUrl, validDomains)) {
				return warning("error.redirectDomainIllegal",
						new String[] { nextUrl });
			}
			Map<String, String> sms = Servlets.getParamMap(request,
					MESSAGE_PREFIX, true);
			Map<String, String> newsms = new HashMap<String, String>(sms.size());
			String search, replacement, value;
			getData().put(STATUS, getStatus());
			for (Entry<String, Object> entry : getData().entrySet()) {
				search = "{" + entry.getKey() + "}";
				replacement = entry.getValue().toString();
				nextUrl = StringUtils.replace(nextUrl, search, replacement);
				redirectUrl = StringUtils.replace(redirectUrl, search,
						replacement);
				for (Entry<String, String> smEntry : sms.entrySet()) {
					value = smEntry.getValue();
					value = StringUtils.replace(value, search, replacement);
					newsms.put(smEntry.getKey(), value);
				}
			}
			// URL编码
			if (nextUrl != null) {
				nextUrl = response.encodeRedirectURL(nextUrl);
			}
			if (redirectUrl != null) {
				redirectUrl = response.encodeRedirectURL(redirectUrl);
			}
			// 如果成功且是重定向
			if (isSuccess() && StringUtils.isNotBlank(redirectUrl)) {
				try {
					response.sendRedirect(redirectUrl);
					return null;
				} catch (IOException e) {
					throw new IllegalArgumentException("redirect error: "
							+ redirectUrl, e);
				}
			}

			modelMap.addAttribute(MESSAGES, getMessages());
			modelMap.addAttribute(NEXT_URL, nextUrl);
			modelMap.addAllAttributes(getData());
			modelMap.addAllAttributes(newsms);
			String timeout = request.getParameter(TIMEOUT);
			if (StringUtils.isNotBlank(timeout)) {
				modelMap.addAttribute(TIMEOUT, timeout);
			}
			String template;
			if (isSuccess()) {
				template = site.getTemplate(OPERATION_SUCCESS);
			} else {
				template = site.getTemplate(OPERATION_ERROR);
			}
			Map<String, Object> map = modelMap.asMap();
			ForeContext.setData(map, request);
			return template;
		}
	}

	public boolean hasErrors() {
		return getStatus() >= 100;
	}

	public boolean isSuccess() {
		return !hasErrors();
	}

	public void addMessageRaw(String message) {
		getMessages().add(message);
	}

	public void addMessage(String code) {
		getMessages().add(wac.getMessage(code, null, code, locale));
	}

	public void addMessage(String code, String[] args) {
		getMessages().add(wac.getMessage(code, args, code, locale));
	}

	public void addMessage(String code, String[] args, String defaultMessage) {
		getMessages().add(wac.getMessage(code, args, defaultMessage, locale));
	}

	public void addData(String key, Object value) {
		getData().put(key, value);
	}

	/**
	 * <ul>
	 * <li>0-99为成功。
	 * <li>100-199为通用错误。100：验证码错误；101：未登录；102：无权限。
	 * <li>400-449为请求参数错误。
	 * <li>450-499为找不到请求参数所对应的数据。
	 * <li>500以上为逻辑错误。
	 * </ul>
	 * 
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}

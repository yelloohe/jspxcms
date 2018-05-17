package com.jspxcms.core.support;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//import com.jspxcms.core.domain.Global;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.security.ShiroUser;
import com.jspxcms.core.service.GlobalService;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.UserService;

/**
 * ForeInterceptor
 * 
 * @author liufang
 * 
 */
public class ForeInterceptor implements HandlerInterceptor {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Site site = null;
		Context.setMobile(false);
		String serverName = request.getServerName();
		site = siteService.findByDomain(serverName);
		if (site != null) {
			// 域名与站点域名不相同，则为手机访问（手机端域名可能与PC端域名一样）
			if (!serverName.equals(site.getDomain())) {
				Context.setMobile(true);
			}
		}
		if (site == null) {
			site = globalService.findUnique().getSite();
		}
		if (site == null) {
			throw new CmsException("site.error.siteNotFound");
		}
		Context.setCurrentSite(site);
		
		Device device = deviceResolver.resolveDevice(request);
		// 手机域名存在，并且是手机客户端访问
		if(StringUtils.isNotBlank(site.getMobileDomain()) && device.isMobile()) {
			Context.setMobile(true);
		}
		ShiroUser shiroUser = null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		// 用户登录信息，允许记住用户。
		if (principal != null) {
			if (principal instanceof ShiroUser) {
				shiroUser = (ShiroUser) principal;
			} else {
				subject.logout();
			}
		}
		if (shiroUser != null) {
			User user = userService.get(shiroUser.id);
			Context.setCurrentUser(user);
			Context.setCurrentGroup(request, user.getGroup());
			Context.setCurrentGroups(request, user.getGroups());
			Context.setCurrentOrg(request, user.getOrg());
			Context.setCurrentOrgs(request, user.getOrgs());
		} else {
			MemberGroup anon = memberGroupService.getAnonymous();
			Context.setCurrentGroup(request, anon);
			Context.setCurrentGroups(request, Arrays.asList(new MemberGroup[] { anon }));
			// 未登录，组织为空
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Context.resetCurrentSite();
		Context.resetCurrentUser();
		Context.resetMobile();
	}

	private DeviceResolver deviceResolver;
	private SiteService siteService;
	private GlobalService globalService;
	private UserService userService;
	private MemberGroupService memberGroupService;

	@Autowired
	public void setDeviceResolver(DeviceResolver deviceResolver) {
		this.deviceResolver = deviceResolver;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setMemberGroupService(MemberGroupService memberGroupService) {
		this.memberGroupService = memberGroupService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	@Autowired
	public void setGlobalService(GlobalService globalService) {
		this.globalService = globalService;
	}
}

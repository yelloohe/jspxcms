package com.jspxcms.core.support;

import java.util.Collection;

import javax.servlet.ServletRequest;

import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;

/**
 * CMS上下文
 * 
 * @author liufang
 * 
 */
public abstract class Context {
	/**
	 * 是否手机访问线程变量
	 */
	private static ThreadLocal<Boolean> isMobileHolder = new ThreadLocal<Boolean>();

	public static void setMobile(boolean isMobile) {
		isMobileHolder.set(isMobile);
	}

	public static boolean isMobile() {
		return isMobileHolder.get()!=null && isMobileHolder.get();
	}

	public static void resetMobile() {
		isMobileHolder.remove();
	}
	
	/**
	 * 站点线程变量
	 */
	private static ThreadLocal<Site> siteHolder = new ThreadLocal<Site>();

	public static void setCurrentSite(Site site) {
		siteHolder.set(site);
	}

	public static Site getCurrentSite() {
		return siteHolder.get();
	}

	public static void resetCurrentSite() {
		siteHolder.remove();
	}

	/**
	 * 用户线程变量
	 */
	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

	public static void setCurrentUser(User user) {
		userHolder.set(user);
	}

	public static User getCurrentUser() {
		return userHolder.get();
	}

	public static void resetCurrentUser() {
		userHolder.remove();
	}

	public static Integer getCurrentSiteId() {
		Site site = getCurrentSite();
		return site != null ? site.getId() : null;
	}

	public static Integer getCurrentUserId() {
		User user = getCurrentUser();
		return user != null ? user.getId() : null;
	}

	public static MemberGroup getCurrentGroup(ServletRequest request) {
		MemberGroup group = (MemberGroup) request
				.getAttribute(GROUP_REQUEST_NAME);
		return group;
	}

	public static void setCurrentGroup(ServletRequest request, MemberGroup group) {
		request.setAttribute(GROUP_REQUEST_NAME, group);
	}

	public static void resetCurrentGroup(ServletRequest request) {
		request.removeAttribute(GROUP_REQUEST_NAME);
	}

	public static Org getCurrentOrg(ServletRequest request) {
		Org org = (Org) request.getAttribute(ORG_REQUEST_NAME);
		return org;
	}

	public static void setCurrentOrg(ServletRequest request, Org org) {
		request.setAttribute(ORG_REQUEST_NAME, org);
	}

	public static void resetCurrentOrg(ServletRequest request) {
		request.removeAttribute(ORG_REQUEST_NAME);
	}

	public static Collection<MemberGroup> getCurrentGroups(
			ServletRequest request) {
		@SuppressWarnings("unchecked")
		Collection<MemberGroup> groups = (Collection<MemberGroup>) request
				.getAttribute(GROUPS_REQUEST_NAME);
		return groups;
	}

	public static void setCurrentGroups(ServletRequest request,
			Collection<MemberGroup> groups) {
		request.setAttribute(GROUPS_REQUEST_NAME, groups);
	}

	public static void resetCurrentGroups(ServletRequest request) {
		request.removeAttribute(GROUPS_REQUEST_NAME);
	}

	public static Collection<Org> getCurrentOrgs(ServletRequest request) {
		@SuppressWarnings("unchecked")
		Collection<Org> orgs = (Collection<Org>) request
				.getAttribute(ORGS_REQUEST_NAME);
		return orgs;
	}

	public static void setCurrentOrgs(ServletRequest request,
			Collection<Org> orgs) {
		request.setAttribute(ORGS_REQUEST_NAME, orgs);
	}

	public static void resetCurrentOrgs(ServletRequest request) {
		request.removeAttribute(ORGS_REQUEST_NAME);
	}

	// private static final String SITE_REQUEST_NAME = "_CMS_SITE_REQUEST";
	// private static final String USER_REQUEST_NAME = "_CMS_USER_REQUEST";
	private static final String GROUP_REQUEST_NAME = "_CMS_GROUP_REQUEST";
	private static final String GROUPS_REQUEST_NAME = "_CMS_GROUPS_REQUEST";
	private static final String ORG_REQUEST_NAME = "_CMS_ORG_REQUEST";
	private static final String ORGS_REQUEST_NAME = "_CMS_ORGS_REQUEST";
}

package com.jspxcms.core.support;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.BeanFactory;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.security.ShiroUser;
import com.jspxcms.core.service.GlobalService;
import com.jspxcms.core.service.SiteShiroService;

/**
 * SiteFilter
 * 
 * @author liufang
 * 
 */
public class BackSiteFilter implements Filter {
	private BeanFactory beanFactory;

	public BackSiteFilter(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public static final String SITE_KEY = "_site";

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		Site site = resolveSite(request, response);
		Context.setCurrentSite(site);
		chain.doFilter(request, response);
		Context.resetCurrentSite();
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	private Site resolveSite(HttpServletRequest request, HttpServletResponse response) {
		Site site = null;
		Integer siteId;
		// 从parameter中获取
		String siteIdText = request.getParameter(SITE_KEY);
		if (StringUtils.isNotBlank(siteIdText)) {
			try {
				siteId = Integer.parseInt(siteIdText);
				site = getSiteShiroService().get(siteId);
			} catch (Exception e) {
				// continue
			}
		}
		// 从cookie中获取
		if (site == null) {
			siteIdText = Servlets.getCookie(request, SITE_KEY);
			if (StringUtils.isNotBlank(siteIdText)) {
				try {
					siteId = Integer.parseInt(siteIdText);
					site = getSiteShiroService().get(siteId);
				} catch (Exception e) {
					// continue
				}
			}
		}
		// 从域名中获取
		if (site == null) {
			String domain = request.getServerName();
			site = getSiteShiroService().findByDomain(domain);
		}
		// 从数据库中获得主站
		if (site == null) {
			site = getGlobalService().findUnique().getSite();
		}
		// 获取第一个有权限的站点
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
			List<Site> siteList = getSiteShiroService().findByUserId(shiroUser.id);
			if (!siteList.isEmpty()) {
				boolean contains = false;
				if (site != null) {
					for (Site s : siteList) {
						if (s.getId().equals(site.getId())) {
							contains = true;
							break;
						}
					}
				}
				if (!contains) {
					site = siteList.iterator().next();
				}
			}
		}
		if (site != null) {
			clearPermCache(request, site.getId());
			addSiteCookie(site.getId().toString(), request, response);
			return site;
		} else {
			// 站点必须存在
			throw new CmsException("site.error.siteNotFound");
		}
	}

	private void addSiteCookie(String siteIdText, HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie(SITE_KEY, siteIdText);
		String path = request.getContextPath();
		if (StringUtils.isBlank(path)) {
			path = "/";
		}
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	private void clearPermCache(HttpServletRequest request, Integer currSiteId) {
		HttpSession session = request.getSession();
		Integer prevSiteId = (Integer) session.getAttribute(SITE_KEY);
		if (prevSiteId == null || !currSiteId.equals(prevSiteId)) {
			getEhCacheManager().getCacheManager().clearAll();
		}
		session.setAttribute(SITE_KEY, currSiteId);
	}

	private EhCacheManager ehCacheManager;
	private SiteShiroService siteShiroService;
	private GlobalService globalService;

	public EhCacheManager getEhCacheManager() {
		if (ehCacheManager == null) {
			ehCacheManager = beanFactory.getBean(EhCacheManager.class);
		}
		return ehCacheManager;
	}

	public SiteShiroService getSiteShiroService() {
		if (siteShiroService == null) {
			siteShiroService = beanFactory.getBean(SiteShiroService.class);
		}
		return siteShiroService;
	}

	public GlobalService getGlobalService() {
		if (globalService == null) {
			globalService = beanFactory.getBean(GlobalService.class);
		}
		return globalService;
	}

	public void setSiteShiroService(SiteShiroService siteShiroService) {
		this.siteShiroService = siteShiroService;
	}

	public void setEhCacheManager(EhCacheManager ehCacheManager) {
		this.ehCacheManager = ehCacheManager;
	}

	public void setGlobalService(GlobalService globalService) {
		this.globalService = globalService;
	}
}

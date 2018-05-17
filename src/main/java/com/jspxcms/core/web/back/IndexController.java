package com.jspxcms.core.web.back;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.holder.MenuHolder;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.Context;

/**
 * 后台首页
 * 
 * @author liufang
 * 
 */
@Controller
public class IndexController {
	/**
	 * 后台首页
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping({ "/", "/index.do" })
	public String index(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			Site site = Context.getCurrentSite();
			User user = Context.getCurrentUser();
			List<Site> siteList = siteService.findByUserId(user.getId());
			modelMap.addAttribute("menus", menuHolder.getMenus());
			modelMap.addAttribute("user", user);
			modelMap.addAttribute("site", site);
			modelMap.addAttribute("siteList", siteList);
			return "index";
		} else {
			return "login";
		}
	}

	/**
	 * 空白页。后台的左侧框架无内容时，使用空白页
	 * 
	 * @return
	 */
	@RequestMapping("/blank.do")
	public String blank() {
		return "blank";
	}

	@Autowired
	private MenuHolder menuHolder;
	@Autowired
	private SiteService siteService;

}

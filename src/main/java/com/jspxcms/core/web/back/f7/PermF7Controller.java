package com.jspxcms.core.web.back.f7;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.holder.MenuHolder;

/**
 * PermF7Controller
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/role")
public class PermF7Controller {

	@RequestMapping("choose_perm_tree.do")
	public String f7NodeTree(HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		modelMap.addAttribute("menus", menuHolder.getMenus());
		Servlets.setNoCacheHeader(response);
		return "core/role/choose_perm_tree";
	}

	@Autowired
	private MenuHolder menuHolder;
}

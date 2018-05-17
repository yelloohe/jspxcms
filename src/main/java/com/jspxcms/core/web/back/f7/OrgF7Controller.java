package com.jspxcms.core.web.back.f7;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.service.OrgService;

/**
 * NodeF7Controller
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/org")
public class OrgF7Controller {
	/**
	 * 组织单选。
	 * 
	 * @param id
	 * @param excludeChildrenId
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("choose_org_tree.do")
	public String f7OrgTree(Integer id, String treeNumber,
			@RequestParam(defaultValue = "true") Boolean allowRoot,
			Integer excludeChildrenId, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		if (StringUtils.isNotBlank(treeNumber)) {
			allowRoot = false;
		}
		List<Org> list = service.findList(treeNumber);
		Org bean = null, excludeChildrenBean = null;
		if (id != null) {
			bean = service.get(id);
		}
		if (excludeChildrenId != null) {
			excludeChildrenBean = service.get(excludeChildrenId);
		}

		modelMap.addAttribute("id", id);
		modelMap.addAttribute("allowRoot", allowRoot);
		modelMap.addAttribute("excludeChildrenId", excludeChildrenId);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("excludeChildrenBean", excludeChildrenBean);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("treeNumber", treeNumber);
		Servlets.setNoCacheHeader(response);
		return "core/org/choose_org_tree";
	}

	@RequestMapping("choose_org_tree_multi.do")
	public String f7OrgTreeMulti(Integer[] ids, String treeNumber,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		List<Org> list = service.findList(treeNumber);
		List<Org> beans = new ArrayList<Org>();
		if (ids != null) {
			for (Integer id : ids) {
				beans.add(service.get(id));
			}
		}

		modelMap.addAttribute("ids", ids);
		modelMap.addAttribute("beans", beans);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("treeNumber", treeNumber);
		Servlets.setNoCacheHeader(response);
		return "core/org/choose_org_tree_multi";
	}

	@Autowired
	private OrgService service;
}

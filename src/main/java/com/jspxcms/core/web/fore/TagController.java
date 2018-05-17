package com.jspxcms.core.web.fore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.Tag;
import com.jspxcms.core.service.TagService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import com.jspxcms.core.support.SiteResolver;

/**
 * TagsController
 * 
 * @author liufang
 * 
 */
@Controller
public class TagController {
	public static final String TAG_LIST_TEMPLATE = "sys_tag_list.html";
	public static final String TAG_TEMPLATE = "sys_tag.html";

	@RequestMapping("/tag")
	public String index(Integer page, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		return index(null, page, request, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/tag")
	public String index(@PathVariable String siteNumber, Integer page,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(TAG_LIST_TEMPLATE);
	}

	@RequestMapping("/tag/{id}")
	public String tag(@PathVariable Integer id, Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return tag(null, id, page, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/tag/{id}")
	public String tag(@PathVariable String siteNumber,
			@PathVariable Integer id, Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Tag tag = service.get(id);
		Response resp = new Response(request, response, modelMap);
		if (tag == null) {
			return resp.badRequest("Tag not found: " + id);
		}
		return tag(tag, page, request, modelMap);
	}

	@RequestMapping("/tagname/{name}")
	public String tagname(@PathVariable String name, Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return tagname(null, name, page, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/tagname/{name}")
	public String tagname(@PathVariable String siteNumber,
			@PathVariable String name, Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		Tag tag = service.findByName(site.getId(), name);
		Response resp = new Response(request, response, modelMap);
		if (tag == null) {
			return resp.badRequest("tagname not found: " + name);
		}
		return tag(tag, page, request, modelMap);
	}

	private String tag(Tag tag, Integer page, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		modelMap.addAttribute("tag", tag);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(TAG_TEMPLATE);
	}

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private TagService service;
}

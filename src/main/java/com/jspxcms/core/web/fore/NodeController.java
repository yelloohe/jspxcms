package com.jspxcms.core.web.fore;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.NodeBufferService;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import com.jspxcms.core.support.SiteResolver;

/**
 * NodeController
 * 
 * @author liufang
 * 
 */
@Controller
public class NodeController {
	@RequestMapping(value = { "/", "/index" })
	public String index(HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return index(null, request, response, modelMap);
	}

	@RequestMapping(value = Constants.SITE_PREFIX_PATH + "")
	public String index(@PathVariable String siteNumber,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		Node node = query.findRoot(site.getId());
		if (!Validations.exist(node, messages, "Node", "root")) {
			return resp.badRequest();
		}
		Collection<MemberGroup> groups = Context.getCurrentGroups(request);
		Collection<Org> orgs = Context.getCurrentOrgs(request);
		if (!node.isViewPerm(groups, orgs)) {
			User user = Context.getCurrentUser();
			if (user != null) {
				return resp.forbidden();
			} else {
				return resp.unauthorized();
			}
		}
		modelMap.addAttribute("node", node);
		modelMap.addAttribute("text", node.getText());

		ForeContext.setData(modelMap.asMap(), request);
		String template = Servlets.getParam(request, "template");
		if (StringUtils.isNotBlank(template)) {
			return template;
		} else {
			return node.getTemplate();
		}
	}

	@RequestMapping("/node/{id:[0-9]+}")
	public String node(@PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return node(null, id, 1, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/node/{id:[0-9]+}")
	public String node(@PathVariable String siteNumber,
			@PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return node(siteNumber, id, 1, request, response, modelMap);
	}

	@RequestMapping("/node/{id:[0-9]+}_{page:[0-9]+}")
	public String node(@PathVariable Integer id, @PathVariable Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		return node(null, id, page, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH
			+ "/node/{id:[0-9]+}_{page:[0-9]+}")
	public String node(@PathVariable String siteNumber,
			@PathVariable Integer id, @PathVariable Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Node node = query.get(id);
		siteResolver.resolveSite(siteNumber, node);
		Site site = Context.getCurrentSite();
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		if (!Validations.exist(node, messages, "Node", id)) {
			return resp.badRequest();
		}
		if (!node.getSite().getId().equals(site.getId())) {
			site = node.getSite();
			Context.setCurrentSite(site);
		}
		Collection<MemberGroup> groups = Context.getCurrentGroups(request);
		Collection<Org> orgs = Context.getCurrentOrgs(request);
		if (!node.isViewPerm(groups, orgs)) {
			User user = Context.getCurrentUser();
			if (user != null) {
				return resp.forbidden();
			} else {
				return resp.unauthorized();
			}
		}
		String linkUrl = node.getLinkUrl();
		if (StringUtils.isNotBlank(linkUrl)) {
			return "redirect:" + linkUrl;
		}
		modelMap.addAttribute("node", node);
		modelMap.addAttribute("text", node.getText());

		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page, node);
		String template = Servlets.getParam(request, "template");
		if (StringUtils.isNotBlank(template)) {
			return template;
		} else {
			return node.getTemplate();
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/node_views/{id:[0-9]+}",
			Constants.SITE_PREFIX_PATH + "/node_views/{id:[0-9]+}" })
	public String views(@PathVariable Integer id) {
		return Integer.toString(bufferService.updateViews(id));
	}

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private NodeBufferService bufferService;
	@Autowired
	private NodeQueryService query;
}

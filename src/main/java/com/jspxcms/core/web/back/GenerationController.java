package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPERATION_SUCCESS;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.fulltext.InfoFulltextGenerator;
import com.jspxcms.core.html.HtmlGenerator;
import com.jspxcms.core.html.HtmlService;
import com.jspxcms.core.service.ModelService;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;

/**
 * GenerationController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/generation")
public class GenerationController {
	private static final Logger logger = LoggerFactory.getLogger(GenerationController.class);

	@RequiresPermissions("core:generation:html_index")
	@RequestMapping("html_index.do")
	public String htmlIndex(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		return "core/generation/html_index";
	}

	@RequiresPermissions("core:generation:html_submit")
	@RequestMapping("make_all_html.do")
	public String makeAllHtml(HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		Integer userId = Context.getCurrentUserId();
		htmlGenerator.makeAll(site.getId(), site.getName(), userId, false);
		logService.operation("opr.html.makeAll", site.getName(), null, site.getId(), request);
		logger.info("make all HTML. name={}", site.getName());
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:html_index.do";
	}

	@RequiresPermissions("core:generation:html_submit")
	@RequestMapping("one_key_enable.do")
	public String oneKeyEnable(HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		modelService.oneKeyEnableHtml(site.getId());
		logService.operation("html.oneKeyEnable", site.getName(), null, site.getId(), request);
		logger.info("one key enable. name={}", site.getName());
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:html_index.do";
	}
	
	@RequiresPermissions("core:generation:html_submit")
	@RequestMapping("one_key_disable.do")
	public String oneKeyDisable(HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		modelService.oneKeyDisableHtml(site.getId());
		logService.operation("html.oneKeyEnable", site.getName(), null, site.getId(), request);
		logger.info("one key enable. name={}", site.getName());
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:html_index.do";
	}

	@RequiresPermissions("core:generation:html_submit")
	@RequestMapping("make_home_html.do")
	public String makeHomeHtml(HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Node node = nodeQuery.findRoot(siteId);
		if (node != null) {
			htmlService.makeNode(node.getId());
			logService.operation("opr.html.makeHome", node.getName(), null, node.getId(), request);
			logger.info("make home HTML. name={}", node.getName());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:html_index.do";
	}

	@RequiresPermissions("core:generation:html_submit")
	@RequestMapping("delete_home_html.do")
	public String deleteHomeHtml(HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Node node = nodeQuery.findRoot(siteId);
		if (node != null) {
			htmlGenerator.deleteHtml(node);
			logService.operation("opr.html.deleteHome", node.getName(), null, node.getId(), request);
			logger.info("delete home HTML. name={}", node.getName());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:html_index.do";
	}

	@RequiresPermissions("core:generation:html_submit")
	@RequestMapping("make_node_html.do")
	public String makeNodeHtml(Integer nodeId, @RequestParam(defaultValue = "true") boolean includeChildren,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Integer userId = Context.getCurrentUserId();
		String nodeName = null;
		if (nodeId != null) {
			Node node = nodeQuery.get(nodeId);
			Backends.validateDataInSite(node, siteId);
			nodeName = node.getDisplayName();
		}
		htmlGenerator.makeNode(siteId, nodeId, nodeName, includeChildren, userId);
		logService.operation("opr.html.makeNode", nodeName, null, nodeId, request);
		logger.info("make node HTML. name={}", nodeName);
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:html_index.do";
	}

	@RequiresPermissions("core:generation:html_submit")
	@RequestMapping("make_info_html.do")
	public String makeInfoHtml(Integer nodeId, @RequestParam(defaultValue = "true") boolean includeChildren,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Integer userId = Context.getCurrentUserId();
		String nodeName = null;
		if (nodeId != null) {
			Node node = nodeQuery.get(nodeId);
			Backends.validateDataInSite(node, siteId);
			nodeName = node.getDisplayName();
		}
		htmlGenerator.makeInfo(siteId, nodeId, nodeName, includeChildren, userId);
		logService.operation("opr.html.makeInfo", nodeName, null, nodeId, request);
		logger.info("make info HTML. name={}", nodeName);
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:html_index.do";
	}

	@RequiresPermissions("core:generation:fulltext_submit")
	@RequestMapping("fulltext_submit.do")
	public String fulltextSubmit(Integer nodeId, HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Integer userId = Context.getCurrentUserId();
		Node node = null;
		String nodeName = null;
		if (nodeId != null) {
			node = nodeQuery.get(nodeId);
			Backends.validateDataInSite(node, siteId);
			nodeName = node.getName();
		}
		infoFulltextGenerator.addDocument(siteId, node, userId);
		logService.operation("opr.fulltext.make", nodeName, null, nodeId, request);
		logger.info("make fulltext. name={}", nodeName);
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:html_index.do";
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private NodeQueryService nodeQuery;
	@Autowired
	private HtmlService htmlService;
	@Autowired
	private HtmlGenerator htmlGenerator;
	@Autowired
	private InfoFulltextGenerator infoFulltextGenerator;
}

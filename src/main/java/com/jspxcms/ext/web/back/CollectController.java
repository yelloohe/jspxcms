package com.jspxcms.ext.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPERATION_SUCCESS;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;
import com.jspxcms.ext.collect.Collector;
import com.jspxcms.ext.domain.Collect;
import com.jspxcms.ext.domain.CollectField;
import com.jspxcms.ext.service.CollectService;

@Controller
@RequestMapping("/ext/collect")
public class CollectController {
	private static final Logger logger = LoggerFactory.getLogger(CollectController.class);

	@RequiresPermissions("ext:collect:list")
	@RequestMapping("list.do")
	public String list(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		Page<Collect> pagedList = service.findAll(siteId, params, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		return "ext/collect/collect_list";
	}

	@RequiresPermissions("ext:collect:create")
	@RequestMapping("create.do")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		if (id != null) {
			Collect bean = service.get(id);
			Backends.validateDataInSite(bean, site.getId());
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "ext/collect/collect_form";
	}

	@RequiresPermissions("ext:collect:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Collect bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		RowSide<Collect> side = service.findSide(siteId, params, bean, position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "ext/collect/collect_form";
	}

	@RequiresPermissions("ext:collect:save")
	@RequestMapping("save.do")
	public String save(Collect bean, Integer nodeId, String redirect, HttpServletRequest request,
			RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Integer userId = Context.getCurrentUserId();
		Node node = nodeQueryService.get(nodeId);
		Backends.validateDataInSite(node, siteId);
		service.save(bean, nodeId, userId, siteId);
		logService.operation("opr.collect.add", bean.getName(), null, bean.getId(), request);
		logger.info("save Collect, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else if (Constants.REDIRECT_CREATE.equals(redirect)) {
			return "redirect:create.do";
		} else {
			ra.addAttribute("id", bean.getId());
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("ext:collect:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Collect bean, Integer nodeId, Integer position, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		Node node = nodeQueryService.get(nodeId);
		Backends.validateDataInSite(node, site.getId());
		Backends.validateDataInSite(bean, site.getId());
		service.update(bean, nodeId);
		logService.operation("opr.collect.edit", bean.getName(), null, bean.getId(), request);
		logger.info("update Collect, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("ext:collect:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		List<Collect> beans = service.delete(ids);
		for (Collect bean : beans) {
			logService.operation("opr.collect.delete", bean.getName(), null, bean.getId(), request);
			logger.info("delete Collect, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@RequestMapping("list_pattern_dialog.do")
	public String listPatternDialog(String listPattern, Integer pageBegin, Integer pageEnd, String charset,
			String userAgent, String areaId, String itemId, @RequestParam(defaultValue = "true") boolean desc,
			org.springframework.ui.Model modelMap) throws ClientProtocolException, IOException {
		List<String> urls = Collect.getListUrls(listPattern, pageBegin, pageEnd, desc);
		modelMap.addAttribute("urls", urls);
		modelMap.addAttribute("charset", charset);
		modelMap.addAttribute("userAgent", userAgent);
		modelMap.addAttribute("areaId", areaId);
		modelMap.addAttribute("itemId", itemId);
		return "ext/collect/collect_pattern_dialog";
	}

	@RequestMapping("item_pattern_dialog.do")
	public String itemPatternDialog(Integer collectId, String filterId, String areaId, String itemId,
			org.springframework.ui.Model modelMap) throws ClientProtocolException, IOException, URISyntaxException {
		Collect collect = service.get(collectId);
		List<URI> listUris = collect.getListUris();
		List<String> urls = new ArrayList<String>();
		if (listUris.size() > 0) {
			URI uri = listUris.get(0);
			String html = Collect.fetchHtml(uri, collect.getCharset(), collect.getUserAgent());
			List<URI> itemUris = collect.getItemUris(html, uri);
			for (URI itemUri : itemUris) {
				urls.add(itemUri.toString());
			}
		}
		modelMap.addAttribute("urls", urls);
		modelMap.addAttribute("charset", collect.getCharset());
		modelMap.addAttribute("userAgent", collect.getUserAgent());
		modelMap.addAttribute("filterId", filterId);
		modelMap.addAttribute("areaId", areaId);
		modelMap.addAttribute("itemId", itemId);
		return "ext/collect/collect_pattern_dialog";
	}

	@RequestMapping("id_pattern_dialog.do")
	public String idPatternDialog(Integer collectId, String idPattern, boolean idReg, String idUrl, boolean isUrlType,
			String filterId, String areaId, String itemId, org.springframework.ui.Model modelMap)
			throws ClientProtocolException, IOException, URISyntaxException {
		Collect collect = service.get(collectId);
		String charset = collect.getCharset();
		String userAgent = collect.getUserAgent();
		List<URI> listUris = collect.getListUris();
		List<String> urls = new ArrayList<String>();
		if (listUris.size() > 0) {
			URI uri = listUris.get(0);
			String html = Collect.fetchHtml(uri, charset, userAgent);
			List<URI> itemUris = collect.getItemUris(html, uri);
			for (URI itemUri : itemUris) {
				html = Collect.fetchHtml(itemUri, charset, userAgent);
				String id = Collect.findFirst(html, idPattern, idReg);
				urls.add(StringUtils.replace(idUrl, "{id}", id));
			}
		}
		modelMap.addAttribute("urls", urls);
		modelMap.addAttribute("charset", collect.getCharset());
		modelMap.addAttribute("userAgent", collect.getUserAgent());
		modelMap.addAttribute("filterId", filterId);
		modelMap.addAttribute("areaId", areaId);
		modelMap.addAttribute("itemId", itemId);
		return "ext/collect/collect_pattern_dialog";
	}

	@RequestMapping("find_text.do")
	public void findText(String source, String search, @RequestParam(defaultValue = "false") boolean isReg,
			@RequestParam(defaultValue = "true") boolean isFirst, HttpServletResponse response) {
		StringBuilder result = new StringBuilder();
		if (isFirst) {
			result.append(Collect.findFirst(source, search, isReg));
		} else {
			for (String s : Collect.find(source, search, isReg)) {
				result.append(s).append('\n');
			}
			if (result.length() > 1) {
				result.setLength(result.length() - 1);
			}
		}
		Servlets.writeHtml(response, result.toString());
	}

	@RequestMapping("filter_text.do")
	public void filterText(String source, String filter, HttpServletResponse response) {
		List<Pattern> patterns = CollectField.getFilterPattern(filter);
		String result = CollectField.applyFilter(patterns, source);
		Servlets.writeHtml(response, result);
	}

	@RequestMapping("fetch_url.do")
	public void fetchUrl(String url, String charset, String userAgent, HttpServletResponse response) {
		String source;
		try {
			source = Collect.fetchHtml(URI.create(url), charset, userAgent);
		} catch (Exception e) {
			source = e.getMessage();
		}
		Servlets.writeHtml(response, source);
	}

	@RequiresPermissions("ext:collect:start")
	@RequestMapping("start.do")
	public String start(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Collect bean;
		for (Integer id : ids) {
			collector.start(id);
			bean = service.get(id);
			logService.operation("opr.collect.start", bean.getName(), null, bean.getId(), request);
			logger.info("start Collect, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("ext:collect:stop")
	@RequestMapping("stop.do")
	public String stop(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Collect bean;
		for (Integer id : ids) {
			service.ready(id);
			bean = service.get(id);
			logService.operation("opr.collect.stop", bean.getName(), null, bean.getId(), request);
			logger.info("stop Collect, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	@RequestMapping("schedule_job.do")
	public String scheduleJob(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		List<Collect> collectList = service.findList(siteId);
		modelMap.addAttribute("collectList", collectList);
		modelMap.addAttribute("includePage", "../../ext/collect/collect_job.jsp");
		return "core/schedule_job/schedule_job_form";
	}

	@ModelAttribute
	public void preloadBean(@RequestParam(required = false) Integer oid, org.springframework.ui.Model modelMap) {
		if (oid != null) {
			Collect bean = service.get(oid);
			modelMap.addAttribute("bean", bean);
		}
	}

	private void validateIds(Integer[] ids, Integer siteId) {
		for (Integer id : ids) {
			Backends.validateDataInSite(service.get(id), siteId);
		}
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private NodeQueryService nodeQueryService;
	@Autowired
	private Collector collector;
	@Autowired
	private CollectService service;
}

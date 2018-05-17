package com.jspxcms.ext.web.fore;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.Response;
import com.jspxcms.core.support.SiteResolver;
import com.jspxcms.ext.domain.AdSlot;
import com.jspxcms.ext.service.AdSlotService;

/**
 * AdController 广告控制器
 * 
 * @author yangxing
 * 
 */
@Controller
public class AdController {
	@RequestMapping("/dd/{id}")
	public String list(@PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return list(null, id, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/dd/{id}")
	public String list(@PathVariable String siteNumber,
			@PathVariable Integer id, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		if (!Validations.notNull(id, messages, "id")) {
			return resp.badRequest();
		}
		AdSlot slot = service.get(id);
		if (!Validations.exist(slot, messages, "AdSlot", id)) {
			return resp.badRequest();
		}
		return list(slot, request, response, modelMap);
	}

	@RequestMapping("/dd_number/{number}")
	public String list(@PathVariable String number, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return list(null, number, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/dd_number/{number}")
	public String list(@PathVariable String siteNumber,
			@PathVariable String number, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		if (!Validations.notEmpty(number, messages, "number")) {
			return resp.badRequest();
		}
		Site site = Context.getCurrentSite();
		AdSlot slot = null;
		List<AdSlot> slotList = service.findList(number, site.getId());
		if (!slotList.isEmpty()) {
			slot = slotList.get(0);
		}
		if (!Validations.exist(slot, messages, "AdSlot", number)) {
			return resp.badRequest();
		}
		return list(slot, request, response, modelMap);
	}

	private String list(AdSlot slot, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		modelMap.addAttribute("slot", slot);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(slot.getTemplate());
	}

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private AdSlotService service;
}

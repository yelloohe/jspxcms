package com.jspxcms.ext.web.fore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.ForeContext;
import com.jspxcms.core.support.SiteResolver;

/**
 * FriendlinkController
 * 
 * @author yangxing
 * 
 */
@Controller
public class FriendlinkController {
	public static final String TEMPLATE = "sys_friendlink.html";

	@RequestMapping("/friendlink")
	public String list(HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		return list(null, request, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/friendlink")
	public String list(@PathVariable String siteNumber,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(TEMPLATE);
	}

	@Autowired
	private SiteResolver siteResolver;
}

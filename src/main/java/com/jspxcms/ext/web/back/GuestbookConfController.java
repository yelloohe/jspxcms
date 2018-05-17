package com.jspxcms.ext.web.back;

import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.Context;
import com.jspxcms.ext.domain.SiteGuestbook;

/**
 * GuestbookConfController
 * 
 * @author yangxing
 * 
 */
@Controller
@RequestMapping("/ext/guestbook_conf")
public class GuestbookConfController {
	private static final Logger logger = LoggerFactory
			.getLogger(GuestbookConfController.class);

	@RequiresPermissions("ext:guestbook_conf:edit")
	@RequestMapping("edit.do")
	public String edit(HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		SiteGuestbook siteGuestbook = new SiteGuestbook(site.getCustoms());
		modelMap.addAttribute("bean", siteGuestbook);
		return "ext/guestbook_conf/guestbook_conf";
	}

	@RequiresPermissions("ext:guestbook_conf:update")
	@RequestMapping("update.do")
	public String update(SiteGuestbook bean, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		siteService.updateConf(site, bean);
		logService.operation("opr.guestbookConf.edit", site.getName(), null,
				site.getId(), request);
		logger.info("update Guestbook config.");
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:edit.do";
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private SiteService siteService;
}

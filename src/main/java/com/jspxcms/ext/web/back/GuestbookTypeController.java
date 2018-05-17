package com.jspxcms.ext.web.back;

import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.List;

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
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;
import com.jspxcms.ext.domain.GuestbookType;
import com.jspxcms.ext.service.GuestbookTypeService;

/**
 * GuestbookTypeController
 * 
 * @author yangxing
 * 
 */
@Controller
@RequestMapping("/ext/guestbook_type")
public class GuestbookTypeController {
	private static final Logger logger = LoggerFactory
			.getLogger(GuestbookTypeController.class);

	@RequiresPermissions("ext:guestbook_type:list")
	@RequestMapping("list.do")
	public String list(HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		List<GuestbookType> list = service.findList(siteId);
		modelMap.addAttribute("list", list);
		return "ext/guestbook_type/guestbook_type_list";
	}

	@RequiresPermissions("ext:guestbook_type:save")
	@RequestMapping("save.do")
	public String save(GuestbookType bean, String redirect,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		service.save(bean, siteId);
		logService.operation("opr.guestbookType.add", bean.getName(), null,
				bean.getId(), request);
		logger.info("save GuestbookType, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("ext:guestbook_type:batch_update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer[] id, String[] name, String[] number,
			String[] description, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(id, site.getId());
		GuestbookType[] beans = service.batchUpdate(id, name, number,
				description);
		for (GuestbookType bean : beans) {
			logService.operation("opr.guestbookType.batchEdit", bean.getName(),
					null, bean.getId(), request);
			logger.info("update GuestbookType, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("ext:guestbook_type:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		GuestbookType[] beans = service.delete(ids);
		for (GuestbookType bean : beans) {
			logService.operation("opr.guestbookType.delete", bean.getName(),
					null, bean.getId(), request);
			logger.info("delete GuestbookType, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	private void validateIds(Integer[] ids, Integer siteId) {
		for (Integer id : ids) {
			Backends.validateDataInSite(service.get(id), siteId);
		}
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private GuestbookTypeService service;
}

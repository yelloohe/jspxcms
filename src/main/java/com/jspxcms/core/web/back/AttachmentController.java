package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.MESSAGE;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Attachment;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.AttachmentService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;

@Controller
@RequestMapping("/core/attachment")
public class AttachmentController {
	private static final Logger logger = LoggerFactory
			.getLogger(AttachmentController.class);

	@RequiresPermissions("core:attachment:list")
	@RequestMapping("list.do")
	public String list(
			@RequestParam(defaultValue = "false") boolean queryUsed,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		Integer siteId = Context.getCurrentSiteId();
		Page<Attachment> pagedList = service.findAll(siteId, queryUsed, params,
				pageable);
		modelMap.addAttribute("queryUsed", queryUsed);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/attachment/attachment_list";
	}

	@RequiresPermissions("core:attachment:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids,
			@RequestParam(defaultValue = "false") boolean queryUsed,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateDelete(ids, site.getId());
		List<Attachment> beans = service.delete(ids);
		for (Attachment bean : beans) {
			logService.operation("opr.attachment.delete", bean.getName(), null,
					bean.getId(), request);
			logger.info("delete Attachment, name={}.", bean.getName());
		}
		ra.addAttribute("queryUsed", queryUsed);
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	private void validateDelete(Integer[] ids, Integer siteId) {
		for (Integer id : ids) {
			Attachment bean = service.get(id);
			Backends.validateDataInSite(bean, siteId);
			if (bean.isUsed()) {
				throw new CmsException("attachment.error.used");
			}
		}
	}

	@ModelAttribute("bean")
	public Attachment preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private AttachmentService service;
}

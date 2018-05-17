package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPERATION_SUCCESS;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Global;
import com.jspxcms.core.domain.GlobalMail;
import com.jspxcms.core.domain.GlobalOther;
import com.jspxcms.core.domain.GlobalRegister;
import com.jspxcms.core.domain.GlobalUpload;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Model;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.GlobalService;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.ModelService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.OrgService;
import com.jspxcms.core.service.PublishPointService;
import com.jspxcms.core.service.SiteService;

/**
 * ConfGlobalController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/conf_global")
public class ConfGlobalController {
	public static final String TYPE = "type";

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:base_edit")
	@RequestMapping("base_edit.do")
	public String baseEdit(org.springframework.ui.Model modelMap) {
		List<PublishPoint> uploadsPublishPointList = publishPointService.findByType(PublishPoint.TYPE_UPLOAD);
		List<Site> siteList = siteService.findList();
		modelMap.addAttribute("uploadsPublishPointList", uploadsPublishPointList);
		modelMap.addAttribute("siteList", siteList);
		modelMap.addAttribute(TYPE, "base");
		return "core/conf_global/conf_global_base";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:base_update")
	@RequestMapping("base_update.do")
	public String baseUpdate(@ModelAttribute("bean") Global bean, Integer uploadsPublishPointId,
			HttpServletRequest request, RedirectAttributes ra) {
		service.update(bean, uploadsPublishPointId);
		logService.operation("opr.confGlobal.baseEdit", null, null, 1, request);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:base_edit.do";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:custom_edit")
	@RequestMapping("custom_edit.do")
	public String customEdit(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Model model = modelService.findDefault(null, Global.MODEL_TYPE);
		modelMap.addAttribute("model", model);
		modelMap.addAttribute(TYPE, "custom");
		return "core/conf_global/conf_global_custom";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:custom_update")
	@RequestMapping("custom_update.do")
	public String customUpdate(@ModelAttribute("bean") Global bean, HttpServletRequest request, RedirectAttributes ra) {
		Map<String, String> map = Servlets.getParamMap(request, "customs_");
		Map<String, String> clobMap = Servlets.getParamMap(request, "clobs_");
		service.updateCustoms(bean, map, clobMap);
		logService.operation("opr.confGlobal.customEdit", null, null, 1, request);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:custom_edit.do";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:upload_edit")
	@RequestMapping("upload_edit.do")
	public String uploadEdit(org.springframework.ui.Model modelMap) {
		modelMap.addAttribute(TYPE, "upload");
		return "core/conf_global/conf_global_upload";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:upload_update")
	@RequestMapping("upload_update.do")
	public String uploadUpdate(GlobalUpload bean, HttpServletRequest request, RedirectAttributes ra) {
		service.updateConf(bean);
		logService.operation("opr.confGlobal.uploadEdit", null, null, 1, request);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:upload_edit.do";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:register_edit")
	@RequestMapping("register_edit.do")
	public String registerEdit(org.springframework.ui.Model modelMap) {
		List<MemberGroup> groupList = memberGroupService.findRealGroups();
		List<Org> orgList = orgService.findList();
		modelMap.addAttribute("groupList", groupList);
		modelMap.addAttribute("orgList", orgList);
		modelMap.addAttribute(TYPE, "register");
		return "core/conf_global/conf_global_register";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:register_update")
	@RequestMapping("register_update.do")
	public String registerUpdate(GlobalRegister bean, HttpServletRequest request, RedirectAttributes ra) {
		service.updateConf(bean);
		logService.operation("opr.confGlobal.registerEdit", null, null, 1, request);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:register_edit.do";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:mail_edit")
	@RequestMapping("mail_edit.do")
	public String mailEdit(org.springframework.ui.Model modelMap) {
		modelMap.addAttribute(TYPE, "mail");
		return "core/conf_global/conf_global_mail";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:mail_update")
	@RequestMapping("mail_update.do")
	public String mailUpdate(GlobalMail bean, HttpServletRequest request, RedirectAttributes ra) {
		service.updateConf(bean);
		logService.operation("opr.confGlobal.mailEdit", null, null, 1, request);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:mail_edit.do";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:mail_send")
	@RequestMapping("mail_send.do")
	public String mailSend(HttpServletRequest request, RedirectAttributes ra) {
		String to = Servlets.getParam(request, "to");
		String subject = Servlets.getParam(request, "subject");
		String text = Servlets.getParam(request, "text");
		Global global = service.findUnique();
		GlobalMail mail = global.getMail();
		mail.sendMail(new String[] { to }, subject, text);
		logService.operation("opr.confGlobal.mailSend", to, null, 1, request);
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:mail_edit.do";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:other_edit")
	@RequestMapping("other_edit.do")
	public String otherEdit(org.springframework.ui.Model modelMap) {
		modelMap.addAttribute(TYPE, "other");
		return "core/conf_global/conf_global_other";
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:conf_global:other_update")
	@RequestMapping("other_update.do")
	public String otherUpdate(GlobalOther bean, HttpServletRequest request, RedirectAttributes ra) {
		service.updateConf(bean);
		logService.operation("opr.confGlobal.otherEdit", null, null, 1, request);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:other_edit.do";
	}

	@ModelAttribute("bean")
	public Global preloadBean() {
		return service.findUnique();
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("version");
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private PublishPointService publishPointService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MemberGroupService memberGroupService;
	@Autowired
	private GlobalService service;
}

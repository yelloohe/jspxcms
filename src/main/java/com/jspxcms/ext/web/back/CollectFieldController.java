package com.jspxcms.ext.web.back;

import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.util.Reflections;
import com.jspxcms.core.domain.Model;
import com.jspxcms.core.domain.ModelField;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.Context;
import com.jspxcms.ext.domain.Collect;
import com.jspxcms.ext.domain.CollectField;
import com.jspxcms.ext.service.CollectFieldService;
import com.jspxcms.ext.service.CollectService;

@Controller
@RequestMapping("/ext/collect_field")
public class CollectFieldController {
	private static final Logger logger = LoggerFactory
			.getLogger(CollectFieldController.class);

	public static final String[] EXCLUDE_FIELDS = { "node", "specials",
			"nodes", "viewGroups", "allowComment", "infoTemplate",
			"attributes", "priority", "tagKeywords", "color", "infoPath",
			"doc", "files", "images" };

	public static final String[] INCLUDE_FIELDS = { "id", "next", "views",
			"downloads" };

	@RequiresPermissions("ext:collect_field:list")
	@RequestMapping("list.do")
	public String list(Integer collectId, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Collect collect = collectService.get(collectId);
		Backends.validateDataInSite(collect, site.getId());
		List<CollectField> list = collect.getFields();
		modelMap.addAttribute("collect", collect);
		modelMap.addAttribute("list", list);
		return "ext/collect_field/collect_field_list";
	}

	@RequiresPermissions("ext:collect_field:create")
	@RequestMapping("create.do")
	public String create(Integer collectId, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Collect collect = collectService.get(collectId);
		Backends.validateDataInSite(collect, site.getId());
		List<CollectField> collectFields = collect.getFields();
		Model model = collect.getNode().getInfoModel();
		List<ModelField> modelFields = model.getEnabledFields();
		List<Map<String, String>> list = getFields(modelFields, collectFields);
		modelMap.addAttribute("collect", collect);
		modelMap.addAttribute("list", list);
		return "ext/collect_field/collect_field_form";
	}

	@RequiresPermissions("ext:collect_field:save")
	@RequestMapping("save.do")
	public String save(Integer collectId, String[] code, String[] name,
			Integer[] type, HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Collect collect = collectService.get(collectId);
		Backends.validateDataInSite(collect, siteId);
		List<CollectField> beans = service.save(code, name, type, collectId,
				siteId);
		for (CollectField bean : beans) {
			logService.operation("opr.collectField.add", bean.getName(), null,
					bean.getId(), request);
			logger.info("save CollectField, name={}.", bean.getName());
		}
		ra.addAttribute("collectId", collectId);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("ext:collect_field:update")
	@RequestMapping("update.do")
	public String update(Integer collectId, Integer[] id, String[] name,
			Integer[] sourceType, String[] sourceText, String[] sourceUrl,
			String[] dataPattern, Boolean[] dataReg, String[] dataAreaPattern,
			Boolean[] dataAreaReg, String[] filter, String[] downloadType,
			String[] imageParam, String[] dateFormat,
			HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Collect collect = collectService.get(collectId);
		Backends.validateDataInSite(collect, siteId);
		List<CollectField> beans = service.update(id, sourceType, sourceText,
				sourceUrl, dataPattern, dataReg, dataAreaPattern, dataAreaReg,
				filter, downloadType, imageParam, dateFormat);
		for (CollectField bean : beans) {
			logService.operation("opr.collectField.edit", bean.getName(), null,
					bean.getId(), request);
		}
		logger.info("update CollectField, id={}.", StringUtils.join(id, ','));
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addAttribute("collectId", collectId);
		return "redirect:list.do";
	}

	@RequiresPermissions("ext:collect_field:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, Integer collectId,
			HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		CollectField[] beans = service.delete(ids);
		for (CollectField bean : beans) {
			logService.operation("opr.collectField.delete", bean.getName(),
					null, bean.getId(), request);
			logger.info("delete CollectField, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		ra.addAttribute("collectId", collectId);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public CollectField preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	private static List<Map<String, String>> getFields(
			List<ModelField> modelFields, List<CollectField> collectFields) {
		List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
		List<Object> codes = Reflections.getPropertyList(collectFields, "code");
		Map<String, String> map;
		for (String code : INCLUDE_FIELDS) {
			if (!codes.contains(code)) {
				map = new HashMap<String, String>();
				map.put("code", code);
				map.put("type", String.valueOf(CollectField.TYPE_SYSTEM));
				fields.add(map);
			}
		}
		for (ModelField f : modelFields) {
			String name = f.getName();
			if (!ArrayUtils.contains(EXCLUDE_FIELDS, name)
					&& !codes.contains(name)) {
				map = new HashMap<String, String>();
				map.put("code", f.getName());
				map.put("name", f.getLabel());
				int type;
				if (f.isPredefined()) {
					type = CollectField.TYPE_SYSTEM;
				} else if (f.isClob()) {
					type = CollectField.TYPE_CLOB;
				} else {
					type = CollectField.TYPE_CUSTOM;
				}
				map.put("type", String.valueOf(type));
				fields.add(map);
			}
		}
		return fields;
	}

	private void validateIds(Integer[] ids, Integer siteId) {
		for (Integer id : ids) {
			Backends.validateDataInSite(service.get(id), siteId);
		}
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private CollectService collectService;
	@Autowired
	private CollectFieldService service;
}

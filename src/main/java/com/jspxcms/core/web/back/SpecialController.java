package com.jspxcms.core.web.back;

import static com.jspxcms.core.constant.Constants.CREATE;
import static com.jspxcms.core.constant.Constants.DELETE_SUCCESS;
import static com.jspxcms.core.constant.Constants.EDIT;
import static com.jspxcms.core.constant.Constants.MESSAGE;
import static com.jspxcms.core.constant.Constants.OPRT;
import static com.jspxcms.core.constant.Constants.SAVE_SUCCESS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.jspxcms.core.domain.Model;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.Special;
import com.jspxcms.core.domain.SpecialCategory;
import com.jspxcms.core.domain.SpecialFile;
import com.jspxcms.core.domain.SpecialImage;
import com.jspxcms.core.service.ModelService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.SpecialCategoryService;
import com.jspxcms.core.service.SpecialService;
import com.jspxcms.core.support.Backends;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;

/**
 * SpecialController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/special")
public class SpecialController {
	private static final Logger logger = LoggerFactory
			.getLogger(SpecialController.class);

	@RequiresPermissions("core:special:list")
	@RequestMapping("list.do")
	public String list(
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		Page<Special> pagedList = service.findAll(siteId, params, pageable);
		modelMap.addAttribute("pagedList", pagedList);

		List<SpecialCategory> categoryList = specialCategoryService
				.findList(siteId);
		modelMap.addAttribute("categoryList", categoryList);
		return "core/special/special_list";
	}

	@RequiresPermissions("core:special:create")
	@RequestMapping("create.do")
	public String create(Integer id, Integer modelId, Integer categoryId,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Model model = null;
		SpecialCategory category = null;
		if (id != null) {
			Special bean = service.get(id);
			Backends.validateDataInSite(bean, siteId);
			if (bean != null) {
				model = bean.getModel();
				category = bean.getCategory();
			}
			modelMap.addAttribute("bean", bean);
		}
		if (categoryId != null) {
			category = specialCategoryService.get(categoryId);
			Backends.validateDataInSite(category, siteId);
		}
		if (modelId != null) {
			model = modelService.get(modelId);
		}
		if (model == null) {
			model = modelService.findDefault(siteId, Special.MODEL_TYPE);
		}
		if (model == null) {
			throw new CmsException("special.error.modelNotFound");
		}
		List<SpecialCategory> categoryList = specialCategoryService
				.findList(siteId);
		List<Model> modelList = modelService.findList(siteId,
				Special.MODEL_TYPE);
		modelMap.addAttribute("categoryList", categoryList);
		modelMap.addAttribute("category", category);
		modelMap.addAttribute("model", model);
		modelMap.addAttribute("modelList", modelList);

		modelMap.addAttribute(OPRT, CREATE);
		return "core/special/special_form";
	}

	@RequiresPermissions("core:special:edit")
	@RequestMapping("edit.do")
	public String edit(
			Integer id,
			Integer modelId,
			Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Special bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		Model model = bean.getModel();
		if (modelId != null) {
			model = modelService.get(modelId);
		}
		Map<String, String[]> params = Servlets.getParamValuesMap(request,
				Constants.SEARCH_PREFIX);
		RowSide<Special> side = service.findSide(siteId, params, bean,
				position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);

		List<SpecialCategory> categoryList = specialCategoryService
				.findList(siteId);
		List<Model> modelList = modelService.findList(siteId,
				Special.MODEL_TYPE);
		modelMap.addAttribute("categoryList", categoryList);
		modelMap.addAttribute("category", bean.getCategory());
		modelMap.addAttribute("modelList", modelList);
		modelMap.addAttribute("model", model);

		modelMap.addAttribute(OPRT, EDIT);
		return "core/special/special_form";
	}

	@RequiresPermissions("core:special:save")
	@RequestMapping("save.do")
	public String save(Special bean, Integer categoryId, Integer modelId,
			String[] imagesName, String[] imagesText, String[] imagesImage,
			String[] filesName, String[] filesFile, Long[] filesLength,
			String redirect, HttpServletRequest request, RedirectAttributes ra) {
		Integer siteId = Context.getCurrentSiteId();
		Integer userId = Context.getCurrentUserId();
		SpecialCategory category = specialCategoryService.get(categoryId);
		Backends.validateDataInSite(category, siteId); 
		Map<String, String> customs = Servlets.getParamMap(request,
				"customs_");
		Map<String, String> clobs = Servlets.getParamMap(request, "clobs_");
		List<SpecialImage> images = new ArrayList<SpecialImage>();
		if (imagesName != null) {
			SpecialImage specialImage;
			for (int i = 0, len = imagesName.length; i < len; i++) {
				if (StringUtils.isNotBlank(imagesName[i])
						|| StringUtils.isNotBlank(imagesText[i])
						|| StringUtils.isNotBlank(imagesImage[i])) {
					specialImage = new SpecialImage(imagesName[i],
							imagesText[i], imagesImage[i]);
					images.add(specialImage);
				}
			}
		}
		List<SpecialFile> files = new ArrayList<SpecialFile>();
		if (filesName != null) {
			SpecialFile specialFile;
			for (int i = 0, len = filesFile.length; i < len; i++) {
				if (StringUtils.isNotBlank(filesName[i])
						&& StringUtils.isNotBlank(filesFile[i])) {
					specialFile = new SpecialFile(filesName[i], filesFile[i],
							filesLength[i]);
					files.add(specialFile);
				}
			}
		}
		service.save(bean, categoryId, modelId, userId, customs, clobs, images,
				files, siteId);
		logService.operation("opr.special.add", bean.getTitle(), null,
				bean.getId(), request);
		logger.info("save Special, title={}.", bean.getTitle());
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

	@RequiresPermissions("core:special:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Special bean,
			Integer categoryId, Integer modelId, String[] imagesName,
			String[] imagesText, String[] imagesImage, String[] filesName,
			String[] filesFile, Long[] filesLength, Integer position,
			String redirect, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		SpecialCategory category = specialCategoryService.get(categoryId);
		Backends.validateDataInSite(category, site.getId());
		Backends.validateDataInSite(bean, site.getId());
		Map<String, String> customs = Servlets.getParamMap(request,
				"customs_");
		Map<String, String> clobs = Servlets.getParamMap(request, "clobs_");
		List<SpecialImage> images = new ArrayList<SpecialImage>();
		if (imagesName != null) {
			SpecialImage specialImage;
			for (int i = 0, len = imagesName.length; i < len; i++) {
				if (StringUtils.isNotBlank(imagesName[i])
						|| StringUtils.isNotBlank(imagesText[i])
						|| StringUtils.isNotBlank(imagesImage[i])) {
					specialImage = new SpecialImage(imagesName[i],
							imagesText[i], imagesImage[i]);
					images.add(specialImage);
				}
			}
		}
		List<SpecialFile> files = new ArrayList<SpecialFile>();
		if (filesName != null) {
			SpecialFile specialFile;
			for (int i = 0, len = filesFile.length; i < len; i++) {
				if (StringUtils.isNotBlank(filesName[i])
						&& StringUtils.isNotBlank(filesFile[i])) {
					specialFile = new SpecialFile(filesName[i], filesFile[i],
							filesLength[i]);
					files.add(specialFile);
				}
			}
		}
		service.update(bean, categoryId, modelId, customs, clobs, images, files);
		logService.operation("opr.special.edit", bean.getTitle(), null,
				bean.getId(), request);
		logger.info("update Special, title={}.", bean.getTitle());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:special:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Special[] beans = service.delete(ids);
		for (Special bean : beans) {
			logService.operation("opr.special.delete", bean.getTitle(), null,
					bean.getId(), request);
			logger.info("delete Special, title={}.", bean.getTitle());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@ModelAttribute("bean")
	public Special preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	private void validateIds(Integer[] ids, Integer siteId) {
		for (Integer id : ids) {
			Backends.validateDataInSite(service.get(id), siteId);
		}
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private SpecialCategoryService specialCategoryService;
	@Autowired
	private SpecialService service;
}

package com.jspxcms.core.web.back;

import com.jspxcms.common.file.*;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.common.web.Validations;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.support.CmsException;
import com.jspxcms.core.support.Context;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.jspxcms.core.constant.Constants.*;

/**
 * WebFileControllerAbstractor
 * 
 * @author liufang
 * 
 */
@Controller
public abstract class WebFileControllerAbstractor {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static final int TEMPLATE = 1;
	public static final int UPLOAD = 2;
	public static final int SITE = 3;

	public static final CommonFileFilter dirFilter = new DirCommonFileFilter();

	protected abstract int getType();

	protected abstract String getBase(Site site);

	protected abstract String getDefPath(Site site);

	protected abstract String getUrlPrefix(Site site);

	protected abstract FileHandler getFileHandler(Site site);

	protected abstract boolean isFtp(Site site);

	protected String left(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		Site site = Context.getCurrentSite();
		String base = getBase(site);
		String urlPrefix = getUrlPrefix(site);
		FileHandler fileHandler = getFileHandler(site);
		CommonFile parent = new CommonFile(base, true);
		List<CommonFile> list = fileHandler.listFiles(dirFilter, base, urlPrefix);
		modelMap.addAttribute("parent", parent);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("type", getType());
		modelMap.addAttribute("isFtp", isFtp(site));
		return "core/web_file/web_file_left";
	}

	protected String leftTree(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		Site site = Context.getCurrentSite();
		String parentId = Servlets.getParam(request, "parentId");
		parentId = parentId == null ? "" : parentId;
		String base = getBase(site);
		String urlPrefix = getUrlPrefix(site);
		if (StringUtils.isBlank(parentId)) {
			parentId = base;
		}
		if (!Validations.uri(parentId, base)) {
			throw new CmsException("invalidURI");
		}
		FileHandler fileHandler = getFileHandler(site);
		List<CommonFile> list = fileHandler.listFiles(dirFilter, parentId, urlPrefix);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("type", getType());
		modelMap.addAttribute("isFtp", isFtp(site));
		return "core/web_file/web_file_left_tree";
	}

	protected String list(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		Site site = Context.getCurrentSite();
		String parentId = Servlets.getParam(request, "parentId");
		parentId = parentId == null ? "" : parentId;
		String searchName = Servlets.getParam(request, "search_name");
		String base = getBase(site);
		String defPath = getDefPath(site);
		String urlPrefix = getUrlPrefix(site);
		if (StringUtils.isBlank(parentId)) {
			parentId = defPath;
		}
		if (!Validations.uri(parentId, base)) {
			throw new CmsException("invalidURI");
		}
		FileHandler fileHandler = getFileHandler(site);
		CommonFile pp = null;
		if (parentId.length() > base.length()) {
			pp = new CommonFile(CommonFile.getParent(parentId), true);
		}
		List<CommonFile> list = fileHandler.listFiles(searchName, parentId, urlPrefix);
		String sort = request.getParameter("page_sort");
		String directionection = request.getParameter("page_sort_dir");
		CommonFile.sort(list, sort, directionection);
		if (pp != null) {
			pp.setParent(true);
			list.add(0, pp);
			modelMap.addAttribute("ppId", pp.getId());
		}
		modelMap.addAttribute("parentId", parentId);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("type", getType());
		modelMap.addAttribute("isFtp", isFtp(site));
		return "core/web_file/web_file_list";
	}

	protected String create(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		Site site = Context.getCurrentSite();
		String parentId = Servlets.getParam(request, "parentId");
		String base = getBase(site);
		if (StringUtils.isBlank(parentId)) {
			parentId = base;
		}
		if (!Validations.uri(parentId, base)) {
			throw new CmsException("invalidURI");
		}
		String cid = Servlets.getParam(request, "cid");
		if (StringUtils.isNotBlank(cid)) {
			if (!Validations.uri(cid, base)) {
				throw new CmsException("invalidURI");
			}
			FileHandler fileHandler = getFileHandler(site);

			String urlPrefix = getUrlPrefix(site);
			CommonFile bean = fileHandler.get(cid, urlPrefix);
			modelMap.addAttribute("bean", bean);
		}

		modelMap.addAttribute("parentId", parentId);
		modelMap.addAttribute("type", getType());
		modelMap.addAttribute("isFtp", isFtp(site));
		modelMap.addAttribute(OPRT, CREATE);
		return "core/web_file/web_file_form";
	}

	protected String edit(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		Site site = Context.getCurrentSite();
		String id = Servlets.getParam(request, "id");
		String base = getBase(site);

		String urlPrefix = getUrlPrefix(site);
		if (!Validations.uri(id, base)) {
			throw new CmsException("invalidURI");
		}
		FileHandler fileHandler = getFileHandler(site);
		CommonFile bean = fileHandler.get(id, urlPrefix);

		String parentId = Servlets.getParam(request, "parentId");
		modelMap.addAttribute("parentId", parentId);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute(OPRT, EDIT);
		modelMap.addAttribute("type", getType());
		modelMap.addAttribute("isFtp", isFtp(site));
		return "core/web_file/web_file_form";
	}

	protected String mkdir(String parentId, String dir, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes ra) throws IOException {
		Site site = Context.getCurrentSite();
		parentId = parentId == null ? "" : parentId;
		String base = getBase(site);

		if (StringUtils.isBlank(parentId)) {
			parentId = base;
		}
		if (!Validations.uri(parentId, base)) {
			throw new CmsException("invalidURI");
		}
		FileHandler fileHandler = getFileHandler(site);
		boolean success = fileHandler.mkdir(dir, parentId);
		if (success) {
			logService.operation("opr.role.add", parentId + "/" + dir, null, null, request);
			logger.info("mkdir file, name={}.", parentId + "/" + dir);
		}
		ra.addFlashAttribute("refreshLeft", true);
		ra.addAttribute("parentId", parentId);
		ra.addFlashAttribute(MESSAGE, success ? OPERATION_SUCCESS : OPERATION_FAILURE);
		return "redirect:list.do";
	}

	protected String save(String parentId, String name, String text, String redirect, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes ra) throws IOException {
		Site site = Context.getCurrentSite();
		String base = getBase(site);

		if (!Validations.uri(parentId, base)) {
			throw new CmsException("invalidURI");
		}
		FileHandler fileHandler = getFileHandler(site);
		fileHandler.store(text, name, parentId);
		logService.operation("opr.webFile.add", parentId + "/" + name, null, null, request);
		logger.info("save file, name={}.", parentId + "/" + name);
		ra.addFlashAttribute("refreshLeft", true);
		ra.addAttribute("parentId", parentId);
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else if (Constants.REDIRECT_CREATE.equals(redirect)) {
			return "redirect:create.do";
		} else {
			ra.addAttribute("id", parentId + "/" + name);
			return "redirect:edit.do";
		}
	}

	protected void update(String parentId, String origName, String name, String text, Integer position,
			String redirect, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Site site = Context.getCurrentSite();
		String base = getBase(site);

		if (!Validations.uri(parentId + "/" + origName, base)) {
			throw new CmsException("invalidURI");
		}

		FileHandler fileHandler = getFileHandler(site);
		if (StringUtils.isNotBlank(text)) {
			fileHandler.store(text, origName, parentId);
		}
		if (!origName.equals(name)) {
			fileHandler.rename(name, parentId + "/" + origName);
		}
		logService.operation("opr.webFile.edit", parentId + "/" + origName, null, null, request);
		logger.info("update file, name={}.", parentId + "/" + origName);
		Servlets.writeHtml(response, "true");
	}

	protected String delete(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		Site site = Context.getCurrentSite();
		String[] ids = Servlets.getParamValues(request, "ids");
		String base = getBase(site);

		FileHandler fileHandler = getFileHandler(site);
		for (int i = 0, len = ids.length; i < len; i++) {
			if (!Validations.uri(ids[i], base)) {
				throw new CmsException("invalidURI");
			}
		}
		boolean success = fileHandler.delete(ids);
		if (success) {
			for (String id : ids) {
				logService.operation("opr.webFile.delete", id, null, null, request);
			}
			logger.info("delete file success, name={}.", StringUtils.join(ids, ','));
		} else {
			logger.info("delete file failure, name={}.", StringUtils.join(ids, ','));
		}
		String parentId = Servlets.getParam(request, "parentId");
		ra.addAttribute("parentId", parentId);
		ra.addFlashAttribute("refreshLeft", true);
		ra.addFlashAttribute(MESSAGE, success ? DELETE_SUCCESS : OPERATION_FAILURE);
		return "redirect:list.do";
	}

	protected String rename(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		Site site = Context.getCurrentSite();
		String id = Servlets.getParam(request, "id");
		String name = Servlets.getParam(request, "name");
		String base = getBase(site);

		FileHandler fileHandler = getFileHandler(site);
		if (!Validations.uri(id, base)) {
			throw new CmsException("invalidURI");
		}
		boolean success = fileHandler.rename(name, id);
		if (success) {
			logService.operation("opr.webFile.rename", id, null, null, request);
			logger.info("rename file success, name={}.", id);
		}
		String parentId = Servlets.getParam(request, "parentId");
		ra.addAttribute("parentId", parentId);
		ra.addFlashAttribute("refreshLeft", true);
		ra.addFlashAttribute(MESSAGE, success ? OPERATION_SUCCESS : OPERATION_FAILURE);
		return "redirect:list.do";
	}

	protected String move(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		Site site = Context.getCurrentSite();
		String[] ids = Servlets.getParamValues(request, "ids");
		String dest = Servlets.getParam(request, "dest");
		String base = getBase(site);

		if (!Validations.uri(dest, base)) {
			throw new CmsException("invalidURI");
		}
		for (int i = 0, len = ids.length; i < len; i++) {
			if (!Validations.uri(ids[i], base)) {
				throw new CmsException("invalidURI");
			}
		}
		FileHandler fileHandler = getFileHandler(site);
		fileHandler.move(dest, ids);
		for (String id : ids) {
			logService.operation("opr.webFile.move", id, null, null, request);
		}
		logger.info("move file success, name={}.", StringUtils.join(ids, ','));

		String parentId = Servlets.getParam(request, "parentId");
		ra.addAttribute("parentId", parentId);
		ra.addFlashAttribute("refreshLeft", true);
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	protected String zip(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		Site site = Context.getCurrentSite();
		FileHandler fileHandler = getFileHandler(site);
		if (!(fileHandler instanceof LocalFileHandler)) {
			throw new CmsException("ftp cannot support ZIP.");
		}
		LocalFileHandler localFileHandler = (LocalFileHandler) fileHandler;
		String parentId = Servlets.getParam(request, "parentId");
		String[] ids = Servlets.getParamValues(request, "ids");
		String base = getBase(site);

		File[] files = new File[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			if (!Validations.uri(ids[i], base)) {
				throw new CmsException("invalidURI");
			}
			files[i] = localFileHandler.getFile(ids[i]);
		}
		AntZipUtils.zip(files);
		ra.addAttribute("parentId", parentId);
		ra.addFlashAttribute("refreshLeft", true);
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	protected void zipDownload(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		Site site = Context.getCurrentSite();
		FileHandler fileHandler = getFileHandler(site);
		if (!(fileHandler instanceof LocalFileHandler)) {
			throw new CmsException("ftp cannot support ZIP.");
		}
		LocalFileHandler localFileHandler = (LocalFileHandler) fileHandler;

		String[] ids = Servlets.getParamValues(request, "ids");
		String base = getBase(site);

		File[] files = new File[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			if (!Validations.uri(ids[i], base)) {
				throw new CmsException("invalidURI");
			}
			files[i] = localFileHandler.getFile(ids[i]);
		}
		response.setContentType("application/x-download;charset=UTF-8");
		response.addHeader("Content-disposition", "filename=download_files.zip");
		try {
			AntZipUtils.zip(files, response.getOutputStream());
		} catch (IOException e) {
			logger.error("zip error!", e);
		}
	}

	protected String unzip(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		Site site = Context.getCurrentSite();
		FileHandler fileHandler = getFileHandler(site);
		if (!(fileHandler instanceof LocalFileHandler)) {
			throw new CmsException("ftp cannot support ZIP.");
		}
		LocalFileHandler localFileHandler = (LocalFileHandler) fileHandler;

		String base = getBase(site);
		String[] ids = Servlets.getParamValues(request, "ids");
		for (int i = 0, len = ids.length; i < len; i++) {
			if (!Validations.uri(ids[i], base)) {
				throw new CmsException("invalidURI");
			}
			File file = localFileHandler.getFile(ids[i]);
			if (AntZipUtils.isZipFile(file)) {
				AntZipUtils.unzip(file, file.getParentFile());
				logService.operation("opr.webFile.unzip", ids[i], null, null, request);
				logger.info("unzip file, name={}.", ids[i]);
			}
		}

		String parentId = Servlets.getParam(request, "parentId");
		ra.addAttribute("parentId", parentId);
		ra.addFlashAttribute("refreshLeft", true);
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	protected void upload(MultipartFile file, String parentId, HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {
		Site site = Context.getCurrentSite();
		// parentId = parentId == null ? base : parentId;
		String base = getBase(site);
		if (!Validations.uri(parentId, base)) {
			throw new CmsException("invalidURI", parentId);
		}
		FileHandler fileHandler = getFileHandler(site);
		fileHandler.store(file, parentId);
		logService.operation("opr.webFile.upload", parentId + "/" + file.getOriginalFilename(), null, null, request);
		logger.info("upload file, name={}.", parentId + "/" + file.getOriginalFilename());
		Servlets.writeHtml(response, "true");
	}

	protected void zipUpload(MultipartFile file, String parentId, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes ra) throws IOException {
		Site site = Context.getCurrentSite();
		FileHandler fileHandler = getFileHandler(site);
		if (!(fileHandler instanceof LocalFileHandler)) {
			throw new CmsException("ftp cannot support ZIP.");
		}
		LocalFileHandler localFileHandler = (LocalFileHandler) fileHandler;
		String base = getBase(site);
		// parentId = parentId == null ? base : parentId;

		if (!Validations.uri(parentId, base)) {
			throw new CmsException("invalidURI");
		}
		File parentFile = localFileHandler.getFile(parentId);
		File tempFile = FilesEx.getTempFile();
		file.transferTo(tempFile);
		AntZipUtils.unzip(tempFile, parentFile);
		tempFile.delete();

		logService.operation("opr.webFile.zipUpload", parentId + "/" + file.getOriginalFilename(), null, null, request);
		logger.info("zip upload file, name={}.", parentId + "/" + file.getOriginalFilename());
		Servlets.writeHtml(response, "true");
	}

	protected String dir(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		Site site = Context.getCurrentSite();
		String parentId = Servlets.getParam(request, "parentId");
		parentId = parentId == null ? "" : parentId;
		String base = getBase(site);
		String urlPrefix = getUrlPrefix(site);
		if (StringUtils.isBlank(parentId)) {
			parentId = base;
		}
		if (!Validations.uri(parentId, base)) {
			throw new CmsException("invalidURI");
		}

		// 需排除的文件夹
		final String[] ids = Servlets.getParamValues(request, "ids");
		CommonFileFilter filter = new CommonFileFilter() {
			public boolean accept(CommonFile file) {
				// 只显示文件夹，不显示文件
				if (file.isDirectory()) {
					String id = file.getId();
					for (int i = 0, len = ids.length; i < len; i++) {
						if (id.equals(ids[i]) || id.startsWith(ids[i] + "/")) {
							return false;
						}
					}
					return true;
				}
				return false;
			}
		};
		FileHandler fileHandler = getFileHandler(site);
		List<CommonFile> list = fileHandler.listFiles(filter, parentId, urlPrefix);
		// 设置当前目录
		CommonFile parent = new CommonFile(parentId, true);
		parent.setCurrent(true);
		list.add(0, parent);
		// 设置上级目录
		if (parentId.length() > base.length()) {
			CommonFile pp = new CommonFile(CommonFile.getParent(parentId), true);
			pp.setParent(true);
			list.add(0, pp);
			modelMap.addAttribute("ppId", pp.getId());
		}
		modelMap.addAttribute("ids", ids);
		modelMap.addAttribute("parentId", parentId);
		modelMap.addAttribute("list", list);
		Servlets.setNoCacheHeader(response);
		return "core/web_file/choose_dir";
	}

	protected String dirList(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		dir(request, response, modelMap);
		return "core/web_file/choose_dir_list";
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	protected PathResolver pathResolver;
}

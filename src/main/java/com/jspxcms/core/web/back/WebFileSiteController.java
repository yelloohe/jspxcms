package com.jspxcms.core.web.back;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.core.domain.Site;

/**
 * WebFileController
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/core/web_file_3")
public class WebFileSiteController extends WebFileControllerAbstractor {
	@Override
	protected int getType() {
		return SITE;
	}

	@Override
	protected String getBase(Site site) {
		return "";
	}

	@Override
	protected String getDefPath(Site site) {
		return getBase(site);
	}

	@Override
	protected String getUrlPrefix(Site site) {
		return site.getContextPath();
	}

	@Override
	protected FileHandler getFileHandler(Site site) {
		return FileHandler.getLocalFileHandler(pathResolver, "");
	}

	@Override
	protected boolean isFtp(Site site) {
		return false;
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:left")
	@RequestMapping("left.do")
	public String left(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.left(request, response, modelMap);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:left")
	@RequestMapping("left_tree.do")
	public String leftTree(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.leftTree(request, response, modelMap);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:list")
	@RequestMapping("list.do")
	public String list(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.list(request, response, modelMap);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:create")
	@RequestMapping("create.do")
	public String create(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.create(request, response, modelMap);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:edit")
	@RequestMapping("edit.do")
	public String edit(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.edit(request, response, modelMap);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:mkdir")
	@RequestMapping(value = "mkdir.do", method = RequestMethod.POST)
	public String mkdir(String parentId, String dir, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes ra) throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.mkdir(parentId, dir, request, response, ra);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:save")
	@RequestMapping(value = "save.do", method = RequestMethod.POST)
	public String save(String parentId, String name, String text, String redirect, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes ra) throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.save(parentId, name, text, redirect, request, response, ra);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:update")
	@RequestMapping(value = "update.do", method = RequestMethod.POST)
	public void update(String parentId, String origName, String name, String text, Integer position, String redirect,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		super.update(parentId, origName, name, text, position, redirect, request, response);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:delete")
	@RequestMapping("delete.do")
	public String delete(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.delete(request, response, ra);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:rename")
	@RequestMapping("rename.do")
	public String rename(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.rename(request, response, ra);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:move")
	@RequestMapping("move.do")
	public String move(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.move(request, response, ra);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:zip")
	@RequestMapping("zip.do")
	public String zip(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.zip(request, response, ra);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:zip_download")
	@RequestMapping("zip_download.do")
	public void zipDownload(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		super.zipDownload(request, response, ra);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:unzip")
	@RequestMapping("unzip.do")
	public String unzip(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.unzip(request, response, ra);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:upload")
	@RequestMapping("upload.do")
	public void upload(@RequestParam(value = "file", required = false) MultipartFile file, String parentId,
			HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		super.upload(file, parentId, request, response);
	}

	@RequiresRoles("super")
	@RequiresPermissions("core:web_file_3:zip_upload")
	@RequestMapping("zip_upload.do")
	public void zipUpload(@RequestParam(value = "file", required = false) MultipartFile file, String parentId,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra) throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		super.zipUpload(file, parentId, request, response, ra);
	}

	@RequiresRoles("super")
	@RequestMapping("choose_dir.do")
	protected String dir(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.dir(request, response, modelMap);
	}

	@RequiresRoles("super")
	@RequestMapping("choose_dir_list.do")
	public String dirList(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		if (!isEnableSiteFile()) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		return super.dirList(request, response, modelMap);
	}

	private boolean isEnableSiteFile() {
		return "true".equals(isEnableSiteFile);
	}

	@Value("${webFile.isEnableSiteFile}")
	private String isEnableSiteFile;
}

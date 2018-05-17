package com.jspxcms.core.web.back;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.file.FilesEx;
import com.jspxcms.common.file.LocalFileHandler;
import com.jspxcms.common.image.ImageHandler;
import com.jspxcms.common.image.WatermarkParam;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.UploadHandler;

/**
 * 图片裁剪Controller
 * 
 * @author liufang
 * 
 */
@Controller
@RequestMapping("/commons")
public class ImageCropController {
	@RequiresPermissions("commons:img_crop:select")
	@RequestMapping(value = "img_area_select.do")
	public String imgAreaSelect(String src, Integer targetWidth, Integer targetHeight, String targetFrame, String name,
			org.springframework.ui.Model modelMap) {
		String srcNoCache = src;
		if (StringUtils.isNotBlank(src)) {
			srcNoCache += src.indexOf("?") == -1 ? "?" : "&";
			srcNoCache += "d=" + System.currentTimeMillis();
		}
		modelMap.addAttribute("src", src);
		modelMap.addAttribute("srcNoCache", srcNoCache);
		modelMap.addAttribute("targetWidth", targetWidth);
		modelMap.addAttribute("targetHeight", targetHeight);
		modelMap.addAttribute("targetFrame", targetFrame);
		modelMap.addAttribute("name", name);
		return "commons/img_area_select";
	}

	@RequiresPermissions("commons:img_crop:submit")
	@RequestMapping(value = "img_crop.do")
	public String imgCrop(String src, Float scale, Integer left, Integer top, Integer width, Integer height,
			Integer targetWidth, Integer targetHeight, String name,
			@RequestParam(defaultValue = "false") boolean watermark, HttpServletRequest request,
			org.springframework.ui.Model modelMap) throws IOException {
		if (left == null || top == null || width == null || height == null) {
			return null;
		}
		Site site = Context.getCurrentSite();
		Integer userId = Context.getCurrentUserId();
		PublishPoint point = site.getUploadsPublishPoint();
		String urlPrefix = point.getUrlPrefix();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		if (!src.startsWith(urlPrefix)) {
			return null;
		}
		String id = src.substring(urlPrefix.length());
		String formatName = fileHandler.getFormatName(id);
		if (formatName == null) {
			return null;
		}
		String extension = FilenameUtils.getExtension(id);
		File srcFile = fileHandler.getFile(id);
		File tempFile = null;
		try {
			if (fileHandler instanceof LocalFileHandler) {
				// 本地文件需创建临时副本，防止原文件被修改
				tempFile = FilesEx.getTempFile(extension);
				FileUtils.copyFile(srcFile, tempFile);
			} else {
				// FtpFileHandler获取的文件本身就是临时文件
				tempFile = srcFile;
			}
			String tempPath = tempFile.getAbsolutePath();
			imageHandler.crop(tempPath, tempPath, left, top, width, height);
			WatermarkParam watermarkParam = site.getWatermark().getWatermarkParam(watermark);
			if (watermarkParam.isWatermark()) {
				String imagePath = watermarkParam.getImagePath();
				// 水印文件在模板中，必须是本地文件
				LocalFileHandler handler = FileHandler.getLocalFileHandler(pathResolver, Constants.TEMPLATE_STORE_PATH);
				File overlayFile = handler.getFile(imagePath);
				String overlayPath = overlayFile.getAbsolutePath();
				imageHandler.composite(overlayPath, tempPath, tempPath, watermarkParam);
			}
			String ip = Servlets.getRemoteAddr(request);
			String url = uploadHandler.storeImage(tempFile, extension, formatName, site, ip, userId);
			// 裁剪不删除原图片
			// fileHandler.delete(id);
			modelMap.addAttribute("name", name);
			modelMap.addAttribute("url", url);
			return "commons/img_crop";
		} finally {
			// 删除临时文件
			FileUtils.deleteQuietly(tempFile);
		}
	}

	@Autowired
	private ImageHandler imageHandler;
	@Autowired
	private UploadHandler uploadHandler;
	@Autowired
	private PathResolver pathResolver;

}

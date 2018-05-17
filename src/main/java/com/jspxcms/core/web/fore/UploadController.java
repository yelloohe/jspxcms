package com.jspxcms.core.web.fore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspxcms.common.upload.Uploader;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.SiteResolver;
import com.jspxcms.core.web.back.UploadControllerAbstract;

/**
 * UploadController
 * 
 * @author liufang
 * 
 */
@Controller
public class UploadController extends UploadControllerAbstract {
	@RequestMapping(value = "/upload_image", method = RequestMethod.POST)
	public void uploadImage(Boolean scale, Boolean exact, Integer width, Integer height, Boolean thumbnail,
			Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		uploadImage(null, scale, exact, width, height, thumbnail, thumbnailWidth, thumbnailHeight, watermark, request,
				response);
	}

	@RequestMapping(value = Constants.SITE_PREFIX_PATH + "/upload_image", method = RequestMethod.POST)
	public void uploadImage(@PathVariable String siteNumber, Boolean scale, Boolean exact, Integer width,
			Integer height, Boolean thumbnail, Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		upload(site, request, response, Uploader.IMAGE, scale, exact, width, height, thumbnail, thumbnailWidth,
				thumbnailHeight, watermark);
	}

	@RequestMapping(value = "/upload_flash", method = RequestMethod.POST)
	public void uploadFlash(HttpServletRequest request, HttpServletResponse response) throws IOException {
		uploadFlash(null, request, response);
	}

	@RequestMapping(value = Constants.SITE_PREFIX_PATH + "/upload_flash", method = RequestMethod.POST)
	public void uploadFlash(@PathVariable String siteNumber, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		upload(site, request, response, Uploader.FLASH);
	}

	@RequestMapping(value = "/upload_file", method = RequestMethod.POST)
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		uploadFile(null, request, response);
	}

	@RequestMapping(value = Constants.SITE_PREFIX_PATH + "/upload_file", method = RequestMethod.POST)
	public void uploadFile(@PathVariable String siteNumber, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		upload(site, request, response, Uploader.FILE);
	}

	@RequestMapping(value = "/upload_video", method = RequestMethod.POST)
	public void uploadVideo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		uploadVideo(null, request, response);
	}

	@RequestMapping(value = Constants.SITE_PREFIX_PATH + "/upload_video", method = RequestMethod.POST)
	public void uploadVideo(@PathVariable String siteNumber, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		upload(site, request, response, Uploader.VIDEO);
	}

	@RequestMapping(value = "/ueditor")
	public void ueditor(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ueditor(null, request, response);
	}

	@RequestMapping(value = Constants.SITE_PREFIX_PATH + "/ueditor")
	public void ueditor(@PathVariable String siteNumber, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String action = request.getParameter("action");
		if ("config".equals(action)) {
			super.ueditorConfig(request, response);
		} else if ("uploadimage".equals(action)) {
			uploadImage(siteNumber, null, null, null, null, null, null, null, null, request, response);
		} else if ("catchimage".equals(action)) {
			ueditorCatchImage(siteNumber, request, response);
		} else if ("uploadvideo".equals(action)) {
			uploadVideo(siteNumber, request, response);
		} else if ("uploadfile".equals(action)) {
			uploadFile(siteNumber, request, response);
		} else if ("uploadscrawl".equals(action)) {

		} else if ("listimage".equals(action)) {

		} else if ("listfile".equals(action)) {

		}
	}

	public void ueditorCatchImage(String siteNumber, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		super.ueditorCatchImage(site, request, response);
	}

	@Autowired
	private SiteResolver siteResolver;
}

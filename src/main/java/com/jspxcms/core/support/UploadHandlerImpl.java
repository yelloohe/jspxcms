package com.jspxcms.core.support;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.HttpHeaders;
import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.file.FilesEx;
import com.jspxcms.common.file.LocalFileHandler;
import com.jspxcms.common.image.ImageHandler;
import com.jspxcms.common.image.Images;
import com.jspxcms.common.image.ScaleParam;
import com.jspxcms.common.image.ThumbnailParam;
import com.jspxcms.common.image.WatermarkParam;
import com.jspxcms.common.upload.UploadResult;
import com.jspxcms.common.upload.Uploader;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.commercial.UploadDoc;
import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.domain.Global;
import com.jspxcms.core.domain.GlobalUpload;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.SiteWatermark;
import com.jspxcms.core.service.AttachmentService;

public class UploadHandlerImpl implements UploadHandler {
	protected final Logger logger = LoggerFactory.getLogger(UploadHandlerImpl.class);

	@Override
	public String copyImage(File src, String extension, String formatName, Site site, Boolean scale, Boolean exact,
			Integer width, Integer height, Boolean thumbnail, Integer thumbnailWidth, Integer thumbnailHeight,
			Boolean watermark, String ip, Integer userId, Integer siteId) {
		GlobalUpload gu = site.getGlobal().getUpload();
		ScaleParam scaleParam = gu.getScaleParam(scale, exact, width, height);
		scale = scaleParam.isScale();

		ThumbnailParam thumbnailParam = new ThumbnailParam(thumbnail, thumbnailWidth, thumbnailHeight);
		thumbnail = thumbnailParam.isThumbnail();

		SiteWatermark sw = site.getWatermark();
		WatermarkParam watermarkParam = sw.getWatermarkParam(watermark);
		watermark = watermarkParam.isWatermark();

		PublishPoint point = site.getUploadsPublishPoint();
		String urlPrefix = point.getUrlPrefix();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String pathname = UploadUtils.getUrl(site.getId(), Uploader.IMAGE, extension);
		try {
			File copy = FilesEx.getTempFile();
			try {
				FileUtils.copyFile(src, copy);
				storeImage(copy, scaleParam, thumbnailParam, watermarkParam, formatName, pathname, fileHandler, ip,
						userId, siteId);
				attachmentService.save(pathname, src.length(), ip, userId, siteId);
				return urlPrefix + pathname;
			} finally {
				FileUtils.deleteQuietly(copy);
			}
		} catch (IOException e) {
			logger.error(null, e);
			return null;
		}
	}

	@Override
	public String storeImage(File file, String extension, String formatName, Site site, String ip, Integer userId) {
		PublishPoint point = site.getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String urlPrefix = point.getUrlPrefix();
		String filename = UploadUtils.getUrl(site.getId(), Uploader.IMAGE, extension);
		try {
			fileHandler.storeFile(file, filename);
			long length = file.length();
			attachmentService.save(filename, length, ip, userId, site.getId());
			return urlPrefix + filename;
		} catch (IOException e) {
			logger.error(null, e);
			return null;
		}
	}

	@Override
	public String storeImage(BufferedImage buff, String extension, String formatName, Site site, String ip,
			Integer userId) {
		PublishPoint point = site.getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String urlPrefix = point.getUrlPrefix();
		String filename = UploadUtils.getUrl(site.getId(), Uploader.IMAGE, extension);
		try {
			fileHandler.storeImage(buff, formatName, filename);
			long length = buff.getWidth() * buff.getHeight() / 3;
			attachmentService.save(filename, length, ip, userId, site.getId());
			return urlPrefix + filename;
		} catch (IOException e) {
			logger.error(null, e);
			return null;
		}
	}

	@Override
	public void upload(String url, String type, Site site, Integer userId, String ip, UploadResult result) {
		upload(url, type, site, userId, ip, result, null, null, null, null, null, null, null, null);
	}

	@Override
	public void upload(MultipartFile partFile, String type, Site site, Integer userId, String ip, UploadResult result) {
		upload(partFile, type, site, userId, ip, result, null, null, null, null, null, null, null, null);
	}

	@Override
	public void upload(String url, String type, Site site, Integer userId, String ip, UploadResult result,
			Boolean scale, Boolean exact, Integer width, Integer height, Boolean thumbnail, Integer thumbnailWidth,
			Integer thumbnailHeight, Boolean watermark) {
		try {
			URL source = new URL(url);
			// file（下载）支持重定向支持，其他的不支持。
			if (Uploader.FILE.equals(type)) {
				HttpURLConnection.setFollowRedirects(true);
			} else {
				HttpURLConnection.setFollowRedirects(false);
			}
			HttpURLConnection conn = (HttpURLConnection) source.openConnection();
			conn.setRequestProperty("User-Agent", Constants.USER_ANGENT);
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				result.setError("URL response error:" + responseCode);
				return;
			}
			if (Uploader.IMAGE.equals(type)) {
				String contentType = conn.getContentType();
				if (!validateImageContentType(contentType, result)) {
					return;
				}
			}
			String disposition = conn.getHeaderField(HttpHeaders.CONTENT_DISPOSITION);
			String fileName = StringUtils.substringBetween(disposition, "filename=\"", "\"");
			if (StringUtils.isBlank(fileName)) {
				fileName = FilenameUtils.getName(source.getPath());
			}
			String ext = FilenameUtils.getExtension(fileName);
			File temp = FilesEx.getTempFile(ext);
			InputStream is = conn.getInputStream();
			try {
				FileUtils.copyInputStreamToFile(is, temp);
				doUpload(temp, fileName, type, site, userId, ip, result, scale, exact, width, height, thumbnail,
						thumbnailWidth, thumbnailHeight, watermark);
			} finally {
				IOUtils.closeQuietly(is);
				FileUtils.deleteQuietly(temp);
			}
		} catch (Exception e) {
			result.setError(e.getMessage());
		}
		return;
	}

	@Override
	public void upload(MultipartFile partFile, String type, Site site, Integer userId, String ip, UploadResult result,
			Boolean scale, Boolean exact, Integer width, Integer height, Boolean thumbnail, Integer thumbnailWidth,
			Integer thumbnailHeight, Boolean watermark) {
		try {
			if (!validateFile(partFile, result)) {
				return;
			}
			String fileName = partFile.getOriginalFilename();
			String ext = FilenameUtils.getExtension(fileName);
			File temp = FilesEx.getTempFile(ext);
			partFile.transferTo(temp);
			try {
				doUpload(temp, fileName, type, site, userId, ip, result, scale, exact, width, height, thumbnail,
						thumbnailWidth, thumbnailHeight, watermark);
			} finally {
				FileUtils.deleteQuietly(temp);
			}
		} catch (Exception e) {
			result.setError(e.getMessage());
			logger.error(null, e);
		}
		return;
	}

	private UploadResult doUpload(File file, String fileName, String type, Site site, Integer userId, String ip,
			UploadResult result, Boolean scale, Boolean exact, Integer width, Integer height, Boolean thumbnail,
			Integer thumbnailWidth, Integer thumbnailHeight, Boolean watermark) throws Exception {
		Integer siteId = site.getId();
		long fileLength = file.length();
		String ext = FilenameUtils.getExtension(fileName).toLowerCase();
		GlobalUpload gu = site.getGlobal().getUpload();
		// 后缀名是否合法
		if (!validateExt(ext, type, gu, result)) {
			return result;
		}
		// 文库是否开启
		if (type == Uploader.DOC && !isDocEnabled(result, site.getGlobal())) {
			return result;
		}
		PublishPoint point = site.getUploadsPublishPoint();
		String urlPrefix = point.getUrlPrefix();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		String pathname = site.getSiteBase(Uploader.getQuickPathname(type, ext));
		String fileUrl = urlPrefix + pathname;
		String pdfUrl = null;
		String swfUrl = null;
		if (Uploader.IMAGE.equals(type)) {
			SiteWatermark sw = site.getWatermark();
			doUploadImage(fileHandler, file, pathname, scale, exact, width, height, thumbnail, thumbnailWidth,
					thumbnailHeight, watermark, gu, sw, ip, userId, siteId);
		} else if (Uploader.DOC == type) {
			if (!"swf".equals(ext)) {
				String swfPathname = site.getSiteBase(Uploader.getQuickPathname(type, "swf"));
				swfUrl = urlPrefix + swfPathname;
				String pdfPathname = null;
				if (!"pdf".equals(ext)) {
					pdfPathname = site.getSiteBase(Uploader.getQuickPathname(type, "pdf"));
					pdfUrl = urlPrefix + pdfPathname;
				} else {
					pdfUrl = fileUrl;
				}
				UploadDoc.exec(attachmentService, fileHandler, pathname, ext, pdfPathname, swfPathname, file, ip,
						userId, siteId);
			} else {
				swfUrl = fileUrl;
				fileHandler.storeFile(file, pathname);
			}
		} else {
			fileHandler.storeFile(file, pathname);
		}
		attachmentService.save(pathname, fileLength, ip, userId, siteId);
		result.set(fileUrl, fileName, ext, fileLength, pdfUrl, swfUrl);
		return result;
	}

	private void doUploadImage(FileHandler fileHandler, File file, String pathname, Boolean scale, Boolean exact,
			Integer width, Integer height, Boolean thumbnail, Integer thumbnailWidth, Integer thumbnailHeight,
			Boolean watermark, GlobalUpload gu, SiteWatermark sw, String ip, Integer userId, Integer siteId)
			throws IOException {
		ScaleParam scaleParam = gu.getScaleParam(scale, exact, width, height);
		scale = scaleParam.isScale();

		ThumbnailParam thumbnailParam = new ThumbnailParam(thumbnail, thumbnailWidth, thumbnailHeight);
		thumbnail = thumbnailParam.isThumbnail();

		WatermarkParam watermarkParam = sw.getWatermarkParam(watermark);
		watermark = watermarkParam.isWatermark();

		String formatName = null;
		if (watermark || scale || thumbnail) {
			formatName = Images.getFormatName(file);
		}
		if (StringUtils.isNotBlank(formatName)) {
			// 可以且需要处理的图片
			storeImage(file, scaleParam, thumbnailParam, watermarkParam, formatName, pathname, fileHandler, ip, userId,
					siteId);
		} else {
			// 不可处理的图片
			fileHandler.storeFile(file, pathname);
		}
	}

	private void storeImage(File src, ScaleParam scaleParam, ThumbnailParam thumbnailParam,
			WatermarkParam watermarkParam, String formatName, String pathname, FileHandler fileHandler, String ip,
			Integer userId, Integer siteId) throws IOException {
		String srcPath = src.getAbsolutePath();
		if (scaleParam.isScale()) {
			imageHandler.resize(srcPath, srcPath, scaleParam);
		}
		if (thumbnailParam.isThumbnail()) {
			File thumbnailFile = FilesEx.getTempFile();
			String thumbnailPath = thumbnailFile.getAbsolutePath();
			Integer thumbnailWidth = thumbnailParam.getWidth();
			Integer thumbnailHeight = thumbnailParam.getHeight();
			if (imageHandler.resize(srcPath, thumbnailPath, thumbnailWidth, thumbnailHeight, false)) {
				try {
					String thumbnailName = Uploader.getThumbnailName(pathname);
					fileHandler.storeFile(thumbnailFile, thumbnailName);
					// 新产生的缩略图要单独保存到附件，原图在doUpload里面保存到附件
					attachmentService.save(thumbnailName, thumbnailFile.length(), ip, userId, siteId);
				} finally {
					// 确保删除临时文件
					FileUtils.deleteQuietly(thumbnailFile);
				}
			}
		}
		if (watermarkParam.isWatermark()) {
			String imagePath = watermarkParam.getImagePath();
			LocalFileHandler localFileHandler = FileHandler.getLocalFileHandler(pathResolver,
					Constants.TEMPLATE_STORE_PATH);
			File overlay = localFileHandler.getFile(imagePath);
			String overlayPath = overlay.getAbsolutePath();
			imageHandler.composite(overlayPath, srcPath, srcPath, watermarkParam);
		}
		fileHandler.storeFile(src, pathname);
	}

	private boolean validateExt(String extension, String type, GlobalUpload gu, UploadResult result) {
		if (!gu.isExtensionValid(extension, type)) {
			logger.debug("image extension not allowed: " + extension);
			result.setErrorCode("imageExtensionNotAllowed", new String[] { extension });
			return false;
		}
		return true;
	}

	private boolean isDocEnabled(UploadResult result, Global global) {
		if (!global.isDocEnabled()) {
			result.setError("DOC Converter is not available!");
			return false;
		}
		return true;
	}

	private boolean validateFile(MultipartFile partFile, UploadResult result) {
		if (partFile == null || partFile.isEmpty()) {
			logger.debug("file is empty");
			result.setError("no file upload!");
			return false;
		}
		return true;
	}

	private boolean validateImageContentType(String contentType, UploadResult result) {
		if (!StringUtils.contains(contentType, "image")) {
			logger.debug("ContentType not contain Image: " + contentType);
			result.setError("ContentType not contain Image: " + contentType);
			return false;
		}
		return true;
	}

	@Autowired
	protected ImageHandler imageHandler;
	@Autowired
	protected AttachmentService attachmentService;
	@Autowired
	protected PathResolver pathResolver;
}

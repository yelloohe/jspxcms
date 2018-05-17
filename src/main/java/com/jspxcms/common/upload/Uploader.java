package com.jspxcms.common.upload;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 上传工具类
 * 
 * @author liufang
 * 
 */
public class Uploader {
	/**
	 * 文件
	 */
	public static final String FILE = "file";
	/**
	 * 图片
	 */
	public static final String IMAGE = "image";
	/**
	 * 视频
	 */
	public static final String VIDEO = "video";
	/**
	 * 音频
	 */
	public static final String AUDIO = "audio";
	/**
	 * FLASH
	 */
	public static final String FLASH = "flash";
	/**
	 * 文库
	 */
	public static final String DOC = "doc";

	/**
	 * 缩略图后缀
	 */
	public static final String THUMBNAIL = "_min";
	/**
	 * 快速上传文件夹
	 */
	public static final String QUICK_UPLOAD = "public";

	public static String randomPathname(String pattern, String extension) {
		StringBuilder filename = new StringBuilder();
		DateFormat df = new SimpleDateFormat(pattern);
		filename.append(df.format(new Date()));
		filename.append(RandomStringUtils.random(10, '0', 'Z', true, true)
				.toLowerCase());
		if (StringUtils.isNotBlank(extension)) {
			filename.append(".").append(extension.toLowerCase());
		}
		return filename.toString();
	}

	public static String randomPathname(String extension) {
		return randomPathname("/yyyyMM/yyyyMMddHHmmss_", extension);
	}

	public static String getQuickPathname(String type, String extension) {
		StringBuilder name = new StringBuilder();
		name.append('/').append(type);
		name.append('/').append(QUICK_UPLOAD);
		name.append(Uploader.randomPathname(extension));
		return name.toString();
	}

	public static String getThumbnailName(String filename) {
		if (filename == null) {
			return null;
		}
		String path = FilenameUtils.getFullPath(filename);
		String baseName = FilenameUtils.getBaseName(filename);
		String extension = FilenameUtils.getExtension(filename);
		if (StringUtils.isNotBlank(extension)) {
			return path + baseName + THUMBNAIL + "." + extension;
		} else {
			return path + baseName + THUMBNAIL;
		}
	}
}

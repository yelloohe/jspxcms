package com.jspxcms.common.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Ant Zip工具类
 * 
 * @author liufang
 * 
 */
public class AntZipUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(AntZipUtils.class);
	private static final int DEFAULT_BUFFER_SIZE = 8192;
	private static final String DEFAULT_ENCODING = "GBK";

	public static boolean isZipFile(File file) {
		String extension = FilenameUtils.getExtension(file.getName());
		return "zip".equalsIgnoreCase(extension);
	}

	public static void zip(File[] srcFiles) {
		zip(srcFiles, null, DEFAULT_ENCODING);
	}

	public static void zip(File[] srcFiles, OutputStream os) {
		zip(srcFiles, os, null, DEFAULT_ENCODING);
	}

	public static void zip(File[] srcFiles, FileFilter filter, String encoding) {
		Assert.notEmpty(srcFiles);
		File destFile = new File(srcFiles[0].getParentFile(),
				srcFiles[0].getName() + ".zip");
		zip(srcFiles, destFile, filter, encoding);
	}

	public static void zip(File[] srcFiles, File destFile, FileFilter filter,
			String encoding) {
		try {
			FileOutputStream fos = new FileOutputStream(destFile);
			zip(srcFiles, fos, filter, encoding);
		} catch (FileNotFoundException e) {
			logger.error("", e);
		}

	}

	public static void zip(File[] srcFiles, OutputStream os, FileFilter filter,
			String encoding) {
		Assert.notEmpty(srcFiles);
		String root = srcFiles[0].getParentFile().getAbsolutePath();
		int rootLength = root.length() + 1;
		Queue<File> queue = new LinkedList<File>();
		for (File f : srcFiles) {
			queue.offer(f);
		}
		File file;
		String name;
		FileInputStream fin = null;
		byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
		int readed;
		ZipOutputStream zos = new ZipOutputStream(os);
		if (StringUtils.isNotBlank(encoding)) {
			zos.setEncoding(encoding);
		}
		try {
			try {
				while (!queue.isEmpty()) {
					file = queue.poll();
					name = file.getAbsolutePath().substring(rootLength);
					name = name.replace(File.separatorChar, '/');
					if (file.isDirectory()) {
						zos.putNextEntry(new ZipEntry(name + '/'));
						zos.closeEntry();
						for (File f : file.listFiles(filter)) {
							queue.offer(f);
						}
					} else {
						zos.putNextEntry(new ZipEntry(name));
						fin = new FileInputStream(file);
						while ((readed = fin.read(buff)) > 0) {
							zos.write(buff, 0, readed);
						}
						fin.close();
						zos.closeEntry();
					}
				}
			} finally {
				if (fin != null) {
					fin.close();
				}
				zos.close();
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public static void unzip(File zipFile, File destDir) {
		unzip(zipFile, destDir, DEFAULT_ENCODING);
	}

	public static void unzip(File zipFile, File destDir, String encoding) {
		if (destDir.exists() && !destDir.isDirectory()) {
			throw new IllegalArgumentException("destDir is not a directory!");
		}
		ZipFile zip = null;
		InputStream is = null;
		FileOutputStream fos = null;
		File file;
		String name;
		byte[] buff = new byte[DEFAULT_BUFFER_SIZE];
		int readed;
		ZipEntry entry;
		try {
			try {
				if (StringUtils.isNotBlank(encoding)) {
					zip = new ZipFile(zipFile, encoding);
				} else {
					zip = new ZipFile(zipFile);
				}
				Enumeration<?> en = zip.getEntries();
				while (en.hasMoreElements()) {
					entry = (ZipEntry) en.nextElement();
					name = entry.getName();
					name = name.replace('/', File.separatorChar);
					file = new File(destDir, name);
					if (entry.isDirectory()) {
						file.mkdirs();
					} else {
						// 创建父目录
						file.getParentFile().mkdirs();
						is = zip.getInputStream(entry);
						fos = new FileOutputStream(file);
						while ((readed = is.read(buff)) > 0) {
							fos.write(buff, 0, readed);
						}
						fos.close();
						is.close();
					}
				}
			} finally {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
				if (zip != null) {
					zip.close();
				}
			}
		} catch (IOException e) {
			logger.error("", e);
		}

	}
}

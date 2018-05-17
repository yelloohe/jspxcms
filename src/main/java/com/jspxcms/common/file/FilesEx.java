package com.jspxcms.common.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 文件工具类
 * 
 * @author liufang
 * 
 */
public abstract class FilesEx {
	public static String getSize(Long length) {
		if (length == null) {
			return "0 KB";
		}
		long lengthKB = length / 1024;
		if (lengthKB < 1024) {
			if (length % 1024 > 0) {
				lengthKB++;
			}
			if (lengthKB == 1024) {
				return "1 MB";
			} else {
				return lengthKB + " KB";
			}
		}
		DecimalFormat format = new DecimalFormat("0.##");
		BigDecimal lengthMB = new BigDecimal(length).divide(new BigDecimal(1024 * 1024), 2, RoundingMode.HALF_DOWN);
		if (lengthMB.compareTo(new BigDecimal(1024)) < 0) {
			return format.format(lengthMB) + " MB";
		}
		BigDecimal lengthGB = lengthMB.divide(new BigDecimal(1024), 2, RoundingMode.HALF_DOWN);
		return format.format(lengthGB) + " GB";
	}

	public static String randomName(String extension) {
		StringBuilder name = new StringBuilder();
		name.append(System.currentTimeMillis());
		String random = RandomStringUtils.random(10, '0', 'Z', true, true);
		name.append(random.toLowerCase());
		if (StringUtils.isNotBlank(extension)) {
			name.append(".");
			name.append(extension);
		}
		return name.toString();
	}

	private static final AtomicInteger COUNTER = new AtomicInteger(0);
	private static final String UID = UUID.randomUUID().toString().replace('-', '_');

	private static String getUniqueId() {
		final int limit = 2000000000;
		int current = COUNTER.getAndIncrement();
		String id = Integer.toString(current);
		if (current < limit) {
			id = ("000000000" + id).substring(id.length());
		}
		return id;
	}

	public static void makeParentDir(File file) throws IOException {
		File parent = file.getParentFile();
		if (parent != null) {
			if (!parent.mkdirs() && !parent.isDirectory()) {
				throw new IOException("Directory '" + parent + "' could not be created");
			}
		}
	}

	/**
	 * 获取临时文件，扩展名为.tmp
	 * 
	 * @return
	 */
	public static File getTempFile() {
		return getTempFile(null);
	}

	/**
	 * 获取临时文件
	 * 
	 * @param ext
	 *            为null则默认为.tmp；如不需要扩展名可传空串""。
	 * @return
	 */
	public static File getTempFile(String ext) {
		if (ext == null) {
			ext = "tmp";
		}
		String suffix = StringUtils.isNotBlank(ext) ? "." + ext : "";
		String tempFileName = UID + getUniqueId() + suffix;
		File tempFile = new File(FileUtils.getTempDirectoryPath(), tempFileName);
		return tempFile;
	}

	/**
	 * Iterates over a base name and returns the first non-existent file.<br />
	 * This method extracts a file's base name, iterates over it until the first non-existent appearance with
	 * <code>basename(n).ext</code>. Where n is a positive integer starting from one.
	 * 
	 * @param file
	 *            base file
	 * @return first non-existent file
	 */
	public static File getUniqueFile(final File file) {
		if (!file.exists())
			return file;

		File tmpFile = new File(file.getAbsolutePath());
		File parentDir = tmpFile.getParentFile();
		int count = 1;
		String extension = FilenameUtils.getExtension(tmpFile.getName());
		String baseName = FilenameUtils.getBaseName(tmpFile.getName());
		String suffix = StringUtils.isNotBlank(extension) ? "." + extension : "";
		do {
			tmpFile = new File(parentDir, baseName + "(" + count++ + ")" + suffix);
		} while (tmpFile.exists());
		return tmpFile;
	}

	public static File getFileFromUrl(URL url) throws IOException {
		return getFileFromUrl(url, getTempFile());
	}

	public static File getFileFromUrl(URL url, File file) throws IOException {
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn.getResponseCode() != 200) {
			return null;
		}
		InputStream input = conn.getInputStream();
		if (file == null) {
			file = getTempFile();
		}
		OutputStream output = new FileOutputStream(file);
		IOUtils.copy(input, output);
		IOUtils.closeQuietly(input);
		IOUtils.closeQuietly(output);
		return file;
	}
}

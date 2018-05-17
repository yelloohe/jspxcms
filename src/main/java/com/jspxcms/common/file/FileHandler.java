package com.jspxcms.common.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jspxcms.common.web.PathResolver;

import freemarker.template.Template;

/**
 * 文件处理抽象类
 * 
 * @author liufang
 * 
 */
public abstract class FileHandler {

	public static LocalFileHandler getLocalFileHandler(PathResolver pathResolver, String prefix) {
		return new LocalFileHandler(pathResolver, prefix);
	}

	public static FileHandler getFileHandler(FtpTemplate ftpTemplate, String prefix) {
		return new FtpFileHandler(ftpTemplate, prefix);
	}

	protected String prefix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public abstract boolean mkdir(String name, String id);

	public abstract boolean rename(String dest, String id);

	public abstract void move(String dest, String[] ids);

	public abstract void move(String dest, String id);

	public abstract void store(String text, String name, String path) throws IOException;

	public abstract void store(MultipartFile file, String path) throws IllegalStateException, IOException;

	public abstract void storeFile(InputStream source, String filename) throws IllegalStateException, IOException;

	public abstract void storeFile(MultipartFile file, String filename) throws IllegalStateException, IOException;

	public abstract void storeFile(File file, String filename) throws IllegalStateException, IOException;

	public abstract void storeFile(List<File> files, List<String> filenames) throws IllegalStateException, IOException;

	public abstract void storeFile(final Template template, final Object rootMap, final String filename);

	public abstract void storeImage(BufferedImage image, String extension, String filename) throws IOException;

	public abstract void storeImages(List<BufferedImage> images, String formatName, List<String> filenames)
			throws IOException;

	public abstract boolean delete(String[] ids);

	public abstract boolean delete(String id);

	public abstract File getFile(String id);

	public abstract CommonFile get(String id, String displayPath);

	public abstract InputStream getInputStream(String id);

	public abstract BufferedImage readImage(String id);

	public abstract String getFormatName(String id);

	public abstract List<String> list(String path);

	public abstract List<CommonFile> listFiles(String path, String displayPath);

	public abstract List<CommonFile> listFiles(String search, String path, String displayPath);

	public abstract List<CommonFile> listFiles(CommonFileFilter filter, String path, String displayPath);

}

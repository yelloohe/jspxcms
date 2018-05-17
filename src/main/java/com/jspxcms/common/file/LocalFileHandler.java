package com.jspxcms.common.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.jspxcms.common.file.CommonFile.FileType;
import com.jspxcms.common.image.Images;
import com.jspxcms.common.web.PathResolver;

import freemarker.template.Template;

/**
 * 本地文件处理
 * 
 * @author liufang
 * 
 */
public class LocalFileHandler extends FileHandler {
	private static final Logger logger = LoggerFactory.getLogger(LocalFileHandler.class);

	private PathResolver pathResolver;

	public LocalFileHandler(PathResolver pathResolver, String prefix) {
		this.pathResolver = pathResolver;
		this.prefix = prefix;
	}

	@Override
	public boolean mkdir(String name, String id) {
		File parent = new File(pathResolver.getPath(id, prefix));
		File dir = new File(parent, name);
		return dir.mkdirs();
	}

	@Override
	public boolean rename(String dest, String id) {
		File file = new File(pathResolver.getPath(id, prefix));
		return file.renameTo(new File(file.getParentFile(), dest));
	}

	@Override
	public void move(String dest, String[] ids) {
		File file, destDir;
		for (String id : ids) {
			file = new File(pathResolver.getPath(id, prefix));
			destDir = new File(pathResolver.getPath(dest, prefix));
			try {
				FileUtils.moveToDirectory(file, destDir, true);
			} catch (IOException e) {
				logger.error("move direcotry error.", e);
			}
		}
	}

	@Override
	public void move(String dest, String id) {
		File file = new File(pathResolver.getPath(id, prefix));
		File destDir = new File(pathResolver.getPath(dest, prefix));
		try {
			FileUtils.moveToDirectory(file, destDir, true);
		} catch (IOException e) {
			logger.error("move direcotry error.", e);
		}
	}

	@Override
	public void store(String text, String name, String path) throws IOException {
		File parent = new File(pathResolver.getPath(path, prefix));
		if (!parent.exists()) {
			parent.mkdirs();
		}
		File file = new File(parent, name);
		FileUtils.write(file, text, "UTF-8");
	}

	@Override
	public void store(MultipartFile file, String path) throws IllegalStateException, IOException {
		File parent = new File(pathResolver.getPath(path, prefix));
		if (!parent.exists()) {
			parent.mkdirs();
		}
		File dest = new File(parent, file.getOriginalFilename());
		file.transferTo(dest);
	}

	@Override
	public void storeFile(InputStream source, String filename) throws IllegalStateException, IOException {
		File dest = new File(pathResolver.getPath(filename, prefix));
		FileUtils.copyInputStreamToFile(source, dest);
	}

	@Override
	public void storeFile(File file, String filename) throws IllegalStateException, IOException {
		File dest = new File(pathResolver.getPath(filename, prefix));
		FileUtils.moveFile(file, dest);
	}

	@Override
	public void storeFile(List<File> files, List<String> filenames) throws IllegalStateException, IOException {
		for (int i = 0, len = files.size(); i < len; i++) {
			storeFile(files.get(i), filenames.get(i));
		}
	}

	@Override
	public void storeFile(MultipartFile file, String filename) throws IllegalStateException, IOException {
		File dest = new File(pathResolver.getPath(filename, prefix));
		File parent = dest.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		file.transferTo(dest);
	}

	@Override
	public void storeFile(Template template, Object rootMap, String filename) {
		File dest = new File(pathResolver.getPath(filename, prefix));
		File parent = dest.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		try {
			OutputStream os = null;
			Writer writer = null;
			try {
				os = new FileOutputStream(dest);
				writer = new OutputStreamWriter(os, "UTF-8");
				template.process(rootMap, writer);
			} finally {
				IOUtils.closeQuietly(writer);
				IOUtils.closeQuietly(os);
			}
		} catch (Exception e) {
			logger.error(null, e);
		}

	}

	@Override
	public void storeImage(BufferedImage image, String formatName, String filename) throws IOException {
		File dest = new File(pathResolver.getPath(filename, prefix));
		FilesEx.makeParentDir(dest);
		ImageIO.write(image, formatName, dest);
	}

	@Override
	public void storeImages(List<BufferedImage> images, String formatName, List<String> filenames) throws IOException {
		for (int i = 0, len = images.size(); i < len; i++) {
			storeImage(images.get(i), formatName, filenames.get(i));
		}
	}

	@Override
	public boolean delete(String[] ids) {
		boolean result = false;
		for (String id : ids) {
			result = delete(id);
		}
		return result;
	}

	@Override
	public boolean delete(String id) {
		File file = new File(pathResolver.getPath(id, prefix));
		return FileUtils.deleteQuietly(file);
	}

	@Override
	public List<String> list(String path) {
		ArrayList<String> list = new ArrayList<String>();
		File parent = new File(pathResolver.getPath(path, prefix));
		String[] names = parent.list();
		if (names != null) {
			list.addAll(Arrays.asList(names));
		}
		return list;
	}

	@Override
	public File getFile(String id) {
		File file = new File(pathResolver.getPath(id, prefix));
		return file;
	}

	@Override
	public CommonFile get(String id, String displayPath) {
		File file = new File(pathResolver.getPath(id, prefix));
		CommonFile commonFile = new CommonFile(id, displayPath, file);
		if (commonFile.getType() == FileType.text) {
			try {
				commonFile.setText(FileUtils.readFileToString(file, "UTF-8"));
			} catch (IOException e) {
				logger.error("read file error!", e);
			}
		}
		return commonFile;
	}

	@Override
	public InputStream getInputStream(String id) {
		File file = new File(pathResolver.getPath(id, prefix));
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			logger.error("file not found!", e);
		}
		return is;
	}

	@Override
	public BufferedImage readImage(String id) {
		File file = new File(pathResolver.getPath(id, prefix));
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			logger.error("read image error!", e);
		}
		return image;
	}

	@Override
	public String getFormatName(String id) {
		File file = new File(pathResolver.getPath(id, prefix));
		return Images.getFormatName(file);
	}

	@Override
	public List<CommonFile> listFiles(String path, String displayPath) {
		return listFiles((CommonFileFilter) null, path, displayPath);
	}

	@Override
	public List<CommonFile> listFiles(String search, String path, String displayPath) {
		return listFiles(new SearchCommonFileFilter(search), path, displayPath);
	}

	@Override
	public List<CommonFile> listFiles(CommonFileFilter filter, String path, String displayPath) {
		File parent = new File(pathResolver.getPath(path, prefix));
		List<CommonFile> list = new ArrayList<CommonFile>();
		CommonFile commonFile;
		String id;
		File[] files = parent.listFiles();
		if (files != null) {
			for (File file : parent.listFiles()) {
				id = path + "/" + file.getName();
				commonFile = new CommonFile(id, displayPath, file);
				if (filter == null || filter.accept(commonFile)) {
					list.add(commonFile);
				}
			}
		}
		return list;
	}

}

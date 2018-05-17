package com.jspxcms.common.file;

import java.awt.image.BufferedImage;
import java.io.File;
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
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.jspxcms.common.file.CommonFile.FileType;
import com.jspxcms.common.image.Images;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Ftp文件处理
 * 
 * @author liufang
 * 
 */
public class FtpFileHandler extends FileHandler {
	private static final Logger logger = LoggerFactory.getLogger(FtpFileHandler.class);

	private FtpTemplate ftpTemplate;

	public FtpFileHandler(FtpTemplate ftpTemplate, String prefix) {
		this.ftpTemplate = ftpTemplate;
		this.prefix = prefix;
	}

	public static void mkdir(FTPClient client, String path) throws IOException {
		if (StringUtils.isBlank(path)) {
			return;
		}
		if (path.startsWith("/")) {
			client.changeWorkingDirectory("/");
		}
		for (String dir : StringUtils.split(path, '/')) {
			if (!client.changeWorkingDirectory(dir)) {
				client.makeDirectory(dir);
				client.changeWorkingDirectory(dir);
			}
		}
	}

	@Override
	public boolean mkdir(final String name, final String path) {
		final boolean[] success = new boolean[1];
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String fullPath = prefix + path;
				mkdir(client, fullPath);
				success[0] = client.makeDirectory(fullPath + "/" + name);
			}
		});
		return success[0];
	}

	@Override
	public boolean rename(final String dest, final String id) {
		final boolean[] success = new boolean[1];
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String pathname = prefix + id;
				String path = FilenameUtils.getFullPath(pathname);
				String name = FilenameUtils.getName(pathname);
				if (client.changeWorkingDirectory(path)) {
					success[0] = client.rename(name, dest);
				} else {
					success[0] = false;
				}
			}
		});
		return success[0];
	}

	@Override
	public void move(final String dest, final String[] ids) {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String destname = prefix + dest;
				for (String id : ids) {
					client.rename(prefix + id, destname + "/" + FilenameUtils.getName(id));
				}
			}
		});
	}

	@Override
	public void move(final String dest, final String id) {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String pathname = prefix + id;
				String destname = prefix + dest + "/" + FilenameUtils.getName(id);
				client.rename(pathname, destname);
			}
		});
	}

	@Override
	public void store(final String text, final String name, final String path) throws IOException {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String fullPath = prefix + path;
				mkdir(client, fullPath);
				client.changeWorkingDirectory(fullPath);
				client.setBufferSize(8192);
				OutputStream os = null;
				try {
					os = client.storeFileStream(name);
					IOUtils.write(text, os, "UTF-8");
				} finally {
					IOUtils.closeQuietly(os);
					client.completePendingCommand();
				}
			}
		});
	}

	@Override
	public void store(final MultipartFile file, final String path) {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String fullPath = prefix + path;
				mkdir(client, fullPath);
				client.changeWorkingDirectory(fullPath);
				client.setBufferSize(8192);
				InputStream is = null;
				try {
					is = file.getInputStream();
					client.storeFile(file.getOriginalFilename(), is);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		});
	}

	@Override
	public void storeFile(final InputStream source, final String filename) throws IllegalStateException, IOException {
		try {
			ftpTemplate.execute(new FtpClientExtractor() {
				public void doInFtp(FTPClient client) throws IOException {
					String fullPathName = prefix + filename;
					String fullPath = FilenameUtils.getFullPath(fullPathName);
					mkdir(client, fullPath);
					client.setBufferSize(8192);
					OutputStream output = null;
					try {
						output = client.storeFileStream(fullPathName);
						IOUtils.copyLarge(source, output);
					} finally {
						IOUtils.closeQuietly(output);
						client.completePendingCommand();
					}
				}
			});
		} finally {
			IOUtils.closeQuietly(source);
		}
	}

	@Override
	public void storeFile(final File file, final String filename) throws IllegalStateException, IOException {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String fullPathName = prefix + filename;
				String fullPath = FilenameUtils.getFullPath(fullPathName);
				mkdir(client, fullPath);
				client.setBufferSize(8192);
				InputStream is = null;
				try {
					is = FileUtils.openInputStream(file);
					client.storeFile(fullPathName, is);
				} finally {
					IOUtils.closeQuietly(is);
					FileUtils.deleteQuietly(file);
				}
			}
		});
	}

	@Override
	public void storeFile(final List<File> files, final List<String> filenames) throws IllegalStateException,
			IOException {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				client.setBufferSize(8192);
				for (int i = 0, len = files.size(); i < len; i++) {
					File file = files.get(i);
					String fullPathName = prefix + filenames.get(i);
					String fullPath = FilenameUtils.getFullPath(fullPathName);
					mkdir(client, fullPath);
					InputStream is = null;
					try {
						is = FileUtils.openInputStream(file);
						client.storeFile(fullPathName, is);
					} finally {
						IOUtils.closeQuietly(is);
						FileUtils.deleteQuietly(file);
					}
				}
			}
		});

	}

	@Override
	public void storeFile(final MultipartFile file, final String filename) {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String fullPathName = prefix + filename;
				String fullPath = FilenameUtils.getFullPath(fullPathName);
				mkdir(client, fullPath);
				client.setBufferSize(8192);
				InputStream is = null;
				try {
					is = file.getInputStream();
					client.storeFile(fullPathName, is);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		});
	}

	@Override
	public void storeFile(final Template template, final Object rootMap, final String filename) {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String fullPathName = prefix + filename;
				String fullPath = FilenameUtils.getFullPath(fullPathName);
				mkdir(client, fullPath);
				client.setBufferSize(8192);
				try {
					OutputStream os = null;
					Writer writer = null;
					try {
						os = client.storeFileStream(fullPathName);
						writer = new OutputStreamWriter(os, "UTF-8");
						template.process(rootMap, writer);
					} finally {
						IOUtils.closeQuietly(os);
						IOUtils.closeQuietly(writer);
						client.completePendingCommand();
					}
				} catch (TemplateException e) {
					logger.error("Create Template error.", e);
				}
			}
		});
	}

	@Override
	public void storeImage(final BufferedImage image, final String formatName, final String filename) {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String fullPathName = prefix + filename;
				String fullPath = FilenameUtils.getFullPath(fullPathName);
				mkdir(client, fullPath);
				client.setBufferSize(8192);
				OutputStream os = null;
				try {
					os = client.storeFileStream(fullPathName);
					ImageIO.write(image, formatName, os);
				} finally {
					IOUtils.closeQuietly(os);
					client.completePendingCommand();
				}
			}
		});
	}

	@Override
	public void storeImages(final List<BufferedImage> images, final String formatName, final List<String> filenames) {
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				client.setBufferSize(8192);
				OutputStream os = null;
				String fullPathname;
				String fullPath;
				for (int i = 0, len = images.size(); i < len; i++) {
					fullPathname = prefix + filenames.get(i);
					fullPath = FilenameUtils.getFullPath(fullPathname);
					mkdir(client, fullPath);
					try {
						os = client.storeFileStream(fullPathname);
						ImageIO.write(images.get(i), formatName, os);
					} finally {
						IOUtils.closeQuietly(os);
						client.completePendingCommand();
					}
				}
			}
		});
	}

	@Override
	public boolean delete(final String[] ids) {
		final boolean[] success = new boolean[1];
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				success[0] = false;
				for (String id : ids) {
					success[0] = delete(id, prefix, client);
				}
			}
		});
		return success[0];

	}

	@Override
	public boolean delete(final String id) {
		final boolean[] success = new boolean[1];
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				success[0] = delete(id, prefix, client);
			}
		});
		return success[0];
	}

	private boolean delete(String id, String prefix, FTPClient client) throws IOException {
		String pathname = prefix + id;
		String path = FilenameUtils.getFullPath(pathname);
		String name = FilenameUtils.getName(pathname);
		client.changeWorkingDirectory(path);
		boolean success = false;
		for (FTPFile file : client.listFiles(path)) {
			if (file.getName().equals(name)) {
				if (file.isDirectory()) {
					delete(pathname, client);
					success = client.removeDirectory(pathname);
				} else {
					success = client.deleteFile(name);
				}
			}
		}
		return success;
	}

	private void delete(String pathname, FTPClient client) throws IOException {
		String subpath;
		for (FTPFile file : client.listFiles(pathname)) {
			subpath = pathname + "/" + file.getName();
			if (file.isDirectory()) {
				delete(subpath, client);
				client.removeDirectory(subpath);
			} else {
				client.deleteFile(subpath);
			}
		}
	}

	@Override
	public File getFile(final String id) {
		final File[] file = new File[1];
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String ext = FilenameUtils.getExtension(id);
				file[0] = FilesEx.getTempFile(ext);
				FileOutputStream os = null;
				try {
					os = new FileOutputStream(file[0]);
					client.retrieveFile(prefix + id, os);
				} finally {
					IOUtils.closeQuietly(os);
				}
			}
		});
		return file[0];
	}

	@Override
	public CommonFile get(final String id, final String displayPath) {
		final CommonFile commonFile = new CommonFile(id, displayPath);
		if (commonFile.getType() == FileType.text) {
			ftpTemplate.execute(new FtpClientExtractor() {
				public void doInFtp(FTPClient client) throws IOException {
					commonFile.setText(IOUtils.toString(client.retrieveFileStream(prefix + id), "UTF-8"));
				}
			});
		}
		return commonFile;
	}

	@Override
	public InputStream getInputStream(final String id) {
		final InputStream[] is = new InputStream[1];
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				is[0] = client.retrieveFileStream(prefix + id);
			}
		});
		return is[0];
	}

	@Override
	public BufferedImage readImage(final String id) {
		final BufferedImage[] image = new BufferedImage[1];
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				InputStream is = client.retrieveFileStream(prefix + id);
				if (is != null) {
					image[0] = ImageIO.read(is);
				}
			}
		});
		return image[0];
	}

	@Override
	public String getFormatName(final String id) {
		final String[] formatName = new String[1];
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				InputStream input = client.retrieveFileStream(prefix + id);
				try {
					formatName[0] = Images.getFormatName(input);
				} finally {
					IOUtils.closeQuietly(input);
				}
			}
		});
		return formatName[0];
	}

	@Override
	public List<String> list(final String path) {
		final List<String> list = new ArrayList<String>();
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				String[] names = client.listNames(prefix + path);
				if (names != null) {
					list.addAll(Arrays.asList(names));
				}
			}
		});
		return list;
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
	public List<CommonFile> listFiles(final CommonFileFilter filter, final String path, final String displayPath) {
		final List<CommonFile> list = new ArrayList<CommonFile>();
		ftpTemplate.execute(new FtpClientExtractor() {
			public void doInFtp(FTPClient client) throws IOException {
				CommonFile commonFile;
				String id;
				FTPFile[] files = client.listFiles(prefix + path);
				if (files != null) {
					for (FTPFile file : client.listFiles(prefix + path)) {
						if (path.endsWith("/")) {
							id = path + file.getName();
						} else {
							id = path + "/" + file.getName();
						}
						commonFile = new CommonFile(id, displayPath, file);
						if (filter == null || filter.accept(commonFile)) {
							list.add(commonFile);
						}
					}
				}
			}
		});
		return list;
	}
}

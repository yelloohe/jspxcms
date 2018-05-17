package com.jspxcms.common.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.jspxcms.common.image.Images;

/**
 * WebFile
 * 
 * @author liufang
 * 
 */
public class WebFile {
	public static final FileFilter HIDDEN_FILTER = new HiddenFilter();
	private File file;
	private String root;
	private String displayPath;
	private FileFilter filter = HIDDEN_FILTER;
	private boolean isParent = false;
	private boolean isCurrent = false;

	public WebFile(File file) {
		this(file, "");
	}

	public WebFile(File file, String root) {
		this(file, root, "");
	}

	public WebFile(File file, String root, String displayPath) {
		this.file = file;
		this.root = root;
		this.displayPath = displayPath;
	}

	public WebFile getParentFile() {
		return new WebFile(file.getParentFile(), root, displayPath);
	}

	public String[] list() {
		return file.list();
	}

	public List<WebFile> listFiles() {
		return listFiles(filter);
	}

	public List<WebFile> listFiles(String search) {
		return listFiles(new HiddenFilter(search));
	}

	public List<WebFile> listFiles(FileFilter filter) {
		File[] files = file.listFiles(filter);
		List<WebFile> list = new LinkedList<WebFile>();
		if (ArrayUtils.isEmpty(files)) {
			return list;
		}
		WebFile wf;
		for (int i = 0, len = files.length; i < len; i++) {
			wf = new WebFile(files[i], root, displayPath);
			wf.setFileFilter(filter);
			list.add(wf);
		}
		return list;
	}

	public String getId() {
		String absPath = FilenameUtils.normalize(file.getAbsolutePath());
		String path = absPath.substring(root.length());
		path = StringUtils.replace(path, File.separator, "/");
		if (StringUtils.isBlank(path)) {
			path = "/";
		}
		return path;
	}

	public String getName() {
		if (isParent) {
			return "..";
		} else if (isCurrent) {
			return ".";
		} else {
			return file.getName();
		}
	}

	public String getShortName() {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index != -1) {
			return name.substring(0, index);
		} else {
			return name;
		}
	}

	public String getExtension() {
		return FilenameUtils.getExtension(file.getName());
	}

	public boolean isHasChildDir() {
		String[] names = file.list();
		if (names == null || names.length == 0) {
			return false;
		}
		for (int i = 0; i < names.length; i++) {
			File f = new File(file, names[i]);
			if (f.isDirectory()) {
				return true;
			}
		}
		return false;
	}

	public boolean isFile() {
		return file.isFile();
	}

	public boolean isDirectory() {
		return file.isDirectory();
	}

	public boolean isHidden() {
		return file.isHidden();
	}

	public long length() {
		return file.length();
	}

	public long getLength() {
		return file.length();
	}

	public long getLengthKB() {
		long length = file.length();
		long lengthKB = length / 1024;
		if (length % 1024 > 0) {
			lengthKB++;
		}
		return lengthKB;
	}

	public long lastModified() {
		return file.lastModified();
	}

	public Date getLastModified() {
		return new Date(file.lastModified());
	}

	public FileType getType() {
		if (file.isDirectory()) {
			return FileType.directory;
		}
		String extension = getExtension();
		if (Images.isImageExtension(extension)) {
			return FileType.image;
		} else if ("html".equalsIgnoreCase(extension)
				|| "htm".equalsIgnoreCase(extension)
				|| "js".equalsIgnoreCase(extension)
				|| "css".equalsIgnoreCase(extension)
				|| "txt".equalsIgnoreCase(extension)
				|| "xml".equalsIgnoreCase(extension)
				|| "ftl".equalsIgnoreCase(extension)
				|| "vm".equalsIgnoreCase(extension)
				|| "jsp".equalsIgnoreCase(extension)
				|| "jspx".equalsIgnoreCase(extension)
				|| "asp".equalsIgnoreCase(extension)
				|| "aspx".equalsIgnoreCase(extension)
				|| "php".equalsIgnoreCase(extension)
				|| "sql".equalsIgnoreCase(extension)
				|| "tag".equalsIgnoreCase(extension)
				|| "tld".equalsIgnoreCase(extension)
				|| "properties".equalsIgnoreCase(extension)) {
			return FileType.text;
		} else if ("zip".equalsIgnoreCase(extension)) {
			return FileType.zip;
		} else {
			return FileType.file;
		}
	}

	public String getText() throws IOException {
		if (getType() == FileType.text) {
			return FileUtils.readFileToString(file, "UTF-8");
		} else {
			return null;
		}
	}

	public String getUrl() {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(displayPath)) {
			sb.append(displayPath);
		}
		sb.append(getId());
		return sb.toString();
	}

	public boolean isEditable() {
		return getType() == FileType.text;
	}

	public boolean isImage() {
		return getType() == FileType.image;
	}

	public boolean isZip() {
		return getType() == FileType.zip;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setFileFilter(FileFilter filter) {
		this.filter = filter;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public static enum FileType {
		directory, zip, text, image, file
	}

	public static class HiddenFilter implements FileFilter {
		private String search;

		public HiddenFilter() {

		}

		public HiddenFilter(String search) {
			this.search = search;
		}

		public boolean accept(File pathname) {
			if (StringUtils.isNotBlank(search)) {
				String name = pathname.getName();
				return !pathname.isHidden()
						&& StringUtils.containsIgnoreCase(name, search);
			} else {
				return !pathname.isHidden();
			}
		}
	};

	public static Comparator<WebFile> NAME_ASC = new Comparator<WebFile>() {
		public int compare(WebFile file1, WebFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			}
			String name1 = file1.getName().toLowerCase();
			String name2 = file2.getName().toLowerCase();
			return name1.compareTo(name2);
		}
	};
	public static Comparator<WebFile> NAME_DESC = new Comparator<WebFile>() {
		public int compare(WebFile file1, WebFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return 1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return -1;
			}
			String name1 = file1.getName().toLowerCase();
			String name2 = file2.getName().toLowerCase();
			return -name1.compareTo(name2);
		}
	};
	public static Comparator<WebFile> LASTMODIFIED_ASC = new Comparator<WebFile>() {
		public int compare(WebFile file1, WebFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			}
			long lastModified1 = file1.lastModified();
			long lastModified2 = file2.lastModified();
			return (int) (lastModified1 - lastModified2);
		}
	};
	public static Comparator<WebFile> LASTMODIFIED_DESC = new Comparator<WebFile>() {
		public int compare(WebFile file1, WebFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			}
			long lastModified1 = file1.lastModified();
			long lastModified2 = file2.lastModified();
			return -(int) (lastModified1 - lastModified2);
		}
	};
	public static Comparator<WebFile> TYPE_ASC = new Comparator<WebFile>() {
		public int compare(WebFile file1, WebFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			} else if (file1.isDirectory() && file2.isDirectory()) {
				String name1 = file1.getName().toLowerCase();
				String name2 = file2.getName().toLowerCase();
				return name1.compareTo(name2);
			}
			FileType type1 = file1.getType();
			FileType type2 = file2.getType();
			int num = type1.compareTo(type2);
			if (num == 0) {
				String ext1 = file1.getExtension();
				String ext2 = file2.getExtension();
				num = ext1.compareTo(ext2);
			}
			return num;
		}
	};
	public static Comparator<WebFile> TYPE_DESC = new Comparator<WebFile>() {
		public int compare(WebFile file1, WebFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			} else if (file1.isDirectory() && file2.isDirectory()) {
				String name1 = file1.getName().toLowerCase();
				String name2 = file2.getName().toLowerCase();
				return -name1.compareTo(name2);
			}
			FileType type1 = file1.getType();
			FileType type2 = file2.getType();
			int num = type1.compareTo(type2);
			if (num == 0) {
				String ext1 = file1.getExtension();
				String ext2 = file2.getExtension();
				num = ext1.compareTo(ext2);
			}
			return -num;
		}
	};

	public static Comparator<WebFile> LENGTH_ASC = new Comparator<WebFile>() {
		public int compare(WebFile file1, WebFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			} else if (file1.isDirectory() && file2.isDirectory()) {
				return file1.getName().compareTo(file2.getName());
			}
			long length1 = file1.length();
			long length2 = file2.length();
			return (int) (length1 - length2);
		}
	};
	public static Comparator<WebFile> LENGTH_DESC = new Comparator<WebFile>() {
		public int compare(WebFile file1, WebFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return 1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return -1;
			} else if (file1.isDirectory() && file2.isDirectory()) {
				return file1.getName().compareTo(file2.getName());
			}
			long length1 = file1.length();
			long length2 = file2.length();
			return -(int) (length1 - length2);
		}
	};

	public static void sort(List<WebFile> list, String sort,
			String directionection) {
		Comparator<WebFile> comp;
		if ("name".equals(sort)) {
			comp = "desc".equals(directionection) ? NAME_DESC : NAME_ASC;
		} else if ("lastModified".equals(sort)) {
			comp = "desc".equals(directionection) ? LASTMODIFIED_DESC
					: LASTMODIFIED_ASC;
		} else if ("type".equals(sort)) {
			comp = "desc".equals(directionection) ? TYPE_DESC : TYPE_ASC;
		} else if ("length".equals(sort)) {
			comp = "desc".equals(directionection) ? LENGTH_DESC : LENGTH_ASC;
		} else {
			comp = NAME_ASC;
		}
		Collections.sort(list, comp);
	}

}

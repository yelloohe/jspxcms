package com.jspxcms.common.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;

import com.jspxcms.common.image.Images;

/**
 * CommonFile
 * 
 * @author liufang
 * 
 */
public class CommonFile {
	/**
	 * 获取上级文件路径
	 * 
	 * @param path
	 * @return
	 */
	public static String getParent(String path) {
		if (path == null) {
			return null;
		}
		int index = path.lastIndexOf('/', path.length() - 2);
		if (index != -1) {
			return path.substring(0, index);
		} else {
			return null;
		}
	}

	private String id;
	private boolean directory;
	private long length;
	private Date lastModified;
	private String text;

	private String displayPath;
	private boolean parent = false;
	private boolean current = false;

	public CommonFile(String id) {
		this(id, "");
	}

	public CommonFile(String id, String displayPath) {
		this.id = id;
		this.displayPath = displayPath;
	}

	public CommonFile(String id, boolean directory) {
		this(id, "", directory);
	}

	public CommonFile(String id, String displayPath, boolean directory) {
		this.id = id;
		this.displayPath = displayPath;
		this.directory = directory;
	}

	public CommonFile(String id, String displayPath, File file) {
		this.id = id;
		this.displayPath = displayPath;
		this.fillWithFile(file);
	}

	public CommonFile(String id, String displayPath, FTPFile file) {
		this.id = id;
		this.displayPath = displayPath;
		this.fillWithFtp(file);
	}

	public void fillWithFile(File file) {
		this.setDirectory(file.isDirectory());
		this.setLastModified(new Date(file.lastModified()));
		this.setLength(file.length());
	}

	public void fillWithFtp(FTPFile file) {
		this.setDirectory(file.isDirectory());
		this.setLastModified(file.getTimestamp().getTime());
		this.setLength(file.getSize());
	}

	public String getId() {
		if (StringUtils.isBlank(this.id)) {
			return "/";
		}
		return this.id;
	}

	public String getName() {
		if (isParent()) {
			return "..";
		} else if (isCurrent()) {
			return ".";
		} else {
			return FilenameUtils.getName(id);
		}
	}

	public String getBaseName() {
		return FilenameUtils.getBaseName(id);
	}

	public String getExtension() {
		return FilenameUtils.getExtension(id);
	}

	public String getParent() {
		int index = getId().lastIndexOf('/', 1);
		return getId().substring(0, index);
	}

	public boolean isFile() {
		return !directory;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getLengthKB() {
		long lengthKB = length / 1024;
		if (length % 1024 > 0) {
			lengthKB++;
		}
		return lengthKB;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public FileType getType() {
		if (isDirectory()) {
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
		return parent;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
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

	public static Comparator<CommonFile> NAME_ASC = new Comparator<CommonFile>() {
		public int compare(CommonFile file1, CommonFile file2) {
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
	public static Comparator<CommonFile> NAME_DESC = new Comparator<CommonFile>() {
		public int compare(CommonFile file1, CommonFile file2) {
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
	public static Comparator<CommonFile> LASTMODIFIED_ASC = new Comparator<CommonFile>() {
		public int compare(CommonFile file1, CommonFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			}
			long lastModified1 = file1.getLastModified().getTime();
			long lastModified2 = file2.getLastModified().getTime();
			return (int) (lastModified1 - lastModified2);
		}
	};
	public static Comparator<CommonFile> LASTMODIFIED_DESC = new Comparator<CommonFile>() {
		public int compare(CommonFile file1, CommonFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			}
			long lastModified1 = file1.getLastModified().getTime();
			long lastModified2 = file2.getLastModified().getTime();
			return -(int) (lastModified1 - lastModified2);
		}
	};
	public static Comparator<CommonFile> TYPE_ASC = new Comparator<CommonFile>() {
		public int compare(CommonFile file1, CommonFile file2) {
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
	public static Comparator<CommonFile> TYPE_DESC = new Comparator<CommonFile>() {
		public int compare(CommonFile file1, CommonFile file2) {
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

	public static Comparator<CommonFile> LENGTH_ASC = new Comparator<CommonFile>() {
		public int compare(CommonFile file1, CommonFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return 1;
			} else if (file1.isDirectory() && file2.isDirectory()) {
				return file1.getName().compareTo(file2.getName());
			}
			long length1 = file1.getLength();
			long length2 = file2.getLength();
			return (int) (length1 - length2);
		}
	};
	public static Comparator<CommonFile> LENGTH_DESC = new Comparator<CommonFile>() {
		public int compare(CommonFile file1, CommonFile file2) {
			if (file1.isDirectory() && !file2.isDirectory()) {
				return 1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return -1;
			} else if (file1.isDirectory() && file2.isDirectory()) {
				return file1.getName().compareTo(file2.getName());
			}
			long length1 = file1.getLength();
			long length2 = file2.getLength();
			return -(int) (length1 - length2);
		}
	};

	public static void sort(List<CommonFile> list, String sort,
			String directionection) {
		Comparator<CommonFile> comp;
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

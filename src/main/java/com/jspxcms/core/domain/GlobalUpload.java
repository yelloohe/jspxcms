package com.jspxcms.core.domain;

import com.jspxcms.common.image.ScaleParam;
import com.jspxcms.core.support.Configurable;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 上传配置对象
 * 
 * @author liufang
 *
 */
public class GlobalUpload implements Configurable {
	public static final String PREFIX = "sys_upload_";

	public static final String EXTENSIONS = "Extensions";
	public static final String LIMIT = "Limit";

	// 7z,aiff,asf,avi,bmp,csv,doc,fla,flv,gif,gz,gzip,jpeg,jpg,mid,mov,mp3,mp4,mpc,mpeg,mpg,ods,odt,pdf,png,ppt,pxd,qt,ram,rar,rm,rmi,rmvb,rtf,sdc,sitd,swf,sxc,sxw,tar,tgz,tif,tiff,txt,vsd,wav,wma,wmv,xls,xml,zip
	public static final String FILE_EXTENSIONS = PREFIX + "file" + EXTENSIONS;
	public static final String IMAGE_EXTENSIONS = PREFIX + "image" + EXTENSIONS;
	public static final String FLASH_EXTENSIONS = PREFIX + "flash" + EXTENSIONS;
	public static final String VIDEO_EXTENSIONS = PREFIX + "video" + EXTENSIONS;
	public static final String DOC_EXTENSIONS = PREFIX + "doc" + EXTENSIONS;

	public static final String FILE_LIMIT = PREFIX + "file" + LIMIT;
	public static final String IMAGE_LIMIT = PREFIX + "image" + LIMIT;
	public static final String FLASH_LIMIT = PREFIX + "flash" + LIMIT;
	public static final String VIDEO_LIMIT = PREFIX + "video" + LIMIT;
	public static final String DOC_LIMIT = PREFIX + "doc" + LIMIT;

	public static final String IMAGE_MAX_WIDTH = PREFIX + "imageMaxWidth";
	public static final String IMAGE_MAX_HEIGHT = PREFIX + "imageMaxHeight";

	private Map<String, String> customs;

	public GlobalUpload() {
	}

	public GlobalUpload(Map<String, String> customs) {
		this.customs = customs;
	}

	public ScaleParam getScaleParam(Boolean scale, Boolean exact, Integer width, Integer height) {
		ScaleParam scaleInfo;
		if (scale == null) {
			width = getImageMaxWidth();
			height = getImageMaxHeight();
			scale = (width != null && width > 0) || (height != null && height > 0);
			scaleInfo = new ScaleParam(scale, false, width, height);
		} else {
			scaleInfo = new ScaleParam(scale, exact, width, height);
		}
		return scaleInfo;
	}

	public boolean isFileExtensionValid(String extension) {
		return isValid(extension, getFileExtensions());
	}

	public boolean isImageExtensionValid(String extension) {
		return isValid(extension, getImageExtensions());

	}

	public boolean isFlashExtensionValid(String extension) {
		return isValid(extension, getFlashExtensions());

	}

	public boolean isVideoExtensionValid(String extension) {
		return isValid(extension, getVideoExtensions());
	}

	public boolean isDocExtensionValid(String extension) {
		return isValid(extension, getDocExtensions());
	}

	public boolean isExtensionValid(String extension, String type) {
		return isValid(extension, getExtensions(type));
	}

	private boolean isValid(String extension, String allowed) {
		if (StringUtils.isNotBlank(allowed)) {
			for (String a : StringUtils.split(allowed, ',')) {
				if (a.equalsIgnoreCase(extension)) {
					return true;
				}
			}
			return false;
		}
		return true;

	}

	public String getExtensions(String type) {
		return getCustoms().get(PREFIX + type + EXTENSIONS);
	}

	public String getFileTypes() {
		return getTypes(getFileExtensions());
	}

	public String getImageTypes() {
		return getTypes(getImageExtensions());
	}

	public String getFlashTypes() {
		return getTypes(getFlashExtensions());
	}

	public String getVideoTypes() {
		return getTypes(getVideoExtensions());
	}

	public String getDocTypes() {
		return getTypes(getDocExtensions());
	}

	public String getTypes(String ext) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(ext)) {
            sb.append(" /(\\.|\\/)(");
            sb.append(StringUtils.replace(ext, ",", "|"));
            sb.append(")$/i");
		} else {
			sb.append("undefined");
		}
		return sb.toString();
	}

	public String getFileExtensions() {
		return getCustoms().get(FILE_EXTENSIONS);
	}

	public void setFileExtensions(String fileExtensions) {
		if (StringUtils.isNotBlank(fileExtensions)) {
			getCustoms().put(FILE_EXTENSIONS, fileExtensions);
		} else {
			getCustoms().remove(FILE_EXTENSIONS);
		}
	}

	public String getImageExtensions() {
		return getCustoms().get(IMAGE_EXTENSIONS);
	}

	public void setImageExtensions(String imageExtensions) {
		if (StringUtils.isNotBlank(imageExtensions)) {
			getCustoms().put(IMAGE_EXTENSIONS, imageExtensions);
		} else {
			getCustoms().remove(IMAGE_EXTENSIONS);
		}
	}

	public String getFlashExtensions() {
		return getCustoms().get(FLASH_EXTENSIONS);
	}

	public void setFlashExtensions(String flashExtensions) {
		if (StringUtils.isNotBlank(flashExtensions)) {
			getCustoms().put(FLASH_EXTENSIONS, flashExtensions);
		} else {
			getCustoms().remove(FLASH_EXTENSIONS);
		}
	}

	public String getVideoExtensions() {
		return getCustoms().get(VIDEO_EXTENSIONS);
	}

	public void setVideoExtensions(String videoExtensions) {
		if (StringUtils.isNotBlank(videoExtensions)) {
			getCustoms().put(VIDEO_EXTENSIONS, videoExtensions);
		} else {
			getCustoms().remove(VIDEO_EXTENSIONS);
		}
	}

	public String getDocExtensions() {
		return getCustoms().get(DOC_EXTENSIONS);
	}

	public void setDocExtensions(String docExtensions) {
		if (StringUtils.isNotBlank(docExtensions)) {
			getCustoms().put(DOC_EXTENSIONS, docExtensions);
		} else {
			getCustoms().remove(DOC_EXTENSIONS);
		}
	}

	public int getFileLimit() {
		String value = getCustoms().get(FILE_LIMIT);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	public void setFileLimit(Integer fileLimit) {
		if (fileLimit != null && fileLimit > 0) {
			getCustoms().put(FILE_LIMIT, fileLimit.toString());
		} else {
			getCustoms().remove(FILE_LIMIT);
		}
	}

	public int getImageLimit() {
		String value = getCustoms().get(IMAGE_LIMIT);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	public void setImageLimit(Integer imageLimit) {
		if (imageLimit != null && imageLimit > 0) {
			getCustoms().put(IMAGE_LIMIT, imageLimit.toString());
		} else {
			getCustoms().remove(IMAGE_LIMIT);
		}
	}

	public int getFlashLimit() {
		String value = getCustoms().get(FLASH_LIMIT);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	public void setFlashLimit(Integer flashLimit) {
		if (flashLimit != null && flashLimit > 0) {
			getCustoms().put(FLASH_LIMIT, flashLimit.toString());
		} else {
			getCustoms().remove(FLASH_LIMIT);
		}
	}

	public int getVideoLimit() {
		String value = getCustoms().get(VIDEO_LIMIT);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	public void setVideoLimit(Integer videoLimit) {
		if (videoLimit != null && videoLimit > 0) {
			getCustoms().put(VIDEO_LIMIT, videoLimit.toString());
		} else {
			getCustoms().remove(VIDEO_LIMIT);
		}
	}

	public int getDocLimit() {
		String value = getCustoms().get(DOC_LIMIT);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	public void setDocLimit(Integer docLimit) {
		if (docLimit != null && docLimit > 0) {
			getCustoms().put(DOC_LIMIT, docLimit.toString());
		} else {
			getCustoms().remove(DOC_LIMIT);
		}
	}

	public int getImageMaxWidth() {
		String value = getCustoms().get(IMAGE_MAX_WIDTH);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	public void setImageMaxWidth(Integer imageMaxWidth) {
		if (imageMaxWidth != null && imageMaxWidth > 0) {
			getCustoms().put(IMAGE_MAX_WIDTH, imageMaxWidth.toString());
		} else {
			getCustoms().remove(IMAGE_MAX_WIDTH);
		}
	}

	public int getImageMaxHeight() {
		String value = getCustoms().get(IMAGE_MAX_HEIGHT);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	public void setImageMaxHeight(Integer imageMaxHeight) {
		if (imageMaxHeight != null && imageMaxHeight > 0) {
			getCustoms().put(IMAGE_MAX_HEIGHT, imageMaxHeight.toString());
		} else {
			getCustoms().remove(IMAGE_MAX_HEIGHT);
		}
	}

	public Map<String, String> getCustoms() {
		if (customs == null) {
			customs = new HashMap<String, String>();
		}
		return customs;
	}

	public void setCustoms(Map<String, String> customs) {
		this.customs = customs;
	}

	public String getPrefix() {
		return PREFIX;
	}
}

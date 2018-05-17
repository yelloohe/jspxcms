package com.jspxcms.common.image;

public class ThumbnailParam {

	private Boolean thumbnail;
	private Integer width;
	private Integer height;

	public ThumbnailParam(Boolean thumbnail, Integer width, Integer height) {
		this.thumbnail = thumbnail;
		this.width = width;
		this.height = height;
	}

	public boolean isThumbnail() {
		return thumbnail != null && thumbnail && (width != null || height != null);
	}

	public void setThumbnail(Boolean thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

}

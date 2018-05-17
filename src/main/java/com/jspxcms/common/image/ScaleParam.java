package com.jspxcms.common.image;

public class ScaleParam {
	private Boolean scale;
	private Boolean exact;
	private Integer width;
	private Integer height;

	public ScaleParam(Boolean scale, Boolean exact, Integer width, Integer height) {
		setScale(scale);
		setExact(exact);
		setWidth(width);
		setHeight(height);
	}

	public boolean isScale() {
		return scale != null && scale && (width != null || height != null);
	}

	public void setScale(Boolean scale) {
		this.scale = scale;
	}

	public boolean isExact() {
		return exact != null ? exact : false;
	}

	public void setExact(Boolean exact) {
		this.exact = exact;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		if (width != null && width <= 0) {
			width = null;
		}
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		if (height != null && height <= 0) {
			height = null;
		}
		this.height = height;
	}

}

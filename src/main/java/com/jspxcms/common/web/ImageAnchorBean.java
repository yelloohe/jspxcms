package com.jspxcms.common.web;

/**
 * html img 实体
 * 
 * @author liufang
 * 
 */
public class ImageAnchorBean implements ImageAnchor {
	private String title;
	private String url;
	private Boolean newWindow;
	private String src;
	private Integer width;
	private Integer height;

	public ImageAnchorBean() {

	}

	public ImageAnchorBean(String title, String url, Boolean newWindow,
			String src, Integer width, Integer height) {
		this.title = title;
		this.url = url;
		this.newWindow = newWindow;
		this.src = src;
		this.width = width;
		this.height = height;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getNewWindow() {
		return newWindow;
	}

	public void setNewWindow(Boolean newWindow) {
		this.newWindow = newWindow;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
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

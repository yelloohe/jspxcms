package com.jspxcms.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.jspxcms.core.support.UploadUtils;

/**
 * InfoImage
 * 
 * @author liufang
 * 
 */
@Embeddable
public class InfoImage implements Serializable {
	private static final long serialVersionUID = 1L;

	public InfoImage() {
	}

	public InfoImage(String name, String text, String image) {
		this.name = name;
		this.text = text;
		this.image = image;
	}

	@Transient
	public String getImageMin() {
		String image = getImage();
		if (StringUtils.isBlank(image)) {
			return image;
		}
		return UploadUtils.getThumbnailPath(image);
	}

	private String name;
	private String image;
	private String text;

	@Column(name = "f_name", length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_image")
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "f_text")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

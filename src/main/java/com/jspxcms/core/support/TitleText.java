package com.jspxcms.core.support;

import java.util.List;

import javax.persistence.Transient;

/**
 * TitleText
 * 
 * @author liufang
 * 
 */
public class TitleText {
	private String title;
	private String text;

	public TitleText() {

	}

	public TitleText(String title, String text) {
		this.title = title;
		this.text = text;
	}

	@Transient
	public static TitleText getTitleText(List<TitleText> textList, Integer page) {
		if (page == null || page < 1) {
			return textList.get(0);
		} else if (page >= textList.size()) {
			return textList.get(textList.size() - 1);
		} else {
			return textList.get(page - 1);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

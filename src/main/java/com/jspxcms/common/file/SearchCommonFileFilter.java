package com.jspxcms.common.file;

import org.apache.commons.lang3.StringUtils;

public class SearchCommonFileFilter implements CommonFileFilter {
	private String search;

	public SearchCommonFileFilter(String search) {
		this.search = search;
	}

	public boolean accept(CommonFile webFile) {
		return StringUtils.isBlank(search)
				|| StringUtils.contains(webFile.getName(), search);
	}
}

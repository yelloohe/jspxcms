package com.jspxcms.core.service;

import com.jspxcms.core.domain.Info;

public interface InfoTagService {
	public void update(Info info, String[] tagNames);

	public void deleteByTagId(Integer tagId);
}

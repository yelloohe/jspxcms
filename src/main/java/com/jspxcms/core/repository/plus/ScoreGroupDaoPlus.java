package com.jspxcms.core.repository.plus;

import com.jspxcms.core.domain.ScoreGroup;

public interface ScoreGroupDaoPlus {
	public ScoreGroup findTopOne(Integer siteId);
}

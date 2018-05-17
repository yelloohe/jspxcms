package com.jspxcms.core.service;

import com.jspxcms.core.domain.User;

public interface UserOrgService {
	public void update(User user, Integer[] orgIds, Integer orgId, Integer topOrgId);
}

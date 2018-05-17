package com.jspxcms.core.service;

import java.util.List;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserRole;

public interface UserRoleService {
	public List<UserRole> update(User user, Integer[] roleIds, Integer siteId);
}

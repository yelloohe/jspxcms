package com.jspxcms.core.service;

import java.util.List;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserMemberGroup;

public interface UserMemberGroupService {
	public List<UserMemberGroup> update(User user, Integer[] groupIds, Integer groupId);
}

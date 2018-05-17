package com.jspxcms.core.service;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserDetail;

public interface UserDetailService {
	public UserDetail get(Integer id);

	public UserDetail save(UserDetail detail, User user, String ip);

	public UserDetail update(UserDetail detail);
}

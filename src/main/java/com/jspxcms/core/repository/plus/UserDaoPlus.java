package com.jspxcms.core.repository.plus;

import java.util.List;

import com.jspxcms.core.domain.User;

/**
 * UserDaoPlus
 * 
 * @author liufang
 * 
 */
public interface UserDaoPlus {
	public List<User> findByUsername(String[] usernames);
}

package com.jspxcms.core.repository;

import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.UserDetail;

public interface UserDetailDao extends Repository<UserDetail, Integer> {
	public UserDetail findOne(Integer id);

	public UserDetail save(UserDetail bean);

	public void delete(UserDetail bean);

	// --------------------

}

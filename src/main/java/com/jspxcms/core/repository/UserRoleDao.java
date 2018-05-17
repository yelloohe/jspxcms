package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserRole;
import com.jspxcms.core.domain.UserRole.UserRoleId;

public interface UserRoleDao extends Repository<UserRole, UserRoleId> {
	public UserRole findOne(UserRoleId id);

	void delete(UserRole entity);

	// --------------------
	
	/**
	 * 根据角色 ID查询用户列表
	 * 
	 * @param nodeId
	 * @return
	 */
	@Query("from User bean join bean.userRoles userRole where userRole.role.id = ?1")
	public List<User> findByUserRolesRoleId(Integer roleId);

	// @Modifying
	// @Query("delete from UserRole bean where bean.user.id=?1")
	// public int deleteByUserId(Integer userId);

	// @Modifying
	// @Query("delete from UserRole bean where bean.role.id=?1")
	// public int deleteByRoleId(Integer roleId);
}

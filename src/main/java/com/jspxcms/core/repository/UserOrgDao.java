package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserOrg;
import com.jspxcms.core.domain.UserOrg.UserOrgId;

public interface UserOrgDao extends Repository<UserOrg, UserOrgId> {
	public UserOrg findOne(UserOrgId id);

	void delete(UserOrg entity);

	// --------------------
	
	/**
	 * 根据组织 ID查询用户列表
	 * 
	 * @param nodeId
	 * @return
	 */
	@Query("from User bean join bean.userOrgs userOrg where userOrg.org.id = ?1")
	public List<User> findByUserOrgsOrgId(Integer orgId);

//	@Modifying
//	@Query("delete from UserOrg bean where bean.user.id=?1")
//	public int deleteByUserId(Integer userId);

	// @Modifying
	// @Query("delete from UserOrg bean where bean.org.id=?1")
	// public int deleteByOrgId(Integer orgId);
}

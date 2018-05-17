package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserMemberGroup;
import com.jspxcms.core.domain.UserMemberGroup.UserMemberGroupId;

public interface UserMemberGroupDao extends Repository<UserMemberGroup, UserMemberGroupId> {
	public UserMemberGroup findOne(UserMemberGroupId id);
	
	void delete(UserMemberGroup entity);

	// --------------------
	
	/**
	 * 根据会员组 ID查询用户列表
	 * 
	 * @param nodeId
	 * @return
	 */
	@Query("from User bean join bean.userGroups userGroup where userGroup.group.id = ?1")
	public List<User> findByUserGroupsGroupId(Integer groupId);

//	@Modifying
//	@Query("delete from UserMemberGroup bean where bean.user.id=?1")
//	public int deleteByUserId(Integer userId);

//	@Modifying
//	@Query("delete from UserMemberGroup bean where bean.group.id=?1")
//	public int deleteByGroupId(Integer groupId);
}

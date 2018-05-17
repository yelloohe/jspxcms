package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.NodeMemberGroup;
import com.jspxcms.core.domain.NodeMemberGroup.NodeMemberGroupId;

public interface NodeMemberGroupDao extends Repository<NodeMemberGroup, NodeMemberGroupId> {
	public NodeMemberGroup findOne(NodeMemberGroupId id);

	// --------------------

	public List<NodeMemberGroup> findByNodeIdAndGroupId(Integer nodeId, Integer groupId);

	public List<NodeMemberGroup> findByNodeId(Integer nodeId);

	public List<NodeMemberGroup> findByGroupId(Integer groupId);

	@Modifying
	@Query("delete from NodeMemberGroup bean where bean.node.id=?1")
	public int deleteByNodeId(Integer nodeId);

	@Modifying
	@Query("delete from NodeMemberGroup bean where bean.group.id=?1")
	public int deleteByGroupId(Integer groupId);
}

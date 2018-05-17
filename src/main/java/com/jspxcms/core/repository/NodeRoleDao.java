package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.NodeRole;
import com.jspxcms.core.domain.NodeRole.NodeRoleId;

public interface NodeRoleDao extends Repository<NodeRole, NodeRoleId> {
	public NodeRole findOne(NodeRoleId id);

	// --------------------

	public List<NodeRole> findByNodeIdAndRoleId(Integer nodeId, Integer roleId);

	public List<NodeRole> findByNodeId(Integer nodeId);

	public List<NodeRole> findByRoleId(Integer roleId);

	@Modifying
	@Query("delete from NodeRole bean where bean.node.id=?1")
	public int deleteByNodeId(Integer nodeId);

	@Modifying
	@Query("delete from NodeRole bean where bean.role.id=?1")
	public int deleteByRoleId(Integer roleId);
}

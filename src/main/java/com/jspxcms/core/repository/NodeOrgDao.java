package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.NodeOrg;
import com.jspxcms.core.domain.NodeOrg.NodeOrgId;

public interface NodeOrgDao extends Repository<NodeOrg, NodeOrgId> {
	public NodeOrg findOne(NodeOrgId id);

	// --------------------

	public List<NodeOrg> findByNodeId(Integer nodeId);

	@Modifying
	@Query("delete from NodeOrg bean where bean.node.id = ?1")
	public int deleteByNodeId(Integer nodeId);

	@Modifying
	@Query("delete from NodeOrg bean where bean.org.id = ?1")
	public int deleteByOrgId(Integer orgId);

}

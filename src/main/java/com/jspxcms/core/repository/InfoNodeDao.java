package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoNode;
import com.jspxcms.core.domain.InfoNode.InfoNodeId;

public interface InfoNodeDao extends Repository<InfoNode, InfoNodeId> {
	public InfoNode findOne(InfoNodeId id);

	void delete(InfoNode entity);

	// --------------------

	/**
	 * 根据扩展栏目ID查询
	 * 
	 * @param nodeId
	 * @return
	 */
	@Query("from Info bean join bean.infoNodes infonode where infonode.node.id = ?1")
	public List<Info> findByNodesNodeId(Integer nodeId);

	@Modifying
	@Query("update InfoNode bean set bean.node.id=?2 where bean.node.id in (?1)")
	public int moveByNodeId(Collection<Integer> nodeIds, Integer nodeId);

	// @Modifying
	// @Query("delete from InfoNode bean where bean.info.id=?1")
	// public int deleteByInfoId(Integer infoId);

	// 不能直接删除InfoNode数据，会导致f_node_index出错
	// @Modifying
	// @Query("delete from InfoNode bean where bean.node.id=?1")
	// public int deleteByNodeId(Integer nodeId);
}

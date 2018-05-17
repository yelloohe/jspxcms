package com.jspxcms.core.repository;

import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.NodeDetail;

/**
 * NodeDetailDao
 * 
 * @author liufang
 * 
 */
public interface NodeDetailDao extends Repository<NodeDetail, Integer> {
	public NodeDetail findOne(Integer id);

	public NodeDetail save(NodeDetail bean);
}

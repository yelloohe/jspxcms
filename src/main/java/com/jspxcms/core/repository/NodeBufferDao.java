package com.jspxcms.core.repository;

import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.NodeBuffer;

/**
 * NodeBufferDao
 * 
 * @author liufang
 * 
 */
public interface NodeBufferDao extends Repository<NodeBuffer, Integer> {
	public NodeBuffer findOne(Integer id);

	public NodeBuffer save(NodeBuffer bean);
}

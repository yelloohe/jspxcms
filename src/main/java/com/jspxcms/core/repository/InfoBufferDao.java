package com.jspxcms.core.repository;

import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.InfoBuffer;

/**
 * InfoBufferDao
 * 
 * @author liufang
 * 
 */
public interface InfoBufferDao extends Repository<InfoBuffer, Integer> {
	public InfoBuffer findOne(Integer id);

	public InfoBuffer save(InfoBuffer bean);
}

package com.jspxcms.core.repository;

import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.Global;

/**
 * GlobalDao
 * 
 * @author liufang
 * 
 */
public interface GlobalDao extends Repository<Global, Integer> {
	public Global findOne(Integer id);

	public Global save(Global entity);
}
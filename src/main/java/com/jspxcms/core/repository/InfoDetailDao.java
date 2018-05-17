package com.jspxcms.core.repository;

import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.InfoDetail;

/**
 * InfoDetailDao
 * 
 * @author liufang
 * 
 */
public interface InfoDetailDao extends Repository<InfoDetail, Integer> {
	public InfoDetail findOne(Integer id);

	public InfoDetail save(InfoDetail bean);

}

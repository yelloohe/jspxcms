package com.jspxcms.core.repository;

import java.util.List;

/**
 * SQLDao
 * 
 * @author liufang
 * 
 */
public interface SQLDao {
	public List<?> query(String sql, Integer maxRows, Integer startRow);

	public int update(String sql);
}

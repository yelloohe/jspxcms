package com.jspxcms.core.service;

import java.util.List;

/**
 * SQLService
 * 
 * @author liufang
 * 
 */
public interface SQLService {
	public List<?> query(String sql, Integer maxRows, Integer startRow);

	public int update(String sql);
}

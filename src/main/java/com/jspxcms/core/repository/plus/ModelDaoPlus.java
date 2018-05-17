package com.jspxcms.core.repository.plus;

import java.util.List;

import com.jspxcms.core.domain.Model;

/**
 * ModelDaoPlus
 * 
 * @author liufang
 * 
 */
public interface ModelDaoPlus {
	public List<Model> findList(Integer siteId, String type);

	public Model findDefault(Integer siteId, String type);

	public List<Model> findByNumbers(String[] numbers, Integer[] siteIds);
}

package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.SpecialCategory;

/**
 * SpecialCategoryService
 * 
 * @author liufang
 * 
 */
public interface SpecialCategoryService {
	public List<SpecialCategory> findList(Integer siteId,
			Map<String, String[]> params, Sort sort);

	public RowSide<SpecialCategory> findSide(Integer siteId,
			Map<String, String[]> params, SpecialCategory bean,
			Integer position, Sort sort);

	public List<SpecialCategory> findList(Integer siteId);

	public List<SpecialCategory> findList(Integer[] siteId, Limitable limitable);

	public SpecialCategory get(Integer id);

	public SpecialCategory save(SpecialCategory bean, Integer siteId);

	public SpecialCategory update(SpecialCategory bean);

	public SpecialCategory[] batchUpdate(Integer[] id, String[] name,
			Integer[] views);

	public SpecialCategory delete(Integer id);

	public SpecialCategory[] delete(Integer[] ids);
}

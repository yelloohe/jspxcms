package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Role;

/**
 * RoleService
 * 
 * @author liufang
 * 
 */
public interface RoleService {
	public List<Role> findList(Integer siteId, Map<String, String[]> params,
			Sort sort);

	public RowSide<Role> findSide(Integer siteId, Map<String, String[]> params,
			Role bean, Integer position, Sort sort);

	public List<Role> findList(Integer siteId);

	public Role get(Integer id);

	public Role save(Role bean, Integer[] infoPermIds, Integer[] nodePermIds,
			Integer siteId);

	public Role update(Role bean, Integer[] infoPermIds, Integer[] nodePermIds);

	public List<Role> batchUpdate(Integer[] id, String[] name, Integer[] rank,
			String[] description);

	public Role delete(Integer id);

	public Role[] delete(Integer[] ids);
}

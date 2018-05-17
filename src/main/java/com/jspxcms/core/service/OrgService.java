package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Org;

/**
 * OrgService
 * 
 * @author liufang
 * 
 */
public interface OrgService {
	public List<Org> findList(String topTreeNumber, Integer parentId,
			boolean showDescendants, Map<String, String[]> params, Sort sort);

	public RowSide<Org> findSide(String topTreeNumber, Integer parentId,
			boolean showDescendants, Map<String, String[]> params, Org bean,
			Integer position, Sort sort);

	public List<Org> findList();

	public List<Org> findList(String treeNumber);

	public Org findRoot();

	public Org get(Integer id);

	public Org save(Org bean, Integer parentId);

	public Org update(Org bean, Integer parentId);

	public Org[] batchUpdate(Integer[] id, String[] name, String[] number,
			String[] phone, String[] address, boolean isUpdateTree);

	public Org delete(Integer id);

	public Org[] delete(Integer[] ids);

	public int move(Integer[] ids, Integer id);
}

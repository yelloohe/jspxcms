package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.MemberGroup;

/**
 * MemberGroupService
 * 
 * @author liufang
 * 
 */
public interface MemberGroupService {
	public List<MemberGroup> findList(Map<String, String[]> params, Sort sort);

	public RowSide<MemberGroup> findSide(Map<String, String[]> params,
			MemberGroup bean, Integer position, Sort sort);

	public List<MemberGroup> findList();

	public List<MemberGroup> findRealGroups();

	public List<MemberGroup> findList(Integer[] type);

	public MemberGroup getAnonymous();

	public MemberGroup get(Integer id);

	public MemberGroup save(MemberGroup bean, Integer[] viewNodeIds,
			Integer[] contriNodeIds, Integer[] commentNodeIds, Integer siteId);

	public MemberGroup update(MemberGroup bean, Integer[] viewNodeIds,
			Integer[] contriNodeIds, Integer[] commentNodeIds, Integer siteId);

	public MemberGroup delete(Integer id);

	public MemberGroup[] delete(Integer[] ids);
}

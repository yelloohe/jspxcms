package com.jspxcms.ext.service;

import java.util.List;
import java.util.Map;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.ext.domain.Friendlink;

/**
 * FriendlinkService
 * 
 * @author yangxing
 * 
 */

public interface FriendlinkService {
	public List<Friendlink> findList(Integer siteId,
			Map<String, String[]> params);

	public RowSide<Friendlink> findSide(Integer siteId,
			Map<String, String[]> params, Friendlink bean, Integer position);

	public List<Friendlink> findList(Integer[] siteId, String[] type,
			Integer[] typeId, Boolean isWithLogo, Boolean isRecommend,
			Integer[] status, Limitable limitable);

	public Friendlink[] batchUpdate(Integer[] id);

	public Friendlink get(Integer id);

	public Friendlink save(Friendlink bean, Integer typeId, Integer siteId);

	public Friendlink update(Friendlink bean, Integer typeId);

	public Friendlink delete(Integer id);

	public Friendlink[] delete(Integer[] ids);
}

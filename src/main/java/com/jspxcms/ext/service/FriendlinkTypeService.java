package com.jspxcms.ext.service;

import java.util.List;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.FriendlinkType;

/**
 * FriendlinkTypeService
 * 
 * @author yangxing
 * 
 */
public interface FriendlinkTypeService {
	public List<FriendlinkType> findList(Integer siteId);
	
	public List<FriendlinkType> findList(Integer[] siteId, Limitable limitable);
	
	public FriendlinkType get(Integer id);

	public FriendlinkType save(FriendlinkType bean, Integer siteId);

	public FriendlinkType update(FriendlinkType bean);

	public FriendlinkType[] batchUpdate(Integer[] id, String[] name,String[] number);
	
	public FriendlinkType delete(Integer id);

	public FriendlinkType[] delete(Integer[] ids);
}

package com.jspxcms.ext.repository.plus;

import java.util.List;


import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.FriendlinkType;
/**
 * FriendlinkTypeDaoPlus
 * 
 * @author yangxing
 * 
 */
public interface FriendlinkTypeDaoPlus {
	public List<FriendlinkType> getList(Integer[] siteId, Limitable limitable);
}

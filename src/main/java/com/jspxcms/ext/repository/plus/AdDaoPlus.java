package com.jspxcms.ext.repository.plus;

import java.util.List;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Ad;

/**
 * AdDaoPlus
 * 
 * @author liufang
 * 
 */
public interface AdDaoPlus {
	public List<Ad> findList(Integer[] siteId, String[] slot, Integer[] slotId,
			Limitable limitable);
}

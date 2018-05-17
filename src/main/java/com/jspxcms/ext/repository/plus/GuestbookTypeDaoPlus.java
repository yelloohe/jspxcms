package com.jspxcms.ext.repository.plus;

import java.util.List;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.GuestbookType;

/**
 * GuestbookTypeDaoPlus
 * 
 * @author yangxing
 * 
 */
public interface GuestbookTypeDaoPlus {
	public List<GuestbookType> getList(Integer[] siteId, Limitable limitable);
}

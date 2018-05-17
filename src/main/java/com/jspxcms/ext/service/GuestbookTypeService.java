package com.jspxcms.ext.service;

import java.util.List;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.GuestbookType;

/**
 * GuestbookTypeService
 * 
 * @author yangxing
 * 
 */
public interface GuestbookTypeService {
	public List<GuestbookType> findList(Integer siteId);

	public List<GuestbookType> findList(Integer[] siteId, Limitable limitable);

	public GuestbookType get(Integer id);

	public GuestbookType save(GuestbookType bean, Integer siteId);

	public GuestbookType update(GuestbookType bean);

	public GuestbookType[] batchUpdate(Integer[] id, String[] name,
			String[] number, String[] description);

	public GuestbookType delete(Integer id);

	public GuestbookType[] delete(Integer[] ids);
}

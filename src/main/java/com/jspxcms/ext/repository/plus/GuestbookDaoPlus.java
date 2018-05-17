package com.jspxcms.ext.repository.plus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Guestbook;

/**
 * GuestbookDaoPlus
 * 
 * @author yangxing
 * 
 */
public interface GuestbookDaoPlus {
	public List<Guestbook> findList(Integer[] siteId, String[] type,
			Integer[] typeId, Boolean isRecommend, Boolean isReply,
			Integer[] status, Limitable limitable);

	public Page<Guestbook> findPage(Integer[] siteId, String[] type,
			Integer[] typeId, Boolean isRecommend, Boolean isReply,
			Integer[] status, Pageable pageable);
}

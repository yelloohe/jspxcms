package com.jspxcms.core.repository.plus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Tag;

/**
 * TagDaoPlus
 * 
 * @author liufang
 * 
 */
public interface TagDaoPlus {
	public List<Tag> findList(Integer[] siteId, String[] node,
			Integer[] nodeId, Integer refers, Limitable limitable);

	public Page<Tag> findPage(Integer[] siteId, String[] node,
			Integer[] nodeId, Integer refers, Pageable pageable);

	public List<Tag> findByName(String[] names, Integer[] siteIds);
}

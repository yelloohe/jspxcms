package com.jspxcms.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Tag;

/**
 * TagService
 * 
 * @author liufang
 * 
 */
public interface TagService {
	public Page<Tag> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable);

	public RowSide<Tag> findSide(Integer siteId, Map<String, String[]> params, Tag bean, Integer position, Sort sort);

	public List<Tag> findList(Integer[] siteId, String[] node, Integer[] nodeId, Integer refers, Limitable limitable);

	public Page<Tag> findPage(Integer[] siteId, String[] node, Integer[] nodeId, Integer refers, Pageable pageable);

	public List<Tag> findByName(String[] names, Integer[] siteIds);

	public Tag findByName(Integer siteId, String name);

	public Tag get(Integer id);

	public Tag save(Tag bean, Integer siteId);

	public Tag refer(String tagName, Integer siteId);

	public List<Tag> refer(String[] names, Integer siteId);

	public void derefer(Tag tag);

	public void derefer(Collection<Tag> tags);

	public Tag update(Tag bean);

	public Tag delete(Integer id);

	public Tag[] delete(Integer[] ids);
}

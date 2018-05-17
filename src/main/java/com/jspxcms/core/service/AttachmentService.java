package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Attachment;

public interface AttachmentService {
	public Page<Attachment> findAll(Integer siteId, boolean notUsed,
			Map<String, String[]> params, Pageable pageable);

	public RowSide<Attachment> findSide(Integer siteId, boolean notUsed,
			Map<String, String[]> params, Attachment bean, Integer position,
			Sort sort);

	public Attachment findByName(String name);

	public Attachment get(Integer id);

	public Attachment save(String name, Long length, String ip, Integer userId,
			Integer siteId);

	public Attachment save(Attachment bean, Integer userId, Integer siteId);

	public Attachment update(Attachment bean);

	public Attachment delete(Integer id);

	public List<Attachment> delete(Integer[] ids);
}

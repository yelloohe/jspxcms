package com.jspxcms.plug.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.plug.domain.Resume;

public interface ResumeService {
	public Page<Resume> findAll(Integer siteId, Map<String, String[]> params,
			Pageable pageable);

	public RowSide<Resume> findSide(Integer siteId,
			Map<String, String[]> params, Resume bean, Integer position,
			Sort sort);

	public List<Resume> findList(Integer[] siteId, Limitable limitable);

	public Resume get(Integer id);

	public Resume save(Resume bean, Integer siteId);

	public Resume update(Resume bean);

	public Resume delete(Integer id);

	public Resume[] delete(Integer[] ids);
}

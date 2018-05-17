package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.SensitiveWord;

public interface SensitiveWordService {
	public Page<SensitiveWord> findAll(Map<String, String[]> params,
			Pageable pageable);

	public RowSide<SensitiveWord> findSide(Map<String, String[]> params,
			SensitiveWord bean, Integer position, Sort sort);

	public List<SensitiveWord> getList();

	public SensitiveWord get(Integer id);

	public String replace(String s);

	public SensitiveWord save(SensitiveWord bean, Integer siteId);

	public SensitiveWord update(SensitiveWord bean);

	public SensitiveWord delete(Integer id);

	public SensitiveWord[] delete(Integer[] ids);
}

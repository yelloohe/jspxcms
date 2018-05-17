package com.jspxcms.core.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.SensitiveWord;
import com.jspxcms.core.repository.SensitiveWordDao;
import com.jspxcms.core.service.SensitiveWordService;

@Service
@Transactional(readOnly = true)
public class SensitiveWordServiceImpl implements SensitiveWordService {
	public Page<SensitiveWord> findAll(Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	public RowSide<SensitiveWord> findSide(Map<String, String[]> params, SensitiveWord bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<SensitiveWord>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<SensitiveWord> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<SensitiveWord> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<SensitiveWord> sp = SearchFilter.spec(filters, SensitiveWord.class);
		return sp;
	}

	public List<SensitiveWord> getList() {
		return dao.findByStatus(SensitiveWord.NORMAL);
	}

	public SensitiveWord get(Integer id) {
		return dao.findOne(id);
	}

	public String replace(String s) {
		if (StringUtils.isBlank(s)) {
			return s;
		}
		for (SensitiveWord word : getList()) {
			// 如果replacement为null，则会不替换，所以要转换成空字符串
			String replacement = word.getReplacement() != null ? word.getReplacement() : "";
			s = StringUtils.replace(s, word.getName(), replacement);
		}
		return s;
	}

	@Transactional
	public SensitiveWord save(SensitiveWord bean, Integer siteId) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public SensitiveWord update(SensitiveWord bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public SensitiveWord delete(Integer id) {
		SensitiveWord entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public SensitiveWord[] delete(Integer[] ids) {
		SensitiveWord[] beans = new SensitiveWord[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	private SensitiveWordDao dao;

	@Autowired
	public void setDao(SensitiveWordDao dao) {
		this.dao = dao;
	}
}

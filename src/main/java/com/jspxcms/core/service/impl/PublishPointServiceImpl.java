package com.jspxcms.core.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.repository.PublishPointDao;
import com.jspxcms.core.service.GlobalService;
import com.jspxcms.core.service.PublishPointService;

@Service
@Transactional(readOnly = true)
public class PublishPointServiceImpl implements PublishPointService {
	public List<PublishPoint> findList(Map<String, String[]> params, Sort sort) {
		return dao.findAll(spec(params), sort);
	}

	public RowSide<PublishPoint> findSide(Map<String, String[]> params,
			PublishPoint bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<PublishPoint>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<PublishPoint> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<PublishPoint> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<PublishPoint> sp = SearchFilter.spec(filters,
				PublishPoint.class);
		return sp;
	}

	public List<PublishPoint> findByType(Integer type) {
		return dao.findByType(type, new Sort("seq", "id"));
	}

	public PublishPoint get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public PublishPoint save(PublishPoint bean) {
		bean.setGlobal(globalService.findUnique());
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public PublishPoint update(PublishPoint bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public PublishPoint delete(Integer id) {
		PublishPoint entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public PublishPoint[] delete(Integer[] ids) {
		PublishPoint[] beans = new PublishPoint[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	private GlobalService globalService;

	@Autowired
	public void setGlobalService(GlobalService globalService) {
		this.globalService = globalService;
	}

	private PublishPointDao dao;

	@Autowired
	public void setDao(PublishPointDao dao) {
		this.dao = dao;
	}
}

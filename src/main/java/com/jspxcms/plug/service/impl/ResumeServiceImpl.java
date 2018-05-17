package com.jspxcms.plug.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
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
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.plug.domain.Resume;
import com.jspxcms.plug.repository.ResumeDao;
import com.jspxcms.plug.service.ResumeService;

@Service
@Transactional(readOnly = true)
public class ResumeServiceImpl implements ResumeService, SiteDeleteListener {
	public Page<Resume> findAll(Integer siteId, Map<String, String[]> params,
			Pageable pageable) {
		return dao.findAll(spec(siteId, params), pageable);
	}

	public RowSide<Resume> findSide(Integer siteId,
			Map<String, String[]> params, Resume bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<Resume>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Resume> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Resume> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Resume> fsp = SearchFilter.spec(filters,
				Resume.class);
		Specification<Resume> sp = new Specification<Resume>() {
			public Predicate toPredicate(Root<Resume> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site")
							.<Integer> get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	public List<Resume> findList(Integer[] siteId, Limitable limitable) {
		return dao.getList(siteId, limitable);
	}

	public Resume get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Resume save(Resume bean, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Resume update(Resume bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Resume delete(Integer id) {
		Resume entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public Resume[] delete(Integer[] ids) {
		Resume[] beans = new Resume[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	@Transactional
	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.deleteBySiteId(Arrays.asList(ids));
		}
	}

	private SiteService siteService;

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private ResumeDao dao;

	@Autowired
	public void setDao(ResumeDao dao) {
		this.dao = dao;
	}
}

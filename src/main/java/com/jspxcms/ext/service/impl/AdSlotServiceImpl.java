package com.jspxcms.ext.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.jspxcms.core.support.DeleteException;
import com.jspxcms.ext.domain.AdSlot;
import com.jspxcms.ext.listener.AdSlotDeleteListener;
import com.jspxcms.ext.repository.AdSlotDao;
import com.jspxcms.ext.service.AdSlotService;

@Service
@Transactional(readOnly = true)
public class AdSlotServiceImpl implements AdSlotService, SiteDeleteListener {
	public List<AdSlot> findList(Integer siteId, Map<String, String[]> params,
			Sort sort) {
		return dao.findAll(spec(siteId, params), sort);
	}

	public RowSide<AdSlot> findSide(Integer siteId,
			Map<String, String[]> params, AdSlot bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<AdSlot>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<AdSlot> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<AdSlot> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<AdSlot> fsp = SearchFilter.spec(filters,
				AdSlot.class);
		Specification<AdSlot> sp = new Specification<AdSlot>() {
			public Predicate toPredicate(Root<AdSlot> root,
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

	public List<AdSlot> findList(Integer siteId) {
		return dao.findBySiteId(siteId);
	}

	public List<AdSlot> findList(String number, Integer siteId) {
		return dao.findByNumberAndSiteId(number, siteId);
	}

	public AdSlot get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public AdSlot save(AdSlot bean, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public AdSlot update(AdSlot bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	private AdSlot doDelete(Integer id) {
		AdSlot entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public AdSlot delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public AdSlot[] delete(Integer[] ids) {
		firePreDelete(ids);
		AdSlot[] beans = new AdSlot[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("adSlot.management");
			}
		}
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (AdSlotDeleteListener listener : deleteListeners) {
				listener.preAdSlotDelete(ids);
			}
		}
	}

	private List<AdSlotDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(List<AdSlotDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private SiteService siteService;

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private AdSlotDao dao;

	@Autowired
	public void setDao(AdSlotDao dao) {
		this.dao = dao;
	}
}

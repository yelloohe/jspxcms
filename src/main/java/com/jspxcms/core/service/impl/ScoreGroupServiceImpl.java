package com.jspxcms.core.service.impl;

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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.ScoreGroup;
import com.jspxcms.core.domain.ScoreItem;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.repository.ScoreGroupDao;
import com.jspxcms.core.service.ScoreGroupService;
import com.jspxcms.core.service.ScoreItemService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.DeleteException;

@Service
@Transactional(readOnly = true)
public class ScoreGroupServiceImpl implements ScoreGroupService,
		SiteDeleteListener {
	public List<ScoreGroup> findList(Integer siteId,
			Map<String, String[]> params, Sort sort) {
		return dao.findAll(spec(siteId, params), sort);
	}

	public RowSide<ScoreGroup> findSide(Integer siteId,
			Map<String, String[]> params, ScoreGroup bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<ScoreGroup>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<ScoreGroup> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<ScoreGroup> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<ScoreGroup> fsp = SearchFilter.spec(filters,
				ScoreGroup.class);
		Specification<ScoreGroup> sp = new Specification<ScoreGroup>() {
			public Predicate toPredicate(Root<ScoreGroup> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred,
							cb.equal(root.get("site").get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	public ScoreGroup findByNumber(Integer siteId, String number) {
		List<ScoreGroup> list = dao.findBySiteIdAndNumber(siteId, number);
		return !list.isEmpty() ? list.get(0) : null;
	}

	public ScoreGroup findTopOne(Integer siteId) {
		return dao.findTopOne(siteId);
	}

	public ScoreGroup get(Integer id) {
		return dao.findOne(id);
	}

	public boolean numberExist(String number, Integer siteId) {
		return dao.countByNumber(number, siteId) > 0;
	}

	@Transactional
	public ScoreGroup save(ScoreGroup bean, String[] name, Integer[] score,
			String[] icon, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);

		scoreItemService.save(name, score, icon, bean);
		return bean;
	}

	@Transactional
	public ScoreGroup update(ScoreGroup bean, Integer[] itemId,
			String[] itemName, Integer[] itemScore, String[] itemIcon) {
		bean.applyDefaultValue();
		bean = dao.save(bean);

		scoreItemService.update(itemId, itemName, itemScore, itemIcon, bean);
		return bean;
	}

	@Transactional
	public ScoreGroup[] batchUpdate(Integer[] id, String[] name,
			String[] number, String[] description) {
		ScoreGroup[] beans = new ScoreGroup[id.length];
		for (int i = 0, len = id.length; i < len; i++) {
			beans[i] = get(id[i]);
			beans[i].setSeq(i);
			beans[i].setName(name[i]);
			beans[i].setNumber(number[i]);
			beans[i].setDescription(description[i]);
			beans[i].applyDefaultValue();
			dao.save(beans[i]);
		}
		return beans;
	}

	@Transactional
	public ScoreGroup delete(Integer id) {
		ScoreGroup entity = dao.findOne(id);
		for (ScoreItem item : entity.getItems()) {
			scoreItemService.delete(item);
		}
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public ScoreGroup[] delete(Integer[] ids) {
		ScoreGroup[] beans = new ScoreGroup[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("scoreGroup.management");
			}
		}
	}

	private ScoreItemService scoreItemService;
	private SiteService siteService;

	@Autowired
	public void setScoreItemService(ScoreItemService scoreItemService) {
		this.scoreItemService = scoreItemService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private ScoreGroupDao dao;

	@Autowired
	public void setDao(ScoreGroupDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service.impl;

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
import com.jspxcms.core.domain.WorkflowGroup;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.WorkflowGroupDeleteListener;
import com.jspxcms.core.repository.WorkflowGroupDao;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.WorkflowGroupService;
import com.jspxcms.core.support.DeleteException;

/**
 * WorkflowGroupServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class WorkflowGroupServiceImpl implements WorkflowGroupService,
		SiteDeleteListener {
	public List<WorkflowGroup> findList(Integer siteId,
			Map<String, String[]> params, Sort sort) {
		return dao.findAll(spec(siteId, params), sort);
	}

	public RowSide<WorkflowGroup> findSide(Integer siteId,
			Map<String, String[]> params, WorkflowGroup bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<WorkflowGroup>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<WorkflowGroup> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<WorkflowGroup> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<WorkflowGroup> fsp = SearchFilter.spec(filters,
				WorkflowGroup.class);
		Specification<WorkflowGroup> sp = new Specification<WorkflowGroup>() {
			public Predicate toPredicate(Root<WorkflowGroup> root,
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

	public List<WorkflowGroup> findList(Integer siteId) {
		return dao.findBySiteId(siteId);
	}

	public WorkflowGroup get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public WorkflowGroup save(WorkflowGroup bean, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public WorkflowGroup update(WorkflowGroup bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	private WorkflowGroup doDelete(Integer id) {
		WorkflowGroup entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public WorkflowGroup delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public WorkflowGroup[] delete(Integer[] ids) {
		firePreDelete(ids);
		WorkflowGroup[] beans = new WorkflowGroup[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("workflowGroup.management");
			}
		}
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (WorkflowGroupDeleteListener listener : deleteListeners) {
				listener.preWorkflowGroupDelete(ids);
			}
		}
	}

	private List<WorkflowGroupDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(
			List<WorkflowGroupDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private SiteService siteService;

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private WorkflowGroupDao dao;

	@Autowired
	public void setDao(WorkflowGroupDao dao) {
		this.dao = dao;
	}
}

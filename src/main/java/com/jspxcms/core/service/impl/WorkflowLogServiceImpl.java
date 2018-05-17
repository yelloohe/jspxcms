package com.jspxcms.core.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import com.jspxcms.core.domain.WorkflowLog;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.WorkflowLogDao;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.WorkflowLogService;

@Service
@Transactional(readOnly = true)
public class WorkflowLogServiceImpl implements WorkflowLogService,
		UserDeleteListener {
	public Page<WorkflowLog> findAll(Map<String, String[]> params,
			Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	public RowSide<WorkflowLog> findSide(Map<String, String[]> params,
			WorkflowLog bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<WorkflowLog>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<WorkflowLog> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<WorkflowLog> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<WorkflowLog> sp = SearchFilter.spec(filters,
				WorkflowLog.class);
		return sp;
	}

	public WorkflowLog get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public WorkflowLog save(WorkflowLog bean, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public WorkflowLog update(WorkflowLog bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public WorkflowLog delete(Integer id) {
		WorkflowLog entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public WorkflowLog[] delete(Integer[] ids) {
		WorkflowLog[] beans = new WorkflowLog[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	public int deleteByStepId(Collection<Integer> stepIds) {
		return dao.deleteByStepId(stepIds);
	}

	public int deleteByWorkflowId(Collection<Integer> workflowIds) {
		return dao.deleteByWorkflowId(workflowIds);
	}

	public int deleteByProcessUserId(Collection<Integer> userIds) {
		return dao.deleteByProcessUserId(userIds);
	}

	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.deleteByUserId(Arrays.asList(ids));
		}
	}

	private SiteService siteService;

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private WorkflowLogDao dao;

	@Autowired
	public void setDao(WorkflowLogDao dao) {
		this.dao = dao;
	}
}

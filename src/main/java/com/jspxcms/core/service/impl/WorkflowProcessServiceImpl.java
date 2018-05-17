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
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.Workflow;
import com.jspxcms.core.domain.WorkflowProcess;
import com.jspxcms.core.domain.WorkflowStep;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.listener.WorkflowDeleteListener;
import com.jspxcms.core.listener.WorkflowStepDeleteListener;
import com.jspxcms.core.repository.WorkflowProcessDao;
import com.jspxcms.core.service.WorkflowLogService;
import com.jspxcms.core.service.WorkflowProcessService;

@Service
@Transactional(readOnly = true)
public class WorkflowProcessServiceImpl implements WorkflowProcessService,
		WorkflowDeleteListener, UserDeleteListener, WorkflowStepDeleteListener {
	public Page<WorkflowProcess> findAll(Map<String, String[]> params,
			Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	public RowSide<WorkflowProcess> findSide(Map<String, String[]> params,
			WorkflowProcess bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<WorkflowProcess>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<WorkflowProcess> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<WorkflowProcess> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<WorkflowProcess> sp = SearchFilter.spec(filters,
				WorkflowProcess.class);
		return sp;
	}

	public WorkflowProcess findOne(Integer dataType, Integer dataId) {
		return dao.findByDataTypeAndDataId(dataType, dataId);
	}

	public WorkflowProcess get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public WorkflowProcess save(Site site, Workflow workflow,
			WorkflowStep step, User owner, WorkflowProcess process,
			Integer dataId, Boolean isReject, Boolean isEnd) {
		process.setSite(site);
		process.setWorkflow(workflow);
		process.setStep(step);
		process.setUser(owner);
		process.setDataId(dataId);
		process.setRejection(isReject);
		process.setEnd(isEnd);

		process.applyDefaultValue();
		process = dao.save(process);
		return process;
	}

	@Transactional
	public WorkflowProcess update(WorkflowProcess bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public WorkflowProcess delete(Integer id) {
		WorkflowProcess entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public WorkflowProcess[] delete(Integer[] ids) {
		WorkflowProcess[] beans = new WorkflowProcess[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	@Transactional
	public WorkflowProcess delete(WorkflowProcess bean) {
		if (bean != null) {
			dao.delete(bean);
		}
		return bean;
	}

	@Transactional
	public Collection<WorkflowProcess> delete(Collection<WorkflowProcess> beans) {
		if (beans != null) {
			dao.delete(beans);
		}
		return beans;
	}

	@Transactional
	public void preWorkflowDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.deleteByWorkflowId(Arrays.asList(ids));
		}
	}

	public void preWorkflowStepDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			workflowLogService.deleteByStepId(Arrays.asList(ids));
			dao.deleteByStepId(Arrays.asList(ids));
		}

	}

	@Transactional
	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			workflowLogService.deleteByProcessUserId(Arrays.asList(ids));
			dao.deleteByUserId(Arrays.asList(ids));
		}
	}

	private WorkflowLogService workflowLogService;

	@Autowired
	public void setWorkflowLogService(WorkflowLogService workflowLogService) {
		this.workflowLogService = workflowLogService;
	}

	private WorkflowProcessDao dao;

	@Autowired
	public void setDao(WorkflowProcessDao dao) {
		this.dao = dao;
	}
}

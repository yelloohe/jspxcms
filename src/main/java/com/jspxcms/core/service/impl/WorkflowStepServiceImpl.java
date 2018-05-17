package com.jspxcms.core.service.impl;

import java.util.ArrayList;
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
import com.jspxcms.core.domain.Workflow;
import com.jspxcms.core.domain.WorkflowStep;
import com.jspxcms.core.listener.WorkflowStepDeleteListener;
import com.jspxcms.core.repository.WorkflowStepDao;
import com.jspxcms.core.service.WorkflowService;
import com.jspxcms.core.service.WorkflowStepRoleService;
import com.jspxcms.core.service.WorkflowStepService;

@Service
@Transactional(readOnly = true)
public class WorkflowStepServiceImpl implements WorkflowStepService {
	public List<WorkflowStep> findList(Integer workflowId, Map<String, String[]> params, Sort sort) {
		return dao.findAll(spec(workflowId, params), sort);
	}

	public RowSide<WorkflowStep> findSide(Integer workflowId, Map<String, String[]> params, WorkflowStep bean,
			Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<WorkflowStep>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<WorkflowStep> list = dao.findAll(spec(workflowId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<WorkflowStep> spec(final Integer workflowId, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<WorkflowStep> fsp = SearchFilter.spec(filters, WorkflowStep.class);
		Specification<WorkflowStep> sp = new Specification<WorkflowStep>() {
			public Predicate toPredicate(Root<WorkflowStep> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (workflowId != null) {
					pred = cb.and(pred, cb.equal(root.get("workflow").get("id"), workflowId));
				}
				return pred;
			}
		};
		return sp;
	}

	public WorkflowStep get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public WorkflowStep save(WorkflowStep bean, Integer[] roleIds, Integer workflowId) {
		Workflow workflow = workflowService.get(workflowId);
		bean.setWorkflow(workflow);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		workflowStepRoleService.update(bean, roleIds);
		return bean;
	}

	@Transactional
	public WorkflowStep update(WorkflowStep bean, Integer[] roleIds) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		workflowStepRoleService.update(bean, roleIds);
		return bean;
	}

	@Transactional
	public List<WorkflowStep> batchUpdate(Integer[] id, String[] name) {
		List<WorkflowStep> beans = new ArrayList<WorkflowStep>();
		if (ArrayUtils.isEmpty(id)) {
			return beans;
		}
		WorkflowStep bean;
		for (int i = 0, len = id.length; i < len; i++) {
			bean = get(id[i]);
			bean.setName(name[i]);
			bean.setSeq(i);
			beans.add(bean);
		}
		return beans;
	}

	private WorkflowStep doDelete(Integer id) {
		WorkflowStep entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public WorkflowStep delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public WorkflowStep[] delete(Integer[] ids) {
		firePreDelete(ids);
		WorkflowStep[] beans = new WorkflowStep[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	@Transactional
	public void delete(Collection<WorkflowStep> steps) {
		Integer[] ids = new Integer[steps.size()];
		int i = 0;
		for (WorkflowStep step : steps) {
			ids[i++] = step.getId();
		}
		firePreDelete(ids);
		for (WorkflowStep step : steps) {
			dao.delete(step);
		}
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (WorkflowStepDeleteListener listener : deleteListeners) {
				listener.preWorkflowStepDelete(ids);
			}
		}
	}

	private List<WorkflowStepDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(List<WorkflowStepDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private WorkflowService workflowService;
	private WorkflowStepRoleService workflowStepRoleService;

	@Autowired
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	@Autowired
	public void setWorkflowStepRoleService(WorkflowStepRoleService workflowStepRoleService) {
		this.workflowStepRoleService = workflowStepRoleService;
	}

	private WorkflowStepDao dao;

	@Autowired
	public void setDao(WorkflowStepDao dao) {
		this.dao = dao;
	}
}

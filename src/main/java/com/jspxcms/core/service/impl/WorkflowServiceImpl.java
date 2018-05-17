package com.jspxcms.core.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
import com.jspxcms.common.util.Reflections;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.Workflow;
import com.jspxcms.core.domain.WorkflowGroup;
import com.jspxcms.core.domain.WorkflowProcess;
import com.jspxcms.core.domain.WorkflowStep;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.WorkflowDeleteListener;
import com.jspxcms.core.listener.WorkflowGroupDeleteListener;
import com.jspxcms.core.repository.WorkflowDao;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.WorkflowGroupService;
import com.jspxcms.core.service.WorkflowProcessService;
import com.jspxcms.core.service.WorkflowService;
import com.jspxcms.core.service.WorkflowStepService;
import com.jspxcms.core.support.DeleteException;

/**
 * WorkflowServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class WorkflowServiceImpl implements WorkflowService,
		WorkflowGroupDeleteListener, SiteDeleteListener {
	public List<Workflow> findList(Integer siteId,
			Map<String, String[]> params, Sort sort) {
		return dao.findAll(spec(siteId, params), sort);
	}

	public RowSide<Workflow> findSide(Integer siteId,
			Map<String, String[]> params, Workflow bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<Workflow>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Workflow> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Workflow> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Workflow> fsp = SearchFilter.spec(filters,
				Workflow.class);
		Specification<Workflow> sp = new Specification<Workflow>() {
			public Predicate toPredicate(Root<Workflow> root,
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

	public List<Workflow> findList(Integer siteId) {
		Sort sort = new Sort("group.seq", "group.id", "seq", "id");
		return dao.findBySiteId(siteId, sort);
	}

	public Workflow get(Integer id) {
		return dao.findOne(id);
	}

	/**
	 * 新发表、审核。
	 * <ul>
	 * <li>无工作流或无步骤则结束。
	 * <li>已终审的不能再次审核，否则将重新启动流程。
	 * </ul>
	 * 
	 * @param workflow
	 * @param operator
	 * @param dataId
	 * @return 步骤名称。空串代表审核通过，null代表无权限。
	 */
	@Transactional
	public String pass(Workflow workflow, User owner, User operator,
			WorkflowProcess targetProcess, Integer dataType, Integer dataId,
			String opinion, boolean anew) {
		WorkflowProcess process = processService.findOne(dataType, dataId);
		if (process != null && !process.getEnd()) {
			// 流程存在，且未结束，使用原流程工作流
			workflow = process.getWorkflow();
		}
		if (workflow == null) {
			// 没有工作流，审核通过。
			if (process != null) {
				// TODO 写流程日志
				process.passEnd();
			}
			return "";
		}
		if (operator.getInfoFinalPerm(workflow.getSite().getId())) {
			// 拥有终审权限，审核通过。
			if (process != null) {
				// TODO 写流程日志
				process.passEnd();
			}
			return "";
		}
		List<WorkflowStep> steps = workflow.getSteps();
		int size = steps.size();
		if (size <= 0) {
			if (process != null) {
				// 无审核流程，审核通过。
				// TODO 写流程日志
				process.passEnd();
			}
			return "";
		}

		// 当前步骤ID
		Integer currStepId = null;
		if (process != null && !anew && !process.getEnd()
				&& process.getStep() != null) {
			currStepId = process.getStep().getId();
		}
		// 下一个步骤
		WorkflowStep nextStep = null;
		WorkflowStep step;
		// 不在工作流中，可以提交新流程，但不能审核已终审流程。
		// 在工作流中，可以审核流程，但不能审核超过自己步骤的流程。
		for (int i = size - 1; i >= 0; i--) {
			step = steps.get(i);
			if (Reflections.containsAny(step.getRoles(), operator.getRoles(),
					"id")) {
				break;
			}
			if (step.getId().equals(currStepId)) {
				// 没有审核权限或审核权限在当前步骤之前
				return null;
			}
			nextStep = step;
		}

		// TODO 写审核log

		Date endDate = null;
		Boolean isEnd = false;
		Boolean isRejection = false;
		// nextStep==null代表终审通过
		if (nextStep == null) {
			// 终审人员，审核结束。
			isEnd = true;
			endDate = new Timestamp(System.currentTimeMillis());
		}
		if (process != null) {
			process.setWorkflow(workflow);
			process.setStep(nextStep);
			process.setEndDate(endDate);
			process.setRejection(isRejection);
			process.setEnd(isEnd);
		} else {
			Site site = workflow.getSite();
			processService.save(site, workflow, nextStep, owner, targetProcess,
					dataId, isRejection, isEnd);
		}
		return nextStep != null ? nextStep.getName() : "";
	}

	/**
	 * 
	 * @param workflow
	 * @param operator
	 * @param dataType
	 * @param dataId
	 * @param opinion
	 * @param rejectEnd
	 *            是否退稿
	 * @return 步骤名称。空串代表退稿，null代表无权限。
	 */
	@Transactional
	public String reject(Workflow workflow, User owner, User operator,
			WorkflowProcess targetProcess, Integer dataType, Integer dataId,
			String opinion, boolean rejectEnd) {
		WorkflowProcess process = processService.findOne(dataType, dataId);
		// 流程存在，且未结束，使用原流程工作流
		if (process != null && !process.getEnd()) {
			workflow = process.getWorkflow();
		}
		if (workflow == null) {
			// 没有工作流，退稿。
			if (process != null) {
				// TODO 写流程日志
				process.rejectEnd();
			}
			return "";
		}
		List<WorkflowStep> steps = workflow.getSteps();
		int size = steps.size();
		if (size <= 0) {
			// 无审核流程，退稿。
			if (process != null) {
				// TODO 写流程日志
				process.rejectEnd();
			}
			return "";
		}
		// 当前步骤ID
		Integer currStepId = null;
		if (process != null && !process.getEnd() && process.getStep() != null) {
			currStepId = process.getStep().getId();
		}
		// 下一个步骤
		WorkflowStep nextStep = null;
		WorkflowStep step;
		Integer siteId = workflow.getSite().getId();
		// 是否有终审权限
		boolean hasPermission = operator.getInfoFinalPerm(siteId);
		boolean currPoint = false;
		for (int i = 0; i < size; i++) {
			step = steps.get(i);
			if (step.getId().equals(currStepId)) {
				currPoint = true;
			}
			if (!hasPermission
					&& (currPoint || i == size - 1)
					&& Reflections.containsAny(step.getRoles(),
							operator.getRoles(), "id")) {
				// 有流程权限
				hasPermission = true;
			}
			if (!currPoint) {
				nextStep = step;
			}
		}
		// 无权限
		if (!hasPermission) {
			return null;
		}
		// 已经退稿或退回到发稿人
		if (rejectEnd
				|| (nextStep != null && Reflections.containsAny(
						nextStep.getRoles(), owner.getRoles(), "id"))) {
			nextStep = null;
		}

		// TODO 写审核log

		Date endDate = null;
		Boolean isEnd = false;
		if (nextStep == null) {
			isEnd = true;
			endDate = new Timestamp(System.currentTimeMillis());
		}
		Boolean isRejection = true;
		if (process != null) {
			process.setWorkflow(workflow);
			process.setStep(nextStep);
			process.setEndDate(endDate);
			process.setEnd(isEnd);
			process.setRejection(isRejection);
		} else {
			Site site = workflow.getSite();
			processService.save(site, workflow, nextStep, owner, targetProcess,
					dataId, isRejection, isEnd);
		}
		return nextStep != null ? nextStep.getName() : "";
	}

	@Transactional
	public Workflow save(Workflow bean, Integer groupId, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		WorkflowGroup group = groupService.get(groupId);
		bean.setGroup(group);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Workflow update(Workflow bean, Integer groupId) {
		WorkflowGroup group = groupService.get(groupId);
		bean.setGroup(group);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public List<Workflow> batchUpdate(Integer[] id, String[] name,
			String[] description) {
		List<Workflow> beans = new ArrayList<Workflow>();
		if (ArrayUtils.isEmpty(id)) {
			return beans;
		}
		Workflow bean;
		for (int i = 0, len = id.length; i < len; i++) {
			bean = get(id[i]);
			bean.setName(name[i]);
			bean.setDescription(description[i]);
			bean.setSeq(i);
			beans.add(bean);
		}
		return beans;
	}

	private Workflow doDelete(Integer id) {
		Workflow entity = dao.findOne(id);
		if (entity != null) {
			stepService.delete(entity.getSteps());
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public Workflow delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public Workflow[] delete(Integer[] ids) {
		firePreDelete(ids);
		Workflow[] beans = new Workflow[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("workflow.management");
			}
		}
	}

	public void preWorkflowGroupDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countByGroupId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("workflow.management");
			}
		}
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (WorkflowDeleteListener listener : deleteListeners) {
				listener.preWorkflowDelete(ids);
			}
		}
	}

	private List<WorkflowDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(List<WorkflowDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private WorkflowStepService stepService;
	private WorkflowProcessService processService;
	private WorkflowGroupService groupService;
	private SiteService siteService;

	@Autowired
	public void setStepService(WorkflowStepService stepService) {
		this.stepService = stepService;
	}

	@Autowired
	public void setProcessService(WorkflowProcessService processService) {
		this.processService = processService;
	}

	@Autowired
	public void setGroupService(WorkflowGroupService groupService) {
		this.groupService = groupService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private WorkflowDao dao;

	@Autowired
	public void setDao(WorkflowDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.Workflow;
import com.jspxcms.core.domain.WorkflowProcess;
import com.jspxcms.core.domain.WorkflowStep;

public interface WorkflowProcessService {
	public Page<WorkflowProcess> findAll(Map<String, String[]> params,
			Pageable pageable);

	public RowSide<WorkflowProcess> findSide(Map<String, String[]> params,
			WorkflowProcess bean, Integer position, Sort sort);

	public WorkflowProcess findOne(Integer dataType, Integer dataId);

	public WorkflowProcess get(Integer id);

	public WorkflowProcess save(Site site, Workflow workflow,
			WorkflowStep step, User owner, WorkflowProcess process,
			Integer dataId, Boolean isReject, Boolean isEnd);

	public WorkflowProcess delete(Integer id);

	public WorkflowProcess[] delete(Integer[] ids);

	public WorkflowProcess delete(WorkflowProcess bean);

	public Collection<WorkflowProcess> delete(Collection<WorkflowProcess> beans);
}

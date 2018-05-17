package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.WorkflowLog;

public interface WorkflowLogDao extends Repository<WorkflowLog, Integer> {
	public Page<WorkflowLog> findAll(Specification<WorkflowLog> spec,
			Pageable pageable);

	public List<WorkflowLog> findAll(Specification<WorkflowLog> spec,
			Limitable limitable);

	public WorkflowLog findOne(Integer id);

	public WorkflowLog save(WorkflowLog bean);

	public void delete(WorkflowLog bean);

	// --------------------

	@Modifying
	@Query("delete from WorkflowLog bean where bean.process.id in (select process.id from WorkflowProcess process where process.step.id in (?1))")
	public int deleteByStepId(Collection<Integer> stepIds);

	@Modifying
	@Query("delete from WorkflowLog bean where bean.process.id in (select process.id from WorkflowProcess process where process.workflow.id in (?1))")
	public int deleteByWorkflowId(Collection<Integer> workflowIds);

	@Modifying
	@Query("delete from WorkflowLog bean where bean.process.id in (select process.id from WorkflowProcess process where process.user.id in (?1))")
	public int deleteByProcessUserId(Collection<Integer> userIds);

	@Modifying
	@Query("delete from WorkflowLog bean where bean.user.id in (?1)")
	public int deleteByUserId(Collection<Integer> userIds);
}

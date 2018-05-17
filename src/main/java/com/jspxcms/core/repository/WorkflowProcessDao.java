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
import com.jspxcms.core.domain.WorkflowProcess;

public interface WorkflowProcessDao extends
		Repository<WorkflowProcess, Integer> {
	public Page<WorkflowProcess> findAll(Specification<WorkflowProcess> spec,
			Pageable pageable);

	public List<WorkflowProcess> findAll(Specification<WorkflowProcess> spec,
			Limitable limitable);

	public WorkflowProcess findOne(Integer id);

	public WorkflowProcess save(WorkflowProcess bean);

	public void delete(WorkflowProcess bean);

	public void delete(Iterable<WorkflowProcess> entities);

	// --------------------

	public WorkflowProcess findByDataTypeAndDataId(Integer dataType,
			Integer dataId);

	@Modifying
	@Query("delete from WorkflowProcess bean where bean.user.id in (?1)")
	public int deleteByUserId(Collection<Integer> userIds);

	@Modifying
	@Query("delete from WorkflowProcess bean where bean.step.id in (?1)")
	public int deleteByStepId(Collection<Integer> siteIds);

	@Modifying
	@Query("delete from WorkflowProcess bean where bean.workflow.id in (?1)")
	public int deleteByWorkflowId(Collection<Integer> workflowIds);
}

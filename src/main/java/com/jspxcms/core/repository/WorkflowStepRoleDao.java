package com.jspxcms.core.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.WorkflowStepRole;
import com.jspxcms.core.domain.WorkflowStepRole.WorkflowStepRoleId;

public interface WorkflowStepRoleDao extends Repository<WorkflowStepRole, WorkflowStepRoleId> {
	public WorkflowStepRole findOne(WorkflowStepRoleId id);

	// --------------------

	@Query("select count(*) from WorkflowStepRole bean where bean.role.id in (?1)")
	public long countByRoleId(Collection<Integer> roleIds);
}

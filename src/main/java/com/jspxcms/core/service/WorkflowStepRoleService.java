package com.jspxcms.core.service;

import com.jspxcms.core.domain.WorkflowStep;

public interface WorkflowStepRoleService {
	public void update(WorkflowStep step, Integer[] roleIds);
}

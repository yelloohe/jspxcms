package com.jspxcms.core.service.impl;

import com.jspxcms.core.domain.Role;
import com.jspxcms.core.domain.WorkflowStep;
import com.jspxcms.core.domain.WorkflowStepRole;
import com.jspxcms.core.domain.WorkflowStepRole.WorkflowStepRoleId;
import com.jspxcms.core.listener.RoleDeleteListener;
import com.jspxcms.core.repository.WorkflowStepRoleDao;
import com.jspxcms.core.service.RoleService;
import com.jspxcms.core.service.WorkflowStepRoleService;
import com.jspxcms.core.support.DeleteException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkflowStepRoleServiceImpl implements WorkflowStepRoleService, RoleDeleteListener {

    private WorkflowStepRole findOrCreate(WorkflowStep step, Role role) {
        WorkflowStepRole bean = dao.findOne(new WorkflowStepRoleId(role.getId(), step.getId()));
        if (bean == null) {
            bean = new WorkflowStepRole(step, role);
        }
        return bean;
    }

    @Transactional
    public void update(WorkflowStep step, Integer[] roleIds) {
        List<WorkflowStepRole> stepRoles = step.getStepRoles();
        stepRoles.clear();
        if (roleIds != null) {
            for (Integer id : roleIds) {
                stepRoles.add(findOrCreate(step, roleService.get(id)));
            }
        }
    }

    public void preRoleDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            if (dao.countByRoleId(Arrays.asList(ids)) > 0) {
                throw new DeleteException("workflowStep.management");
            }
        }
    }

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private WorkflowStepRoleDao dao;

    @Autowired
    public void setDao(WorkflowStepRoleDao dao) {
        this.dao = dao;
    }
}

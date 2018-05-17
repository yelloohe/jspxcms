package com.jspxcms.core.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Role;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserRole;
import com.jspxcms.core.domain.UserRole.UserRoleId;
import com.jspxcms.core.listener.RoleDeleteListener;
import com.jspxcms.core.repository.UserRoleDao;
import com.jspxcms.core.service.RoleService;
import com.jspxcms.core.service.UserRoleService;

@Service
@Transactional(readOnly = true)
public class UserRoleServiceImpl implements UserRoleService, RoleDeleteListener {
	private UserRole findOrCreate(User user, Role role) {
		UserRole bean = dao.findOne(new UserRoleId(user.getId(), role.getId()));
		if (bean == null) {
			bean = new UserRole(user, role);
		}
		return bean;
	}

	@Transactional
	public List<UserRole> update(User user, Integer[] roleIds, Integer siteId) {
		if (roleIds == null) {
			roleIds = new Integer[0];
		}
		List<UserRole> userRoles = user.getUserRoles();
		if (siteId != null) {
			// 删除本站角色
			Set<UserRole> tobeDelete = new HashSet<UserRole>();
			for (UserRole userRole : userRoles) {
				if (userRole.getRole().getSite().getId().equals(siteId)) {
					tobeDelete.add(userRole);
				}
			}
			userRoles.removeAll(tobeDelete);
		} else {
			// 删除所有角色
			userRoles.clear();
		}
		// 再新增
		for (Integer id : roleIds) {
			userRoles.add(findOrCreate(user, roleService.get(id)));
		}
		return userRoles;
	}

	public void preRoleDelete(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				Role role = roleService.get(id);
				for(User user : dao.findByUserRolesRoleId(id)) {
					UserRole userRole = new UserRole(user,role);
					user.getUserRoles().remove(userRole);
					dao.delete(userRole);
				}
			}
		}
	}

	private RoleService roleService;

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	private UserRoleDao dao;

	@Autowired
	public void setDao(UserRoleDao dao) {
		this.dao = dao;
	}
}

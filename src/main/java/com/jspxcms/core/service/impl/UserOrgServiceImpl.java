package com.jspxcms.core.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Org;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserOrg;
import com.jspxcms.core.domain.UserOrg.UserOrgId;
import com.jspxcms.core.listener.OrgDeleteListener;
import com.jspxcms.core.repository.UserOrgDao;
import com.jspxcms.core.service.OrgService;
import com.jspxcms.core.service.UserOrgService;

@Service
@Transactional(readOnly = true)
public class UserOrgServiceImpl implements UserOrgService, OrgDeleteListener {
	private UserOrg findOrCreate(User user, Org org) {
		UserOrg bean = dao.findOne(new UserOrgId(user.getId(), org.getId()));
		if (bean == null) {
			bean = new UserOrg(user, org);
		}
		return bean;
	}

	@Transactional
	public void update(User user, Integer[] orgIds, Integer orgId, Integer topOrgId) {
		// 主组织ID不存在，不更新。有时候更新用户其他信息，但不更新组织，这时组织ID可以传null。
		if (orgId == null) {
			return;
		}
		if (orgIds == null) {
			orgIds = new Integer[0];
		} else {
			orgIds = ArrayUtils.removeElement(orgIds, orgId);
		}
		List<UserOrg> userOrgs = user.getUserOrgs();
		if (topOrgId != null) {
			// 删除当前站点所属组织下的组织
			String treeNumber = orgService.get(topOrgId).getTreeNumber();
			Set<UserOrg> tobeDelete = new HashSet<UserOrg>();
			// 从第二个组织开始，第一个是主组织。
			for (int i = 1, len = userOrgs.size(); i < len; i++) {
				UserOrg userOrg = userOrgs.get(i);
				if (userOrg.getOrg().getTreeNumber().startsWith(treeNumber)) {
					tobeDelete.add(userOrg);
				}
			}
			userOrgs.removeAll(tobeDelete);
		} else {
			// 删除所有组织
			userOrgs.clear();
		}
		// 设置主组织
		if (userOrgs.isEmpty()) {
			userOrgs.add(0, findOrCreate(user, orgService.get(orgId)));
		} else {
			userOrgs.set(0, findOrCreate(user, orgService.get(orgId)));
		}
		// 设置扩展组织
		for (Integer id : orgIds) {
			userOrgs.add(findOrCreate(user, orgService.get(id)));
		}
	}

	@Transactional
	public void preOrgDelete(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				Org org = orgService.get(id);
				for (User user : dao.findByUserOrgsOrgId(id)) {
					UserOrg userRole = new UserOrg(user, org);
					user.getUserOrgs().remove(userRole);
					dao.delete(userRole);
				}
			}
		}
	}

	private OrgService orgService;

	@Autowired
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	private UserOrgDao dao;

	@Autowired
	public void setDao(UserOrgDao dao) {
		this.dao = dao;
	}
}

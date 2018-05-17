package com.jspxcms.core.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeRole;
import com.jspxcms.core.domain.NodeRole.NodeRoleId;
import com.jspxcms.core.domain.Role;
import com.jspxcms.core.repository.NodeRoleDao;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.service.NodeRoleService;
import com.jspxcms.core.service.RoleService;

@Service
@Transactional(readOnly = true)
public class NodeRoleServiceImpl implements NodeRoleService {
	private NodeRole findOrCreate(Node node, Role role, Boolean infoPerm, Boolean nodePerm) {
		NodeRole bean = dao.findOne(new NodeRoleId(node.getId(), role.getId()));
		if (bean == null) {
			bean = new NodeRole(node, role);
		}
		if (infoPerm != null) {
			bean.setInfoPerm(infoPerm);
		}
		if (nodePerm != null) {
			bean.setNodePerm(nodePerm);
		}
		bean.applyDefaultValue();
		return bean;
	}

	@Transactional
	public void update(Role role, Integer[] infoPermIds, Integer[] nodePermIds) {
		Integer siteId = role.getSite().getId();
		List<Node> nodes = nodeService.findList(siteId, null);
		for (Node node : nodes) {
			Boolean infoPerm = infoPermIds == null ? null : ArrayUtils.contains(infoPermIds, node.getId());
			Boolean nodePerm = nodePermIds == null ? null : ArrayUtils.contains(nodePermIds, node.getId());
			NodeRole nodeRole = findOrCreate(node, role, infoPerm, nodePerm);
			role.getNodeRoles().add(nodeRole);
			node.getNodeRoles().add(nodeRole);
		}
	}

	@Transactional
	public void update(Node node, Integer[] infoPermIds, Integer[] nodePermIds) {
		List<Role> roles = roleService.findList(node.getSite().getId());
		for (Role role : roles) {
			Boolean infoPerm = infoPermIds == null ? null : ArrayUtils.contains(infoPermIds, role.getId());
			Boolean nodePerm = nodePermIds == null ? null : ArrayUtils.contains(nodePermIds, role.getId());
			NodeRole nodeRole = findOrCreate(node, role, infoPerm, nodePerm);
			role.getNodeRoles().add(nodeRole);
			node.getNodeRoles().add(nodeRole);
		}
	}

	private NodeQueryService nodeService;
	private RoleService roleService;

	@Autowired
	public void setNodeService(NodeQueryService nodeService) {
		this.nodeService = nodeService;
	}

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	private NodeRoleDao dao;

	@Autowired
	public void setDao(NodeRoleDao dao) {
		this.dao = dao;
	}
}

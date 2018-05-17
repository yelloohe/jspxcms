package com.jspxcms.core.service.impl;

import java.util.SortedSet;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeOrg;
import com.jspxcms.core.domain.NodeOrg.NodeOrgId;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.listener.OrgDeleteListener;
import com.jspxcms.core.repository.NodeOrgDao;
import com.jspxcms.core.service.NodeOrgService;
import com.jspxcms.core.service.OrgService;

@Service
@Transactional(readOnly = true)
public class NodeOrgServiceImpl implements NodeOrgService, OrgDeleteListener {

	private NodeOrg findOrCreate(Node node, Org org, Boolean viewPerm) {
		NodeOrg bean = dao.findOne(new NodeOrgId(node.getId(), org.getId()));
		if (bean == null) {
			bean = new NodeOrg(node, org);
		}
		bean.setViewPerm(viewPerm);
		return bean;
	}

	@Transactional
	public void update(Node node, Integer[] viewOrgIds) {
		// 为null不更新。要设置为空，请传空数组。
		if (viewOrgIds == null) {
			return;
		}
		SortedSet<NodeOrg> nodeOrgs = node.getNodeOrgs();
		// 先更新
		for (NodeOrg nodeOrg : nodeOrgs) {
			nodeOrg.setViewPerm(ArrayUtils.contains(viewOrgIds, nodeOrg.getOrg().getId()));
		}
		// 再新增
		for (Integer viewOrgId : viewOrgIds) {
			nodeOrgs.add(findOrCreate(node, orgService.get(viewOrgId), true));
		}
	}

	@Override
	public void preOrgDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		for (Integer id : ids) {
			dao.deleteByOrgId(id);
		}
	}

	private OrgService orgService;

	@Autowired
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	private NodeOrgDao dao;

	@Autowired
	public void setDao(NodeOrgDao dao) {
		this.dao = dao;
	}
}

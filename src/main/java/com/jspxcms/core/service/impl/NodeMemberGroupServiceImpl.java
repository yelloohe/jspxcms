package com.jspxcms.core.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeMemberGroup;
import com.jspxcms.core.domain.NodeMemberGroup.NodeMemberGroupId;
import com.jspxcms.core.repository.NodeMemberGroupDao;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.NodeMemberGroupService;
import com.jspxcms.core.service.NodeQueryService;

@Service
@Transactional(readOnly = true)
public class NodeMemberGroupServiceImpl implements NodeMemberGroupService {
	private NodeMemberGroup findOrCreate(Node node, MemberGroup group, Boolean viewPerm, Boolean contriPerm,
			Boolean commentPerm) {
		NodeMemberGroup bean = dao.findOne(new NodeMemberGroupId(node.getId(), group.getId()));
		if (bean == null) {
			bean = new NodeMemberGroup(node, group);
		}
		if (viewPerm != null) {
			bean.setViewPerm(viewPerm);
		}
		if (contriPerm != null) {
			bean.setContriPerm(contriPerm);
		}
		if (commentPerm != null) {
			bean.setCommentPerm(commentPerm);
		}
		bean.applyDefaultValue();
		return bean;
	}

	@Transactional
	public void update(MemberGroup group, Integer[] viewNodeIds, Integer[] contriNodeIds, Integer[] commentNodeIds,
			Integer siteId) {
		List<Node> nodes = nodeQueryService.findList(siteId, null);
		for (Node node : nodes) {
			Boolean viewPerm = viewNodeIds == null ? null : ArrayUtils.contains(viewNodeIds, node.getId());
			Boolean contriPerm = contriNodeIds == null ? null : ArrayUtils.contains(contriNodeIds, node.getId());
			Boolean commentPerm = commentNodeIds == null ? null : ArrayUtils.contains(commentNodeIds, node.getId());
			NodeMemberGroup nodeGroup = findOrCreate(node, group, viewPerm, contriPerm, commentPerm);
			node.getNodeGroups().add(nodeGroup);
			group.getNodeGroups().add(nodeGroup);
		}
	}

	@Transactional
	public void update(Node node, Integer[] viewGroupIds, Integer[] contriGroupIds, Integer[] commentGroupIds) {
		List<MemberGroup> groups = memberGroupService.findList();
		for (MemberGroup group : groups) {
			Boolean viewPerm = viewGroupIds == null ? null : ArrayUtils.contains(viewGroupIds, group.getId());
			Boolean contriPerm = contriGroupIds == null ? null : ArrayUtils.contains(contriGroupIds, group.getId());
			Boolean commentPerm = commentGroupIds == null ? null : ArrayUtils.contains(commentGroupIds, group.getId());
			NodeMemberGroup nodeGroup = findOrCreate(node, group, viewPerm, contriPerm, commentPerm);
			node.getNodeGroups().add(nodeGroup);
			group.getNodeGroups().add(nodeGroup);
		}
	}

	private NodeQueryService nodeQueryService;
	private MemberGroupService memberGroupService;

	@Autowired
	public void setNodeQueryService(NodeQueryService nodeQueryService) {
		this.nodeQueryService = nodeQueryService;
	}

	@Autowired
	public void setMemberGroupService(MemberGroupService memberGroupService) {
		this.memberGroupService = memberGroupService;
	}

	private NodeMemberGroupDao dao;

	@Autowired
	public void setDao(NodeMemberGroupDao dao) {
		this.dao = dao;
	}
}

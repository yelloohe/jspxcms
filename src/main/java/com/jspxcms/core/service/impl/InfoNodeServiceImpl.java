package com.jspxcms.core.service.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoNode;
import com.jspxcms.core.domain.InfoNode.InfoNodeId;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.repository.InfoNodeDao;
import com.jspxcms.core.service.InfoNodeService;
import com.jspxcms.core.service.NodeQueryService;

@Service
@Transactional(readOnly = true)
public class InfoNodeServiceImpl implements InfoNodeService {
	private InfoNode findOrCreate(Info info, Node node) {
		InfoNode bean = dao.findOne(new InfoNodeId(info.getId(), node.getId()));
		if (bean == null) {
			bean = new InfoNode(info, node);
		}
		return bean;
	}

	@Transactional
	public List<InfoNode> update(Info info, Integer[] nodeIds, Integer nodeId) {
		// 主栏目为null，不更新。
		if (nodeId == null) {
			return null;
		}
		if (nodeIds == null) {
			nodeIds = new Integer[0];
		} else {
			nodeIds = ArrayUtils.removeElement(nodeIds, nodeId);
		}
		List<InfoNode> infoNodes = info.getInfoNodes();
		infoNodes.clear();
		infoNodes.add(findOrCreate(info, nodeService.get(nodeId)));
		for (Integer id : nodeIds) {
			infoNodes.add(findOrCreate(info, nodeService.get(id)));
		}
		return infoNodes;
	}

	@Transactional
	public int moveByNodeId(Collection<Integer> nodeIds, Integer nodeId) {
		return dao.moveByNodeId(nodeIds, nodeId);
	}

	@Override
	@Transactional
	public void deleteByNodeId(Integer nodeId) {
		Node node = nodeService.get(nodeId);
		for (Info info : dao.findByNodesNodeId(nodeId)) {
			InfoNode infoNode = new InfoNode(info, node);
			info.getInfoNodes().remove(infoNode);
			dao.delete(infoNode);
		}
	}

	private NodeQueryService nodeService;

	@Autowired
	public void setNodeQueryService(NodeQueryService nodeService) {
		this.nodeService = nodeService;
	}

	// private InfoQueryService infoService;
	//
	// @Autowired
	// public void setInfoService(InfoQueryService infoService) {
	// this.infoService = infoService;
	// }

	private InfoNodeDao dao;

	@Autowired
	public void setDao(InfoNodeDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeDetail;
import com.jspxcms.core.repository.NodeDetailDao;
import com.jspxcms.core.service.NodeDetailService;

/**
 * NodeDetailServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class NodeDetailServiceImpl implements NodeDetailService {
	@Transactional
	public void save(NodeDetail detail, Node node) {
		node.setDetail(detail);
		detail.setNode(node);
		detail.applyDefaultValue();
		dao.save(detail);
	}

	@Transactional
	public NodeDetail update(NodeDetail detail, Node node) {
		detail.setNode(node);
		detail.applyDefaultValue();
		NodeDetail entity = dao.save(detail);
		node.setDetail(detail);
		return entity;
	}

	private NodeDetailDao dao;

	@Autowired
	public void setDao(NodeDetailDao dao) {
		this.dao = dao;
	}
}

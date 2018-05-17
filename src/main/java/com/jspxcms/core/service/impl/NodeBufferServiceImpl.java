package com.jspxcms.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeBuffer;
import com.jspxcms.core.repository.NodeBufferDao;
import com.jspxcms.core.service.NodeBufferService;
import com.jspxcms.core.service.NodeQueryService;

/**
 * NodeBufferServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class NodeBufferServiceImpl implements NodeBufferService {
	public NodeBuffer get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public NodeBuffer save(NodeBuffer bean, Node node) {
		bean.setNode(node);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		node.setBuffer(bean);
		return bean;
	}

	@Transactional
	public int updateViews(Integer id) {
		Node node = nodeQueryService.get(id);
		NodeBuffer buffer = node.getBuffer();
		if (buffer == null) {
			buffer = new NodeBuffer();
			save(buffer, node);
		}
		int views = node.getViews();
		int buffViews = buffer.getViews() + 1;
		// 根据缓冲设置处理缓冲
		if (buffViews >= node.getSite().getGlobal().getOther()
				.getBufferNodeViews()) {
			buffer.setViews(0);
			node.setViews(views + buffViews);
		} else {
			buffer.setViews(buffViews);
		}
		return views + buffViews;
	}

	private NodeQueryService nodeQueryService;
	private NodeBufferDao dao;

	@Autowired
	public void setNodeQueryService(NodeQueryService nodeQueryService) {
		this.nodeQueryService = nodeQueryService;
	}

	@Autowired
	public void setDao(NodeBufferDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeDetail;

/**
 * NodeDetailService
 * 
 * @author liufang
 * 
 */
public interface NodeDetailService {
	public void save(NodeDetail detail, Node node);

	public NodeDetail update(NodeDetail detail, Node node);
}

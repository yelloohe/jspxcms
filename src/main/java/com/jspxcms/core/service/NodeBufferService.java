package com.jspxcms.core.service;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeBuffer;

/**
 * NodeBufferService
 * 
 * @author liufang
 * 
 */
public interface NodeBufferService {
	public NodeBuffer get(Integer id);

	public NodeBuffer save(NodeBuffer bean, Node node);

	public int updateViews(Integer id);
}

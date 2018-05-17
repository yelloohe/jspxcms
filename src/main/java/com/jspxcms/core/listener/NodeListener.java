package com.jspxcms.core.listener;

import com.jspxcms.core.domain.Node;

/**
 * NodeListener
 * 
 * @author liufang
 * 
 */
public interface NodeListener {
	public void postNodeSave(Node[] beans);

	public void postNodeUpdate(Node[] beans);

	public void postNodeDelete(Node[] beans);
}

package com.jspxcms.core.listener;

import com.jspxcms.core.domain.Node;

/**
 * AbstractNodeListener
 * 
 * @author liufang
 * 
 */
public abstract class AbstractNodeListener implements NodeListener,
		NodeDeleteListener {
	public void postNodeSave(Node[] beans) {
	}

	public void postNodeUpdate(Node[] beans) {
	}

	public void preNodeDelete(Integer[] ids) {
	}

	public void postNodeDelete(Node[] beans) {
	}
}

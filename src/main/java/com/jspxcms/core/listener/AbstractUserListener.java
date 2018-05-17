package com.jspxcms.core.listener;

import com.jspxcms.core.domain.Info;

/**
 * AbstractUserListener
 * 
 * @author liufang
 * 
 */
public abstract class AbstractUserListener implements UserDeleteListener {
	public void postSave(Info[] beans) {
	}

	public void postUpdate(Info[] beans) {
	}

	public void preUserDelete(Integer[] ids) {
	}

	public void postDelete(Info[] beans) {
	}
}

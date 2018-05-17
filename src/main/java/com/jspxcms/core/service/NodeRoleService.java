package com.jspxcms.core.service;

import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.Role;

public interface NodeRoleService {
	public void update(Role role, Integer[] infoPermIds, Integer[] nodePermIds);

	public void update(Node node, Integer[] infoPermIds, Integer[] nodePermIds);
}

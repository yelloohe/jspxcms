package com.jspxcms.core.fulltext;

import com.jspxcms.core.service.TaskService;

/**
 * InfoFulltextDao
 * 
 * @author liufang
 * 
 */
public interface InfoFulltextDao {
	public int addDocument(Integer siteId, String treeNumber,
			TaskService taskService, Integer taskId);
}

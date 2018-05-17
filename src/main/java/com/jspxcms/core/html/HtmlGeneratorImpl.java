package com.jspxcms.core.html;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.Task;
import com.jspxcms.core.service.TaskService;

public class HtmlGeneratorImpl implements HtmlGenerator {
	private final Logger logger = LoggerFactory.getLogger(HtmlGeneratorImpl.class);

	private static final int INFO_MAX_RESULT = 5;
	private static final int NODE_MAX_RESULT = 1;

	public void makeAll(final Integer siteId, String siteName, final Integer userId, final boolean forUpdate) {
		if (StringUtils.isBlank(siteName)) {
			siteName = siteId.toString();
		}
		Task task = taskService.save("Site: " + siteName, null, Task.NODE_HTML, userId, siteId);
		final Integer taskId = task.getId();
		new Thread() {
			public void run() {
				Integer lastId;
				try {
					lastId = null;
					do {
						lastId = htmlService.makeNode(siteId, null, true, taskId, forUpdate, lastId, NODE_MAX_RESULT);
					} while (lastId != null);

					lastId = null;
					do {
						lastId = htmlService.makeInfo(siteId, null, true, taskId, forUpdate, lastId, INFO_MAX_RESULT);
					} while (lastId != null);
					taskService.finish(taskId);
				} catch (Exception e) {
					taskService.abort(taskId);
					logger.error("make html error!", e);
				}
			}
		}.start();
	}

	public void makeInfo(final Integer siteId, final Integer nodeId, String nodeName, final boolean includeChildren,
			Integer userId) {
		if (StringUtils.isNotBlank(nodeName)) {
			nodeName = "ALL";
		}
		Task task = taskService.save("Node: " + nodeName, null, Task.INFO_HTML, userId, siteId);
		final Integer taskId = task.getId();
		new Thread() {
			public void run() {
				try {
					Integer lastId = null;
					do {
						lastId = htmlService.makeInfo(siteId, nodeId, includeChildren, taskId, false, lastId,
								INFO_MAX_RESULT);
					} while (lastId != null);
					taskService.finish(taskId);
				} catch (Exception e) {
					taskService.abort(taskId);
					logger.error("make html error!", e);
				}
			}
		}.start();
	}

	public void makeNode(final Integer siteId, final Integer nodeId, String nodeName, final boolean includeChildren,
			Integer userId) {
		if (StringUtils.isNotBlank(nodeName)) {
			nodeName = "ALL";
		}
		Task task = taskService.save("Node: " + nodeName, null, Task.NODE_HTML, userId, siteId);
		final Integer taskId = task.getId();
		new Thread() {
			public void run() {
				try {
					Integer lastId = null;
					do {
						htmlService.makeNode(siteId, nodeId, includeChildren, taskId, false, lastId, NODE_MAX_RESULT);
					} while (lastId != null);
					taskService.finish(taskId);
				} catch (Exception e) {
					taskService.abort(taskId);
					logger.error("make html error!", e);
				}
			}
		}.start();
	}

	public void deleteHtml(Node node) {
		if (node == null) {
			return;
		}
		Site site = node.getSite();
		String filename = node.getUrlStatic(1, false, true, false);
		FileHandler fileHandler = site.getHtmlPublishPoint().getFileHandler(pathResolver);
		fileHandler.delete(filename);
		// 手机端域名存在，并且手机端发布点与PC端发布点不相同
		if (StringUtils.isNotBlank(site.getMobileDomain())
				&& !site.getHtmlPublishPoint().getId().equals(site.getMobilePublishPoint().getId())) {
			filename = node.getUrlStatic(1, false, true, true);
			fileHandler = node.getSite().getHtmlPublishPoint().getFileHandler(pathResolver);
			fileHandler.delete(filename);
		}
	}

	private PathResolver pathResolver;
	private HtmlService htmlService;
	private TaskService taskService;

	@Autowired
	public void setPathResolver(PathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}

	@Autowired
	public void setHtmlService(HtmlService htmlService) {
		this.htmlService = htmlService;
	}

	@Autowired
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
}

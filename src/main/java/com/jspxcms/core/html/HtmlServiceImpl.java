package com.jspxcms.core.html;

import static com.jspxcms.core.domain.Node.STATIC_INFO_NODE;
import static com.jspxcms.core.domain.Node.STATIC_INFO_NODE_PARENT;
import static com.jspxcms.core.domain.Node.STATIC_INFO_NODE_PARENT_LIST;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.repository.InfoDao;
import com.jspxcms.core.repository.NodeDao;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.NodeQueryService;
import com.jspxcms.core.service.TaskService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * HtmlServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional
public class HtmlServiceImpl implements HtmlService {
	private static final Logger logger = LoggerFactory
			.getLogger(HtmlServiceImpl.class);

	public void makeInfo(Integer infoId) {
		Info info = infoQuery.get(infoId);
		makeInfo(info);
	}

	public void makeInfo(Info info) {
		if (info == null) {
			return;
		}
//		FileHandler fileHandler = info.getSite().getHtmlPublishPoint()
//				.getFileHandler(pathResolver);
		try {
			PInfo.makeHtml(info, getConfig(), pathResolver, taskService, null);
		} catch (Exception e) {
			logger.error(null, e);
		}
	}

	public void deleteInfo(Info info) {
		if (info == null) {
			return;
		}

//		FileHandler fileHandler = info.getSite().getHtmlPublishPoint()
//				.getFileHandler(pathResolver);
		try {
			PInfo.deleteHtml(info, pathResolver);
		} catch (Exception e) {
			logger.error(null, e);
		}
	}

	public void makeNode(Integer nodeId) {
		Node node = nodeQuery.get(nodeId);
		makeNode(node);
	}

	public void makeNode(Node node) {
		if (node == null) {
			return;
		}
		int method = node.getStaticMethodOrDef();
		// 触发生成只生成列表页第一页。
		int max = 1;
		if (STATIC_INFO_NODE_PARENT_LIST == method) {
			max = Integer.MAX_VALUE;
		}
//		FileHandler fileHandler;
		if (STATIC_INFO_NODE_PARENT == method
				|| STATIC_INFO_NODE_PARENT_LIST == method) {
			while (node != null) {
//				fileHandler = node.getSite().getHtmlPublishPoint()
//						.getFileHandler(pathResolver);
				try {
					PNode.makeHtml(node, max, getConfig(), pathResolver,
							taskService, null);
				} catch (Exception e) {
					logger.error(null, e);
				}
				node = node.getParent();
			}
		} else if (STATIC_INFO_NODE == method) {
//			fileHandler = node.getSite().getHtmlPublishPoint()
//					.getFileHandler(pathResolver);
			try {
				PNode.makeHtml(node, max, getConfig(), pathResolver,
						taskService, null);
			} catch (Exception e) {
				logger.error(null, e);
			}
		} else {
			// do nothing
		}
	}

	public void makeHome(Integer siteId) throws IOException, TemplateException {
		Node node = nodeQuery.findRoot(siteId);
		if (node != null) {
//			FileHandler fileHandler = node.getSite().getHtmlPublishPoint()
//					.getFileHandler(pathResolver);
			PNode.makeHtml(node, 1, getConfig(), pathResolver, taskService, null);
		}
	}

	public Integer makeInfo(Integer siteId, Integer nodeId,
			boolean includeChildren, Integer taskId, boolean forUpdate,
			Integer lastId, int maxResult) throws IOException,
			TemplateException {
		String treeNumber = null;
		if (includeChildren && nodeId != null) {
			Node node = nodeQuery.get(nodeId);
			treeNumber = node.getTreeNumber();
		}
		List<Info> list = infoDao.findForHtml(siteId, nodeId, treeNumber,
				forUpdate, lastId, maxResult);
//		FileHandler fileHandler;
		Iterator<Info> it = list.iterator();
		Info info;
		while (it.hasNext() && taskService.isRunning(taskId)) {
			info = it.next();
			lastId = info.getId();
//			fileHandler = info.getSite().getHtmlPublishPoint()
//					.getFileHandler(pathResolver);
			PInfo.makeHtml(info, getConfig(), pathResolver, taskService, taskId);
		}
		if (list.size() < maxResult) {
			lastId = null;
		}
		return lastId;
	}

	public Integer makeNode(Integer siteId, Integer nodeId,
			boolean includeChildren, Integer taskId, boolean forUpdate,
			Integer lastId, int maxResult) throws IOException,
			TemplateException {
		String treeNumber = null;
		if (includeChildren && nodeId != null) {
			Node node = nodeQuery.get(nodeId);
			treeNumber = node.getTreeNumber();
		}
		List<Node> list = nodeDao.findForHtml(siteId, nodeId, treeNumber,
				forUpdate, lastId, maxResult);
//		FileHandler fileHandler;
		Iterator<Node> it = list.iterator();
		Node node;
		while (it.hasNext() && taskService.isRunning(taskId)) {
			node = it.next();
			lastId = node.getId();
//			fileHandler = node.getSite().getHtmlPublishPoint()
//					.getFileHandler(pathResolver);
			PNode.makeHtml(node, Integer.MAX_VALUE, getConfig(), pathResolver,
					taskService, taskId);
		}
		if (list.size() < maxResult) {
			lastId = null;
		}
		return lastId;
	}

	private Configuration getConfig() {
		return freeMarkerConfigurer.getConfiguration();
	}

	private PathResolver pathResolver;
	private FreeMarkerConfigurer freeMarkerConfigurer;
	private TaskService taskService;
	private InfoQueryService infoQuery;
	private NodeQueryService nodeQuery;

	@Autowired
	public void setPathResolver(PathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}

	@Autowired
	public void setFreeMarkerConfigurer(
			FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}

	@Autowired
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	@Autowired
	public void setInfoQuery(InfoQueryService infoQuery) {
		this.infoQuery = infoQuery;
	}

	@Autowired
	public void setNodeQuery(NodeQueryService nodeQuery) {
		this.nodeQuery = nodeQuery;
	}

	private InfoDao infoDao;
	private NodeDao nodeDao;

	@Autowired
	public void setInfoDao(InfoDao infoDao) {
		this.infoDao = infoDao;
	}

	@Autowired
	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
	}

}

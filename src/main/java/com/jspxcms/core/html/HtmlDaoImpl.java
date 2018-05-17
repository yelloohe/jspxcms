package com.jspxcms.core.html;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.service.TaskService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * HtmlDaoImpl
 * 
 * @author liufang
 * 
 */
@Repository
public class HtmlDaoImpl implements HtmlDao {
	public int makeNode(Integer siteId, Integer nodeId, String treeNumber,
			Configuration config, PathResolver pathResolver,
			TaskService taskService, Integer taskId, boolean forUpdate)
			throws IOException, TemplateException {
		Session session = em.unwrap(Session.class);
		// Session session = (Session) em.getDelegate();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder("from Node bean where 1=1");
		hql.append(" and bean.site.id = :siteId");
		params.put("siteId", siteId);
		if (forUpdate) {
			hql.append(" and (bean.htmlStatus = '" + Node.HTML_TOBE_DELETE);
			hql.append("' or bean.htmlStatus = '" + Node.HTML_TOBE_UPDATE);
			hql.append("')");
		}
		if (StringUtils.isNotBlank(treeNumber)) {
			hql.append(" and bean.treeNumber like :treeNumber");
			params.put("treeNumber", treeNumber + "%");
		} else if (nodeId != null) {
			hql.append(" and bean.id = :nodeId");
			params.put("nodeId", nodeId);
		}
		Query query = session.createQuery(hql.toString());
		query.setProperties(params);
		query.setCacheMode(CacheMode.IGNORE);
		ScrollableResults nodes = query.scroll(ScrollMode.FORWARD_ONLY);
//		FileHandler fileHandler;
		Node node;
		int count = 0;
		while (nodes.next() && taskService.isRunning(taskId)) {
			node = (Node) nodes.get(0);
//			fileHandler = node.getSite().getHtmlPublishPoint()
//					.getFileHandler(pathResolver);
			PNode.makeHtml(node, Integer.MAX_VALUE, config, pathResolver,
					taskService, taskId);
			// 一个节点可能有很多翻页，这里稍微设置小一点
			if (++count % 5 == 0) {
				session.flush();
				session.clear();
			}
		}
		return count;
	}

	public int makeInfo(Integer siteId, Integer nodeId, String treeNumber,
			Configuration config, PathResolver pathResolver,
			TaskService taskService, Integer taskId, boolean forUpdate)
			throws IOException, TemplateException {
		Session session = (Session) em.getDelegate();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder("from Info bean where 1=1");
		hql.append(" and bean.site.id = :siteId");
		if (forUpdate) {
			hql.append(" and (bean.htmlStatus = '" + Node.HTML_TOBE_DELETE);
			hql.append("' or bean.htmlStatus = '" + Node.HTML_TOBE_UPDATE);
			hql.append("')");
		}
		params.put("siteId", siteId);
		if (StringUtils.isNotBlank(treeNumber)) {
			hql.append(" and bean.node.treeNumber like :treeNumber");
			params.put("treeNumber", treeNumber + "%");
		} else if (nodeId != null) {
			hql.append(" and bean.node.id = :nodeId");
			params.put("nodeId", nodeId);
		}
		Query query = session.createQuery(hql.toString());
		query.setProperties(params);
		query.setCacheMode(CacheMode.IGNORE);
		ScrollableResults infos = query.scroll(ScrollMode.FORWARD_ONLY);
//		FileHandler fileHandler;
		Info info;
		int count = 0;
		while (infos.next() && taskService.isRunning(taskId)) {
			info = (Info) infos.get(0);
//			fileHandler = info.getSite().getHtmlPublishPoint()
//					.getFileHandler(pathResolver);
			PInfo.makeHtml(info, config, pathResolver, taskService, taskId);
			if (++count % 20 == 0) {
				session.flush();
				session.clear();
			}
		}
		return count;
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

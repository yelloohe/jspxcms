package com.jspxcms.core.fulltext;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jspxcms.common.fulltext.LuceneIndexTemplate;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.service.TaskService;

/**
 * InfoFulltextDaoImpl
 * 
 * @author liufang
 * 
 */
@Repository
public class InfoFulltextDaoImpl implements InfoFulltextDao {
	public int addDocument(Integer siteId, String treeNumber,
			TaskService taskService, Integer taskId) {
		Session session = (Session) em.getDelegate();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder(
				"from Info bean where bean.status='A'");
		if (StringUtils.isNotBlank(treeNumber)) {
			hql.append(" and bean.node.treeNumber like :treeNumber");
			params.put("treeNumber", treeNumber + "%");
		}
		Query query = session.createQuery(hql.toString());
		query.setProperties(params);
		query.setCacheMode(CacheMode.IGNORE);
		ScrollableResults infos = query.scroll(ScrollMode.FORWARD_ONLY);
		Info info;
		int count = 0;
		while (infos.next() && taskService.isRunning(taskId)) {
			info = (Info) infos.get(0);
			template.addDocument(FInfo.doc(info));
			taskService.add(taskId, 1);
			if (++count % 20 == 0) {
				session.flush();
				session.clear();
			}
		}
		return count;
	}

	private LuceneIndexTemplate template;

	@Autowired
	public void setLuceneIndexTemplate(LuceneIndexTemplate template) {
		this.template = template;
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}

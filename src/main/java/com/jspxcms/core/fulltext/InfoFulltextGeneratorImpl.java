package com.jspxcms.core.fulltext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.fulltext.LuceneIndexTemplate;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.Task;
import com.jspxcms.core.service.TaskService;

public class InfoFulltextGeneratorImpl implements InfoFulltextGenerator {
	private final Logger logger = LoggerFactory
			.getLogger(InfoFulltextGeneratorImpl.class);

	public void addDocument(final Info[] beans) {
		new Thread() {
			@Override
			public void run() {
				try {
					// 等待主线程结束，提交事务后，再运行。
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					logger.error(null, e);
				}
				for (Info bean : beans) {
					infoFulltextService.addDocument(bean.getId());
				}
			}
		}.start();
	}

	public void updateDocument(final Info[] beans) {
		new Thread() {
			@Override
			public void run() {
				try {
					// 等待主线程结束，提交事务后，再运行。
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					logger.error(null, e);
				}
				for (Info bean : beans) {
					infoFulltextService.updateDocument(bean.getId());
				}
			}
		}.start();
	}

	public void deleteDocuments(final Info[] beans) {
		for (Info bean : beans) {
			template.deleteDocuments(FInfo.id(bean.getId()));
		}
	}

	public void addDocument(final Integer siteId, final Node node,
			Integer userId) {
		String name = "ALL";
		if (node != null) {
			name = node.getName();
		}
		Task task = taskService.save("Node: " + name, null, Task.FULLTEXT,
				userId, siteId);
		final Integer taskId = task.getId();
		new Thread() {
			@Override
			public void run() {
				try {
					infoFulltextService.addDocument(siteId, node, taskService,
							taskId);
					taskService.finish(taskId);
				} catch (Exception e) {
					taskService.abort(taskId);
					logger.error("create fulltext index error!", e);
				}
			}
		}.start();
	}

	private TaskService taskService;
	private InfoFulltextService infoFulltextService;
	private LuceneIndexTemplate template;

	@Autowired
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	@Autowired
	public void setInfoFulltextService(InfoFulltextService infoFulltextService) {
		this.infoFulltextService = infoFulltextService;
	}

	@Autowired
	public void setLuceneIndexTemplate(LuceneIndexTemplate template) {
		this.template = template;
	}
}

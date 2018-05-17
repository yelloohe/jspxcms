package com.jspxcms.ext.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.jspxcms.core.constant.Constants;
import com.jspxcms.ext.collect.Collector;

/**
 * 采集任务
 * 
 * @author liufang
 * 
 */
public class CollectJob implements Job {
	private static final Logger logger = LoggerFactory
			.getLogger(CollectJob.class);

	public static final String COLLECT_ID = "collectId";

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			ApplicationContext appContext = (ApplicationContext) context
					.getScheduler().getContext().get(Constants.APP_CONTEXT);
			Collector collector = appContext.getBean(Collector.class);
			JobDataMap map = context.getJobDetail().getJobDataMap();
			Integer collectId = map.getIntegerFromString(COLLECT_ID);
			collector.start(collectId);
			logger.info("run collect job: " + collectId);
		} catch (SchedulerException e) {
			throw new JobExecutionException("Cannot get ApplicationContext", e);
		}
	}
}

package com.jspxcms.core.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.jspxcms.core.constant.Constants;
import com.jspxcms.core.service.InfoService;

/**
 * 信息发布任务
 * 
 * @author liufang
 * 
 */
public class InfoPublishJob implements Job {
	private static final Logger logger = LoggerFactory
			.getLogger(InfoPublishJob.class);

	public static final String SITE_ID = "siteId";

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			ApplicationContext appContext = (ApplicationContext) context
					.getScheduler().getContext().get(Constants.APP_CONTEXT);
			InfoService service = appContext.getBean(InfoService.class);
			JobDataMap map = context.getJobDetail().getJobDataMap();
			Integer siteId = map.getIntegerFromString(SITE_ID);
			service.publish(siteId);
			service.tobePublish(siteId);
			service.expired(siteId);
			logger.info("run info publish job");
		} catch (SchedulerException e) {
			throw new JobExecutionException("Cannot get ApplicationContext", e);
		}
	}
}

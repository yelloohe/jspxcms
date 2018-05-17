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
import com.jspxcms.core.html.HtmlService;

/**
 * 首页生成任务
 * 
 * @author liufang
 * 
 */
public class HtmlHomeJob implements Job {
	private static final Logger logger = LoggerFactory
			.getLogger(HtmlHomeJob.class);

	public static final String SITE_ID = "siteId";

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			ApplicationContext appContext = (ApplicationContext) context
					.getScheduler().getContext().get(Constants.APP_CONTEXT);
			HtmlService htmlService = appContext.getBean(HtmlService.class);
			JobDataMap map = context.getJobDetail().getJobDataMap();
			Integer siteId = map.getIntegerFromString(SITE_ID);
			try {
				htmlService.makeHome(siteId);
			} catch (Exception e) {
				logger.error(null, e);
			}
			logger.info("run html home job");
		} catch (SchedulerException e) {
			throw new JobExecutionException("Cannot get ApplicationContext", e);
		}
	}
}

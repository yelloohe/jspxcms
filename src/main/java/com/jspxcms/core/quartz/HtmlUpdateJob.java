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
import com.jspxcms.core.html.HtmlGenerator;

/**
 * 首页更新任务
 * 
 * @author liufang
 * 
 */
public class HtmlUpdateJob implements Job {
	private static final Logger logger = LoggerFactory
			.getLogger(HtmlUpdateJob.class);

	public static final String SITE_ID = "siteId";
	public static final String SITE_NAME = "siteName";
	public static final String USER_ID = "userId";

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			ApplicationContext appContext = (ApplicationContext) context
					.getScheduler().getContext().get(Constants.APP_CONTEXT);
			HtmlGenerator htmlGenerator = appContext
					.getBean(HtmlGenerator.class);
			JobDataMap map = context.getJobDetail().getJobDataMap();
			Integer siteId = map.getIntegerFromString(SITE_ID);
			String siteName = map.getString(SITE_NAME);
			Integer userId = map.getIntegerFromString(USER_ID);
			htmlGenerator.makeAll(siteId, siteName, userId, true);
			context.setResult(null);
			logger.info("run html home job");
		} catch (SchedulerException e) {
			throw new JobExecutionException("Cannot get ApplicationContext", e);
		}
	}
}

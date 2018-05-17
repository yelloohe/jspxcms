package com.jspxcms.core.quartz;

import java.util.List;

import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.core.domain.ScheduleJob;
import com.jspxcms.core.service.ScheduleJobService;

public class DynamicSchedulerImpl implements DynamicScheduler, InitializingBean {
	public void afterPropertiesSet() throws Exception {
		scheduler.clear();
		List<ScheduleJob> list = service.findAll();
		for (ScheduleJob job : list) {
			service.scheduleJob(job);
		}
	}

	private ScheduleJobService service;
	private Scheduler scheduler;

	@Autowired
	public void setService(ScheduleJobService service) {
		this.service = service;
	}

	@Autowired
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
}

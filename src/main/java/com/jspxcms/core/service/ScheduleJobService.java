package com.jspxcms.core.service;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.ScheduleJob;

public interface ScheduleJobService {
	public List<ScheduleJob> findList(Integer siteId,
			Map<String, String[]> params, Sort sort);

	public RowSide<ScheduleJob> findSide(Integer siteId,
			Map<String, String[]> params, ScheduleJob bean, Integer position,
			Sort sort);

	public List<ScheduleJob> findAll();

	public ScheduleJob get(Integer id);

	public ScheduleJob save(ScheduleJob bean, Map<String, String> dataMap,
			Integer userId, Integer siteId);

	public ScheduleJob update(ScheduleJob bean, Map<String, String> dataMap,
			Integer userId);

	public ScheduleJob delete(Integer id);

	public List<ScheduleJob> delete(Integer[] ids);

	public void fetchNextFireTime(Collection<ScheduleJob> jobs);

	public void scheduleJob(ScheduleJob job) throws SchedulerException,
			ClassNotFoundException, ParseException;

	public void rescheduleJob(ScheduleJob job) throws SchedulerException,
			ParseException, ClassNotFoundException;

	public void unscheduleJob(ScheduleJob job) throws SchedulerException;

	public void unscheduleJobs(List<ScheduleJob> jobs)
			throws SchedulerException;
}

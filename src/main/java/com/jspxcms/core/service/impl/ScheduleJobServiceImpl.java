package com.jspxcms.core.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.util.CollectionUtils;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.ScheduleJob;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.ScheduleJobDao;
import com.jspxcms.core.service.ScheduleJobService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.DeleteException;

@Service
@Transactional(readOnly = true)
public class ScheduleJobServiceImpl implements ScheduleJobService,
		UserDeleteListener, SiteDeleteListener {
	private static final Logger logger = LoggerFactory
			.getLogger(ScheduleJobServiceImpl.class);

	public List<ScheduleJob> findList(Integer siteId,
			Map<String, String[]> params, Sort sort) {
		return dao.findAll(spec(siteId, params), sort);
	}

	public RowSide<ScheduleJob> findSide(Integer siteId,
			Map<String, String[]> params, ScheduleJob bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<ScheduleJob>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<ScheduleJob> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<ScheduleJob> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<ScheduleJob> fsp = SearchFilter.spec(filters,
				ScheduleJob.class);
		Specification<ScheduleJob> sp = new Specification<ScheduleJob>() {
			public Predicate toPredicate(Root<ScheduleJob> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site")
							.<Integer> get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	public List<ScheduleJob> findAll() {
		return dao.findAll();
	}

	public ScheduleJob get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public ScheduleJob save(ScheduleJob bean, Map<String, String> dataMap,
			Integer userId, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		User user = userService.get(userId);
		bean.setUser(user);
		bean.setDataMap(dataMap);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public ScheduleJob update(ScheduleJob bean, Map<String, String> dataMap,
			Integer userId) {
		bean.setDataMap(dataMap);
		if (userId != null) {
			User user = userService.get(userId);
			bean.setUser(user);
		}
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public ScheduleJob delete(Integer id) {
		ScheduleJob entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public List<ScheduleJob> delete(Integer[] ids) {
		List<ScheduleJob> beans = new ArrayList<ScheduleJob>();
		for (int i = 0; i < ids.length; i++) {
			beans.add(delete(ids[i]));
		}
		return beans;
	}

	public void fetchNextFireTime(Collection<ScheduleJob> jobs) {
		if (CollectionUtils.isEmpty(jobs)) {
			return;
		}
		for (ScheduleJob job : jobs) {
			if (!job.isEnabled()) {
				continue;
			}
			try {
				Trigger trigger = scheduler.getTrigger(job.getTriggerKey());
				if (trigger != null) {
					job.setNextFireTime(trigger.getNextFireTime());
				}
			} catch (SchedulerException e) {
				logger.error(null, e);
			}
		}
	}

	@Transactional
	public void scheduleJob(ScheduleJob job) throws SchedulerException,
			ClassNotFoundException, ParseException {
		if (job.isEnabled()
				&& scheduler.getTrigger(job.getTriggerKey()) == null) {
			JobDetail detail = job.getJobDetail();
			Trigger trigger = job.getTrigger();
			scheduler.scheduleJob(detail, trigger);
		}
	}

	@Transactional
	public void rescheduleJob(ScheduleJob job) throws SchedulerException,
			ParseException, ClassNotFoundException {
		TriggerKey key = job.getTriggerKey();
		scheduler.unscheduleJob(key);
		scheduleJob(job);
	}

	@Transactional
	public void unscheduleJob(ScheduleJob job) throws SchedulerException {
		TriggerKey key = job.getTriggerKey();
		scheduler.unscheduleJob(key);
	}

	@Transactional
	public void unscheduleJobs(List<ScheduleJob> jobs)
			throws SchedulerException {
		for (ScheduleJob job : jobs) {
			unscheduleJob(job);
		}
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("scheduleJob.management");
			}
		}
	}

	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countByUserId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("scheduleJob.management");
			}
		}
	}

	private Scheduler scheduler;
	private UserService userService;
	private SiteService siteService;

	@Autowired
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private ScheduleJobDao dao;

	@Autowired
	public void setDao(ScheduleJobDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.ext.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.ext.domain.VisitLog;

public interface VisitLogService {
	public Page<VisitLog> findAll(Integer siteId, Map<String, String[]> params,
			Pageable pageable);

	public RowSide<VisitLog> findSide(Integer siteId,
			Map<String, String[]> params, VisitLog bean, Integer position,
			Sort sort);

	// public List<Object[]> trafficByDate(Date date, Integer siteId);

	public List<Object[]> trafficByDay(Date begin, Date end, Integer siteId);

	public List<Object[]> trafficByHour(Date begin, Date end, Integer siteId);

	public List<Object[]> trafficLast30Minute(Integer siteId);

	public List<Object[]> trafficByMinute(Date begin, Date end, Integer siteId);

	public List<Object[]> sourceCount(Date begin, Date end, Integer siteId,
			List<Object[]> sourceList, int maxSize);

	public Page<Object[]> sourceByTime(Date begin, Date end, Integer siteId,
			Pageable pageable);

	public Page<Object[]> urlByTime(Date begin, Date end, Integer siteId,
			Pageable pageable);

	public List<Object[]> countryByTime(Date begin, Date end, Integer siteId);

	public List<Object[]> browserByTime(Date begin, Date end, Integer siteId);

	public List<Object[]> osByTime(Date begin, Date end, Integer siteId);

	public List<Object[]> deviceByTime(Date begin, Date end, Integer siteId);

	public VisitLog get(Integer id);

	public VisitLog save(String url, String referrer, String ip, String cookie,
			String userAgent, User user, Site site);

	public VisitLog delete(Integer id);

	public List<VisitLog> delete(Integer[] ids);

	public long deleteByDate(Date before, Integer siteId);
}

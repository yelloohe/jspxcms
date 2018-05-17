package com.jspxcms.ext.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.ip.IPSeeker;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.ext.domain.VisitLog;
import com.jspxcms.ext.repository.VisitLogDao;
import com.jspxcms.ext.service.VisitLogService;

import eu.bitwalker.useragentutils.UserAgent;

@Service
@Transactional(readOnly = true)
public class VisitLogServiceImpl implements VisitLogService, SiteDeleteListener, UserDeleteListener {
	private static final Logger logger = LoggerFactory.getLogger(VisitLogServiceImpl.class);

	public Page<VisitLog> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(siteId, params), pageable);
	}

	public RowSide<VisitLog> findSide(Integer siteId, Map<String, String[]> params, VisitLog bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<VisitLog>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<VisitLog> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<VisitLog> spec(final Integer siteId, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<VisitLog> fsp = SearchFilter.spec(filters, VisitLog.class);
		Specification<VisitLog> sp = new Specification<VisitLog>() {
			public Predicate toPredicate(Root<VisitLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site").<Integer>get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	// public List<Object[]> trafficByDate(Date date, Integer siteId) {
	// DateTime dt = new DateTime(date.getTime());
	// Date begin = dt.withMillisOfDay(0).toDate();
	// Date end = dt.plusDays(1).withMillisOfDay(0).toDate();
	// List<Object[]> list = trafficByDate(begin, end, siteId);
	// return list;
	// }
	//
	// public List<Object[]> trafficByTodayAndYesterday(Integer siteId) {
	// DateTime dt = new DateTime();
	// Date begin = dt.plusDays(-1).withMillisOfDay(0).toDate();
	// Date end = dt.plusDays(1).withMillisOfDay(0).toDate();
	// List<Object[]> list = trafficByDate(begin, end, siteId);
	// return list;
	// }

	public List<Object[]> trafficByDay(Date begin, Date end, Integer siteId) {
		List<Object[]> list = dao.trafficByDay(begin, end, siteId);
		List<Object[]> result = new ArrayList<Object[]>();
		DateTime dt = new DateTime(begin.getTime());
		try {
			for (Object[] arr : list) {
				Date d = VisitLog.DATE_FORMAT.parse((String) arr[0]);
				arr[0] = d;
				while (dt.toDate().compareTo(d) < 0) {
					result.add(new Object[] { dt.toDate(), 0L, 0L, 0L });
					dt = dt.plusDays(1);
				}
				result.add(arr);
				dt = dt.plusDays(1);
			}
			while (dt.toDate().compareTo(end) < 0) {
				result.add(new Object[] { dt.toDate(), 0L, 0L, 0L });
				dt = dt.plusDays(1);
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<Object[]> trafficByHour(Date begin, Date end, Integer siteId) {
		List<Object[]> list = dao.trafficByHour(begin, end, siteId);
		List<Object[]> result = new ArrayList<Object[]>();
		DateTime dt = new DateTime(begin.getTime());
		try {
			for (Object[] arr : list) {
				Date d = VisitLog.DATE_HH_FORMAT.parse((String) arr[0]);
				arr[0] = d;
				while (dt.toDate().compareTo(d) < 0) {
					result.add(new Object[] { dt.toDate(), 0L, 0L, 0L });
					dt = dt.plusHours(1);
				}
				result.add(arr);
				dt = dt.plusHours(1);
			}
			while (dt.toDate().compareTo(end) < 0) {
				result.add(new Object[] { dt.toDate(), 0L, 0L, 0L });
				dt = dt.plusHours(1);
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<Object[]> trafficLast30Minute(Integer siteId) {
		DateTime end = new DateTime();
		end = end.plusMinutes(1).withMillisOfSecond(0);
		DateTime begin = end.minusMinutes(30);
		return trafficByMinute(begin.toDate(), end.toDate(), siteId);
	}

	public List<Object[]> trafficByMinute(Date begin, Date end, Integer siteId) {
		List<Object[]> list = dao.trafficByMinute(begin, end, siteId);
		List<Object[]> result = new ArrayList<Object[]>();
		DateTime dt = new DateTime(begin.getTime());
		try {
			for (Object[] arr : list) {
				Date d = VisitLog.DATE_HHMM_FORMAT.parse((String) arr[0]);
				arr[0] = d;
				while (dt.toDate().compareTo(d) < 0) {
					result.add(new Object[] { dt.toDate(), 0L, 0L, 0L });
					dt = dt.plusMinutes(1);
				}
				result.add(arr);
				dt = dt.plusMinutes(1);
			}
			while (dt.toDate().compareTo(end) < 0) {
				result.add(new Object[] { dt.toDate(), 0L, 0L, 0L });
				dt = dt.plusMinutes(1);
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public Page<Object[]> sourceByTime(Date begin, Date end, Integer siteId, Pageable pageable) {
		return dao.sourceByTime(begin, end, siteId, pageable);
	}

	public List<Object[]> sourceCount(Date begin, Date end, Integer siteId, List<Object[]> sourceList, int maxSize) {
		List<Object[]> resultList = new ArrayList<Object[]>();
		List<Object[]> sourceCount = sourceCount(begin, end, siteId);
		long otherPv = (Long) sourceCount.get(0)[0];
		long otherUv = (Long) sourceCount.get(0)[1];
		long otherIp = (Long) sourceCount.get(0)[2];
		int index = 1;
		for (Object[] source : sourceList) {
			if (index++ > maxSize) {
				break;
			}
			otherPv -= (Long) source[1];
			otherUv -= (Long) source[1];
			otherIp -= (Long) source[1];
			resultList.add(source);
		}
		if (otherPv > 0) {
			resultList.add(new Object[] { VisitLog.SOURCE_OTHER, otherPv, otherUv, otherIp });
		}
		return resultList;
	}

	public List<Object[]> sourceCount(Date begin, Date end, Integer siteId) {
		return dao.sourceCount(begin, end, siteId);
	}

	public Page<Object[]> urlByTime(Date begin, Date end, Integer siteId, Pageable pageable) {
		return dao.urlByTime(begin, end, siteId, pageable);
	}

	public List<Object[]> countryByTime(Date begin, Date end, Integer siteId) {
		return dao.countryByTime(begin, end, siteId);
	}

	public List<Object[]> browserByTime(Date begin, Date end, Integer siteId) {
		return dao.browserByTime(begin, end, siteId);
	}

	public List<Object[]> osByTime(Date begin, Date end, Integer siteId) {
		return dao.osByTime(begin, end, siteId);
	}

	public List<Object[]> deviceByTime(Date begin, Date end, Integer siteId) {
		return dao.deviceByTime(begin, end, siteId);
	}

	public VisitLog get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public VisitLog save(String url, String referrer, String ip, String cookie, String userAgent, User user, Site site) {
		VisitLog bean = new VisitLog();
		bean.setUrl(url);
		bean.setReferrer(referrer);
		bean.setCookie(cookie);
		bean.setSite(site);
		bean.setUser(user);

		bean.setIp(ip);
		if (StringUtils.isNotBlank(ip)) {
			bean.setCountry(ipSeeker.getCountry(ip));
			bean.setArea(ipSeeker.getArea(ip));
		}

		bean.setUserAgent(userAgent);
		if (StringUtils.isNotBlank(userAgent)) {
			UserAgent ua = UserAgent.parseUserAgentString(userAgent);
			bean.setBrowser(ua.getBrowser().toString());
			bean.setOs(ua.getOperatingSystem().toString());
			bean.setDevice(ua.getOperatingSystem().getDeviceType().toString());
		}

		if (StringUtils.isNoneBlank(bean.getReferrer()) && StringUtils.isNoneBlank(bean.getUrl())) {
			try {
				URL accessURL = new URL(url);
				URL referrerURL = new URL(referrer);
				// url和referrer的域名不同，则代表来源不同网站，设置来源域名
				if (!StringUtils.equals(referrerURL.getHost(), accessURL.getHost())) {
					String source = referrerURL.getProtocol() + "://" + referrerURL.getHost();
					if (referrerURL.getPort() >= 0) {
						source += ":" + referrerURL.getPort();
					}
					bean.setSource(source);
				}
			} catch (MalformedURLException e) {
				logger.error("url: " + url + "; referrer: " + referrer, e);
			}
		}
		if (StringUtils.isBlank(bean.getSource())) {
			bean.setSource(VisitLog.SOURCE_DIRECT);
		}

		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public VisitLog delete(Integer id) {
		VisitLog bean = dao.findOne(id);
		dao.delete(bean);
		return bean;
	}

	@Transactional
	public List<VisitLog> delete(Integer[] ids) {
		List<VisitLog> beans = new ArrayList<VisitLog>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
		}
		return beans;
	}

	@Transactional
	public long deleteByDate(Date before, Integer siteId) {
		return dao.deleteByTimeAndSiteId(before, siteId);
	}

	@Override
	@Transactional
	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.deleteBySiteId(Arrays.asList(ids));
		}
	}

	@Override
	@Transactional
	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.updateUserToNull(Arrays.asList(ids));
		}
	}

	private IPSeeker ipSeeker;

	@Autowired
	public void setIpSeeker(IPSeeker ipSeeker) {
		this.ipSeeker = ipSeeker;
	}

	private VisitLogDao dao;

	@Autowired
	public void setDao(VisitLogDao dao) {
		this.dao = dao;
	}
}

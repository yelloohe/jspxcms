package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.VisitLog;
import com.jspxcms.ext.repository.plus.VisitLogDaoPlus;

public interface VisitLogDao extends Repository<VisitLog, Integer>,
		VisitLogDaoPlus {
	public Page<VisitLog> findAll(Specification<VisitLog> spec,
			Pageable pageable);

	public List<VisitLog> findAll(Specification<VisitLog> spec,
			Limitable limitable);

	public VisitLog findOne(Integer id);

	public VisitLog save(VisitLog bean);

	public void delete(VisitLog bean);

	// --------------------

	@Query("select SUBSTRING(bean.timeString,1,8) as _date,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by SUBSTRING(bean.timeString,1,8) order by SUBSTRING(bean.timeString,1,8)")
	public List<Object[]> trafficByDay(Date begin, Date end, Integer siteId);

	@Query("select SUBSTRING(bean.timeString,1,10) as _date,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by SUBSTRING(bean.timeString,1,10) order by SUBSTRING(bean.timeString,1,10)")
	public List<Object[]> trafficByHour(Date begin, Date end, Integer siteId);

	@Query("select SUBSTRING(bean.timeString,1,12) as _date,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by SUBSTRING(bean.timeString,1,12) order by SUBSTRING(bean.timeString,1,12)")
	public List<Object[]> trafficByMinute(Date begin, Date end, Integer siteId);

	@Query("select bean.source as _source,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by bean.source order by count(*) desc")
	public Page<Object[]> sourceByTime(Date begin, Date end, Integer siteId,
			Pageable pageable);

	@Query("select count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3")
	public List<Object[]> sourceCount(Date begin, Date end, Integer siteId);

	@Query("select bean.url as _url,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by bean.url order by count(bean.url) desc")
	public Page<Object[]> urlByTime(Date begin, Date end, Integer siteId,
			Pageable pageable);

	@Query("select bean.country as _contry,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by bean.country order by count(bean.country) desc")
	public List<Object[]> countryByTime(Date begin, Date end, Integer siteId);

	@Query("select bean.browser as _browser,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by bean.browser order by count(bean.browser) desc")
	public List<Object[]> browserByTime(Date begin, Date end, Integer siteId);

	@Query("select bean.os as _os,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by bean.os order by count(bean.os) desc")
	public List<Object[]> osByTime(Date begin, Date end, Integer siteId);

	@Query("select bean.device as _device,count(*) as _pv,count(distinct bean.cookie) as _uv,count(distinct bean.ip) as _ip from VisitLog bean where bean.time >= ?1 and bean.time < ?2 and bean.site.id = ?3 group by bean.device order by count(bean.device) desc")
	public List<Object[]> deviceByTime(Date begin, Date end, Integer siteId);

	@Modifying
	@Query("delete from VisitLog bean where bean.time < ?1 and bean.site.id = ?2")
	public int deleteByTimeAndSiteId(Date before, Integer siteId);

	@Modifying
	@Query("delete from VisitLog bean where bean.site.id in (?1)")
	public int deleteBySiteId(Collection<Integer> siteIds);

	@Modifying
	@Query("update VisitLog bean set bean.user.id = null where bean.user.id in (?1)")
	public int updateUserToNull(Collection<Integer> userIds);

}

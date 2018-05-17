package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.ScheduleJob;
import com.jspxcms.core.repository.plus.ScheduleJobDaoPlus;

public interface ScheduleJobDao extends Repository<ScheduleJob, Integer>,
		ScheduleJobDaoPlus {
	public List<ScheduleJob> findAll(Specification<ScheduleJob> spec, Sort sort);

	public List<ScheduleJob> findAll(Specification<ScheduleJob> spec,
			Limitable limit);

	public List<ScheduleJob> findAll();

	public ScheduleJob findOne(Integer id);

	public ScheduleJob save(ScheduleJob bean);

	public void delete(ScheduleJob bean);

	// --------------------

	@Query("select count(*) from ScheduleJob bean where bean.site.id in (?1)")
	public long countBySiteId(Collection<Integer> siteIds);

	@Query("select count(*) from ScheduleJob bean where bean.user.id in (?1)")
	public long countByUserId(Collection<Integer> userIds);

}

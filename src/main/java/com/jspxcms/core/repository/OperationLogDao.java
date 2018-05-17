package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.OperationLog;
import com.jspxcms.core.repository.plus.OperationLogDaoPlus;

public interface OperationLogDao extends Repository<OperationLog, Integer>,
		OperationLogDaoPlus {
	public Page<OperationLog> findAll(Specification<OperationLog> spec,
			Pageable pageable);

	public List<OperationLog> findAll(Specification<OperationLog> spec,
			Limitable limitable);

	public OperationLog findOne(Integer id);

	public OperationLog save(OperationLog bean);

	public void delete(OperationLog bean);

	// --------------------

	@Modifying
	@Query("delete from OperationLog bean where bean.site.id in (?1)")
	public int deleteBySiteId(Collection<Integer> siteIds);

	@Modifying
	@Query("delete from OperationLog bean where bean.user.id in (?1)")
	public int deleteByUserId(Collection<Integer> siteIds);
}

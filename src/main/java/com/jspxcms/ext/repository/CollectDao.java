package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Collect;
import com.jspxcms.ext.repository.plus.CollectDaoPlus;

public interface CollectDao extends Repository<Collect, Integer>,
		CollectDaoPlus {
	public Page<Collect> findAll(Specification<Collect> spec, Pageable pageable);

	public List<Collect> findAll(Specification<Collect> spec,
			Limitable limitable);

	public Collect findOne(Integer id);

	public Collect save(Collect bean);

	public void delete(Collect bean);

	// --------------------

	@Query("from Collect bean where bean.site.id=?1 and bean.status=0 order by bean.id asc")
	public List<Collect> findList(Integer siteId);

	@Query("select count(*) from Collect bean where bean.node.id in (?1)")
	public long countByNodeId(Collection<Integer> nodeIds);

	@Query("select count(*) from Collect bean where bean.user.id in (?1)")
	public long countByUserId(Collection<Integer> userIds);

	@Query("select count(*) from Collect bean where bean.site.id in (?1)")
	public long countBySiteId(Collection<Integer> siteIds);

}

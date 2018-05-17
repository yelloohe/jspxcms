package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Ad;
import com.jspxcms.ext.repository.plus.AdDaoPlus;

public interface AdDao extends Repository<Ad, Integer>, AdDaoPlus {
	public List<Ad> findAll(Specification<Ad> spec, Sort sort);

	public List<Ad> findAll(Specification<Ad> spec, Limitable limit);

	public Ad findOne(Integer id);

	public Ad save(Ad bean);

	public void delete(Ad bean);

	// --------------------

	@Query("select count(*) from Ad bean where bean.site.id in ?1")
	public long countBySiteId(Collection<Integer> siteIds);

	@Query("select count(*) from Ad bean where bean.slot.id in ?1")
	public long countBySlotId(Collection<Integer> slotIds);
}

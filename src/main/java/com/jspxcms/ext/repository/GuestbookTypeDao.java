package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.GuestbookType;
import com.jspxcms.ext.repository.plus.GuestbookTypeDaoPlus;

/**
 * GuestbookTypeDao
 * 
 * @author yangxing
 * 
 */
public interface GuestbookTypeDao extends Repository<GuestbookType, Integer>,
		GuestbookTypeDaoPlus {
	public List<GuestbookType> findAll(Specification<GuestbookType> spec,
			Sort sort);

	public List<GuestbookType> findAll(Specification<GuestbookType> spec,
			Limitable limitable);

	public GuestbookType findOne(Integer id);

	public GuestbookType save(GuestbookType bean);

	public void delete(GuestbookType bean);

	// --------------------

	public List<GuestbookType> findBySiteId(Integer siteId, Sort sort);

	@Query("select count(*) from GuestbookType bean where bean.site.id in ?1")
	public long countBySiteId(Collection<Integer> siteIds);

}

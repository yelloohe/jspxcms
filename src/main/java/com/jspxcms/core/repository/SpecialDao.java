package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Special;
import com.jspxcms.core.repository.plus.SpecialDaoPlus;

/**
 * SpecialDao
 * 
 * @author liufang
 * 
 */
public interface SpecialDao extends Repository<Special, Integer>,
		SpecialDaoPlus {
	public Page<Special> findAll(Specification<Special> spec, Pageable pageable);

	public List<Special> findAll(Specification<Special> spec,
			Limitable limitable);

	public Special findOne(Integer id);

	public Special save(Special bean);

	public void delete(Special bean);

	// --------------------

	@Query("select count(*) from Special bean where bean.site.id in (?1)")
	public long countBySiteId(Collection<Integer> siteIds);

	@Query("select count(*) from Special bean where bean.creator.id in (?1)")
	public long countByCreatorId(Collection<Integer> creatorIds);

	@Query("select count(*) from Special bean where bean.model.id in (?1)")
	public long countByModelId(Collection<Integer> categoryIds);

	@Query("select count(*) from Special bean where bean.category.id in (?1)")
	public long countByCategoryId(Collection<Integer> categoryIds);
}

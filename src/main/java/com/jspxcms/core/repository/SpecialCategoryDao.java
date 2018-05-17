package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.SpecialCategory;
import com.jspxcms.core.repository.plus.SpecialCategoryDaoPlus;

/**
 * SpecialCategoryDao
 * 
 * @author liufang
 * 
 */
public interface SpecialCategoryDao extends
		Repository<SpecialCategory, Integer>, SpecialCategoryDaoPlus {
	public List<SpecialCategory> findAll(Specification<SpecialCategory> spec,
			Sort sort);

	public List<SpecialCategory> findAll(Specification<SpecialCategory> spec,
			Limitable limit);

	public SpecialCategory findOne(Integer id);

	public SpecialCategory save(SpecialCategory bean);

	public void delete(SpecialCategory bean);

	// --------------------

	@Query("select count(*) from ScoreGroup bean where bean.site.id in ?1")
	public long countBySiteId(Collection<Integer> siteIds);
}

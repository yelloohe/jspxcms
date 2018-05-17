package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.ScoreGroup;
import com.jspxcms.core.repository.plus.ScoreGroupDaoPlus;

public interface ScoreGroupDao extends Repository<ScoreGroup, Integer>,
		ScoreGroupDaoPlus {
	public List<ScoreGroup> findAll(Specification<ScoreGroup> spec, Sort sort);

	public List<ScoreGroup> findAll(Specification<ScoreGroup> spec,
			Limitable limit);

	public ScoreGroup findOne(Integer id);

	public ScoreGroup save(ScoreGroup bean);

	public void delete(ScoreGroup bean);

	// --------------------

	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
	public List<ScoreGroup> findBySiteIdAndNumber(Integer siteId, String number);

	@Query("select count(*) from ScoreGroup bean where bean.number=?1 and bean.site.id=?2")
	public long countByNumber(String number, Integer siteId);

	@Query("select count(*) from ScoreGroup bean where bean.site.id in ?1")
	public long countBySiteId(Collection<Integer> siteIds);
}

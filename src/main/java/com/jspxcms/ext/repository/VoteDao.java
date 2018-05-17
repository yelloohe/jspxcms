package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Vote;
import com.jspxcms.ext.repository.plus.VoteDaoPlus;

public interface VoteDao extends Repository<Vote, Integer>, VoteDaoPlus {
	public Page<Vote> findAll(Specification<Vote> spec, Pageable pageable);

	public List<Vote> findAll(Specification<Vote> spec, Limitable limitable);

	public Vote findOne(Integer id);

	public Vote save(Vote bean);

	public void delete(Vote bean);

	// --------------------

	@Query("select count(*) from Vote bean where bean.number=?1 and bean.site.id=?2")
	public long countByNumber(String number, Integer siteId);

	@Query("select count(*) from Vote bean where bean.site.id in ?1")
	public long countBySiteId(Collection<Integer> siteIds);
}

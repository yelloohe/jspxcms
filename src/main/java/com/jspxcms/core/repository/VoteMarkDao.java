package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.VoteMark;
import com.jspxcms.core.repository.plus.VoteMarkDaoPlus;

public interface VoteMarkDao extends Repository<VoteMark, Integer>,
		VoteMarkDaoPlus {
	public List<VoteMark> findAll(Specification<VoteMark> spec, Sort sort);

	public List<VoteMark> findAll(Specification<VoteMark> spec, Limitable limit);

	public VoteMark findOne(Integer id);

	public VoteMark save(VoteMark bean);

	public void delete(VoteMark bean);

	// --------------------
	@Modifying
	@Query("delete from VoteMark bean where bean.ftype=?1 and bean.fid=?2")
	public int unmark(String ftype, Integer fid);
}

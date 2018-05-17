package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.ScoreBoard;

public interface ScoreBoardDao extends Repository<ScoreBoard, Integer> {
	public List<ScoreBoard> findAll(Specification<ScoreBoard> spec, Sort sort);

	public List<ScoreBoard> findAll(Specification<ScoreBoard> spec,
			Limitable limit);

	public ScoreBoard findOne(Integer id);

	public ScoreBoard save(ScoreBoard bean);

	public void delete(ScoreBoard bean);

	// --------------------

	public ScoreBoard findByFtypeAndFidAndItemId(String ftype, Integer fid,
			Integer itemId);

	public List<ScoreBoard> findByFtypeAndFid(String ftype, Integer fid);

	@Modifying
	@Query("delete from ScoreBoard bean where bean.item.id in ?1")
	public int deleteByItemId(Collection<Integer> itemIds);
}

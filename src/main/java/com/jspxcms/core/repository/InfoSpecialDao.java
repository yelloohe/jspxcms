package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoSpecial;
import com.jspxcms.core.domain.InfoSpecial.InfoSpecialId;

public interface InfoSpecialDao extends Repository<InfoSpecial, InfoSpecialId> {
	public InfoSpecial findOne(InfoSpecialId id);

	void delete(InfoSpecial entity);

	// --------------------
	/**
	 * 根据专题 ID查询
	 * 
	 * @param nodeId
	 * @return
	 */
	@Query("from Info bean join bean.infoSpecials infoSpecial where infoSpecial.special.id = ?1")
	public List<Info> findByInfoSpecialsSpecialId(Integer specialId);

	// @Modifying
	// @Query("delete from InfoSpecial bean where bean.special.id=?1")
	// public int deleteBySpecialId(Integer specialId);
}

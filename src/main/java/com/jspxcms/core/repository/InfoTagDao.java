package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoTag;
import com.jspxcms.core.domain.InfoTag.InfoTagId;

public interface InfoTagDao extends Repository<InfoTag, InfoTagId> {
	public InfoTag findOne(InfoTagId id);

	void delete(InfoTag entity);

	// --------------------
	/**
	 * 根据Tag ID查询
	 * 
	 * @param nodeId
	 * @return
	 */
	@Query("from Info bean join bean.infoTags infotag where infotag.tag.id = ?1")
	public List<Info> findByInfoTagsTagId(Integer tagId);

//	@Modifying
//	@Query("delete from InfoTag t where t.info.id=?1")
//	public int deleteByInfoId(Integer infoId);
//
//	@Modifying
//	@Query("delete from InfoTag t where t.tag.id=?1")
//	public int deleteByTagId(Integer tagId);
}

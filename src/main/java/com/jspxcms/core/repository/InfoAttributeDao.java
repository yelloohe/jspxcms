package com.jspxcms.core.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.core.domain.InfoAttribute;
import com.jspxcms.core.domain.InfoAttribute.InfoAttributeId;

/**
 * InfoAttributeDao
 * 
 * @author liufang
 * 
 */
public interface InfoAttributeDao extends Repository<InfoAttribute, InfoAttributeId> {
	public InfoAttribute findOne(InfoAttributeId id);

	// --------------------

	@Modifying
	@Query("delete from InfoAttribute t where t.attribute.id=?1")
	public int deleteByAttributeId(Integer attributeId);
}

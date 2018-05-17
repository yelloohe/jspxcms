package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.FriendlinkType;
import com.jspxcms.ext.repository.plus.FriendlinkTypeDaoPlus;

/**
 * FriendlinkTypeDao
 * 
 * @author yangxing
 * 
 */
public interface FriendlinkTypeDao extends Repository<FriendlinkType, Integer>,
		FriendlinkTypeDaoPlus {
	public List<FriendlinkType> findAll(Specification<FriendlinkType> spec,
			Sort sort);

	public List<FriendlinkType> findAll(Specification<FriendlinkType> spec,
			Limitable limitable);

	public FriendlinkType findOne(Integer id);

	public FriendlinkType save(FriendlinkType bean);

	public void delete(FriendlinkType bean);

	// --------------------

	public List<FriendlinkType> findBySiteId(Integer siteId, Sort sort);

	@Query("select count(*) from FriendlinkType bean where bean.site.id in ?1")
	public long countBySiteId(Collection<Integer> siteIds);
}

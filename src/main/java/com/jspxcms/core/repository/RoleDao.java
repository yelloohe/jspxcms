package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Role;

/**
 * RoleDao
 * 
 * @author liufang
 * 
 */
public interface RoleDao extends Repository<Role, Integer> {
	public List<Role> findAll(Specification<Role> spec, Sort sort);

	public List<Role> findAll(Specification<Role> spec, Limitable limit);

	public Role findOne(Integer id);

	public Role save(Role bean);

	public void delete(Role bean);

	// --------------------

	public List<Role> findBySiteIdIn(Collection<Integer> siteIds);

	@Query("from Role bean where bean.site.id=?1 order by bean.seq asc,bean.id asc")
	public List<Role> findBySiteId(Integer siteId);

	// @Query("select count(*) from Role bean where bean.site.id in (?1)")
	// public long countBySiteId(Collection<Integer> siteIds);
}

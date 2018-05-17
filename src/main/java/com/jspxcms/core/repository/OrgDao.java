package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Org;

/**
 * OrgDao
 * 
 * @author liufang
 * 
 */
public interface OrgDao extends Repository<Org, Integer> {
	public List<Org> findAll(Specification<Org> spec, Sort sort);

	public List<Org> findAll(Specification<Org> spec, Limitable limit);

	public List<Org> findAll(Sort sort);

	public Org findOne(Integer id);

	public Org save(Org bean);

	public void delete(Org bean);

	public List<Org> findByParentIdIsNull();

	// --------------------

	public List<Org> findByTreeNumberStartingWith(String treeNumber, Sort sort);

	@Query("select max(bean.treeNumber) from Org bean where bean.parent.id is null")
	public String findMaxRootTreeNumber();

	@Query("select count(*) from Org bean where bean.parent.id = ?1")
	public long countByParentId(Integer parentId);

	@Query("select count(*) from Org bean where bean.parent.id is null")
	public long countRoot();

	@Query("select bean.treeNumber from Org bean where bean.id = ?1")
	public String findTreeNumber(Integer id);

	@Modifying
	@Query("update Org bean set bean.treeNumber=CONCAT('*',bean.treeNumber) where bean.treeNumber like ?1")
	public int appendModifiedFlag(String treeNumberStart);

	@Modifying
	@Query("update Org bean set bean.treeLevel=(LENGTH(CONCAT(?2,SUBSTRING(bean.treeNumber,?3)))-4)/5, bean.treeNumber=CONCAT(?2,SUBSTRING(bean.treeNumber,?3)) where bean.treeNumber like ?1")
	public int updateTreeNumber(String treeNumber, String value, int len);

	@Modifying
	@Query("update Org bean set bean.parent.id = ?2 where bean.id = ?1")
	public int updateParentId(Integer id, Integer parentId);

	@Modifying
	@Query("update Org bean set bean.treeMax = ?2 where bean.id = ?1")
	public int updateTreeMax(Integer id, String treeMax);
}

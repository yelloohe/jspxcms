package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Workflow;

/**
 * WorkflowDao
 * 
 * @author liufang
 * 
 */
public interface WorkflowDao extends Repository<Workflow, Integer> {
	public List<Workflow> findAll(Specification<Workflow> spec, Sort sort);

	public List<Workflow> findAll(Specification<Workflow> spec, Limitable limit);

	public Workflow findOne(Integer id);

	public Workflow save(Workflow bean);

	public void delete(Workflow bean);

	// --------------------

	public List<Workflow> findBySiteId(Integer siteId, Sort sort);

	@Query("select count(*) from Workflow bean where bean.site.id in (?1)")
	public long countBySiteId(Collection<Integer> siteIds);

	@Query("select count(*) from Workflow bean where bean.group.id in (?1)")
	public long countByGroupId(Collection<Integer> groupIds);
}

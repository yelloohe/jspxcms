package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.WorkflowGroup;

/**
 * WorkflowGroupDao
 * 
 * @author liufang
 * 
 */
public interface WorkflowGroupDao extends Repository<WorkflowGroup, Integer> {
	public List<WorkflowGroup> findAll(Specification<WorkflowGroup> spec,
			Sort sort);

	public List<WorkflowGroup> findAll(Specification<WorkflowGroup> spec,
			Limitable limit);

	public WorkflowGroup findOne(Integer id);

	public WorkflowGroup save(WorkflowGroup bean);

	public void delete(WorkflowGroup bean);

	// --------------------

	public List<WorkflowGroup> findBySiteId(Integer siteId);

	@Query("select count(*) from WorkflowGroup bean where bean.site.id in ?1")
	public long countBySiteId(Collection<Integer> siteIds);
}

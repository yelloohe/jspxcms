package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Task;
import com.jspxcms.core.repository.plus.TaskDaoPlus;

public interface TaskDao extends Repository<Task, Integer>, TaskDaoPlus {
	public Page<Task> findAll(Specification<Task> spec, Pageable pageable);

	public List<Task> findAll(Specification<Task> spec, Limitable limitable);

	public Task findOne(Integer id);

	public Task save(Task bean);

	public void delete(Task bean);

	// --------------------

	@Modifying
	@Query("delete from Task bean where bean.user.id in (?1)")
	public int deleteByUserId(Collection<Integer> userIds);

	@Modifying
	@Query("delete from Task bean where bean.site.id in (?1)")
	public int deleteBySiteId(Collection<Integer> siteIds);
}

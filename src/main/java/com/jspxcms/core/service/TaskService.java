package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Task;

public interface TaskService {
	public Page<Task> findAll(Integer siteId, Map<String, String[]> params,
			Pageable pageable);

	public RowSide<Task> findSide(Integer siteId, Map<String, String[]> params,
			Task bean, Integer position, Sort sort);

	public Task get(Integer id);

	public boolean isRunning(Integer id);

	public void finish(Integer id);

	public void abort(Integer id);

	public Task stop(Integer id);

	public List<Task> stop(Integer[] ids);

	public void add(Integer id, int count);

	public Task save(String name, String description, Integer type,
			Integer userId, Integer siteId);

	public Task update(Task bean);

	public Task delete(Integer id);

	public List<Task> delete(Integer[] ids);
}

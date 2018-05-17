package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.Task;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.TaskDao;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.TaskService;
import com.jspxcms.core.service.UserService;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService, UserDeleteListener,
		SiteDeleteListener {
	public Page<Task> findAll(Integer siteId, Map<String, String[]> params,
			Pageable pageable) {
		return dao.findAll(spec(siteId, params), pageable);
	}

	public RowSide<Task> findSide(Integer siteId, Map<String, String[]> params,
			Task bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<Task>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Task> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Task> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Task> fsp = SearchFilter.spec(filters, Task.class);
		Specification<Task> sp = new Specification<Task>() {
			public Predicate toPredicate(Root<Task> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<Integer> siteIdPath = root.get("site").<Integer> get("id");
				Predicate siteIdPred = cb.equal(siteIdPath, siteId);
				return cb.and(fsp.toPredicate(root, query, cb), siteIdPred);
			}
		};
		return sp;
	}

	public Task get(Integer id) {
		return dao.findOne(id);
	}

	public boolean isRunning(Integer id) {
		// 不需要任务时，id可以为null
		if (id == null) {
			return true;
		}
		Task bean = dao.findOne(id);
		if (bean == null) {
			return false;
		}
		return bean.isRunning();
	}

	@Transactional
	public void finish(Integer id) {
		Task bean = dao.findOne(id);
		if (bean != null) {
			bean.finish();
		}
	}

	@Transactional
	public void abort(Integer id) {
		Task bean = dao.findOne(id);
		if (bean != null) {
			bean.abort();
		}
	}

	@Transactional
	public Task stop(Integer id) {
		Task bean = dao.findOne(id);
		bean.stop();
		return bean;
	}

	@Transactional
	public List<Task> stop(Integer[] ids) {
		List<Task> beans = new ArrayList<Task>(ids.length);
		for (Integer id : ids) {
			beans.add(stop(id));
		}
		return beans;
	}

	@Transactional
	public void add(Integer id, int count) {
		// 不需要任务时，id可以为null
		if (id == null) {
			return;
		}
		Task bean = dao.findOne(id);
		if (bean != null) {
			bean.add(count);
		}
	}

	@Transactional
	public Task save(String name, String description, Integer type,
			Integer userId, Integer siteId) {
		Task bean = new Task();
		bean.setName(name);
		bean.setType(type);
		bean.setDescription(description);

		Site site = siteService.get(siteId);
		bean.setSite(site);
		User user = userService.get(userId);
		bean.setUser(user);

		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Task update(Task bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Task delete(Integer id) {
		Task entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public List<Task> delete(Integer[] ids) {
		List<Task> beans = new ArrayList<Task>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
		}
		return beans;
	}

	@Override
	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.deleteByUserId(Arrays.asList(ids));
		}
	}

	@Override
	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.deleteBySiteId(Arrays.asList(ids));
		}
	}

	private UserService userService;
	private SiteService siteService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private TaskDao dao;

	@Autowired
	public void setDao(TaskDao dao) {
		this.dao = dao;
	}
}

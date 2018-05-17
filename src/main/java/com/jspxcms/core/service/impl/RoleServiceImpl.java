package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Role;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.listener.RoleDeleteListener;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.repository.RoleDao;
import com.jspxcms.core.service.NodeRoleService;
import com.jspxcms.core.service.RoleService;
import com.jspxcms.core.service.SiteService;

/**
 * RoleServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService, SiteDeleteListener {
	public List<Role> findList(Integer siteId, Map<String, String[]> params,
			Sort sort) {
		return dao.findAll(spec(siteId, params), sort);
	}

	public RowSide<Role> findSide(Integer siteId, Map<String, String[]> params,
			Role bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<Role>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Role> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Role> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Role> fsp = SearchFilter.spec(filters, Role.class);
		Specification<Role> sp = new Specification<Role>() {
			public Predicate toPredicate(Root<Role> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site")
							.<Integer> get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	public List<Role> findList(Integer siteId) {
		return dao.findBySiteId(siteId);
	}

	public Role get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Role save(Role bean, Integer[] infoPermIds, Integer[] nodePermIds,
			Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		nodeRoleService.update(bean, infoPermIds, nodePermIds);
		return bean;
	}

	@Transactional
	public Role update(Role bean, Integer[] infoPermIds, Integer[] nodePermIds) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		nodeRoleService.update(bean, infoPermIds, nodePermIds);
		return bean;
	}

	@Transactional
	public List<Role> batchUpdate(Integer[] id, String[] name, Integer[] rank,
			String[] description) {
		List<Role> beans = new ArrayList<Role>();
		if (ArrayUtils.isEmpty(id)) {
			return beans;
		}
		Role bean;
		for (int i = 0, len = id.length; i < len; i++) {
			bean = get(id[i]);
			bean.setName(name[i]);
			bean.setRank(rank[i]);
			bean.setDescription(description[i]);
			bean.setSeq(i);
			beans.add(bean);
		}
		return beans;
	}

	private Role doDelete(Integer id) {
		Role entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public Role delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public Role[] delete(Integer[] ids) {
		firePreDelete(ids);
		Role[] beans = new Role[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ids != null) {
			for (Role role : dao.findBySiteIdIn(Arrays.asList(ids))) {
				delete(role.getId());
			}
			// for (Integer id : ids) {
			// for (Role role : dao.findBySiteId(id)) {
			// delete(role.getId());
			// }
			// }
		}
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (RoleDeleteListener listener : deleteListeners) {
				listener.preRoleDelete(ids);
			}
		}
	}

	private List<RoleDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(List<RoleDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private NodeRoleService nodeRoleService;
	private SiteService siteService;

	@Autowired
	public void setNodeRoleService(NodeRoleService nodeRoleService) {
		this.nodeRoleService = nodeRoleService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private RoleDao dao;

	@Autowired
	public void setDao(RoleDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.listener.MemberGroupDeleteListener;
import com.jspxcms.core.repository.MemberGroupDao;
import com.jspxcms.core.service.MemberGroupService;
import com.jspxcms.core.service.NodeMemberGroupService;

/**
 * MemberGroupServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class MemberGroupServiceImpl implements MemberGroupService {
	public List<MemberGroup> findList(Map<String, String[]> params, Sort sort) {
		return dao.findAll(spec(params), sort);
	}

	public RowSide<MemberGroup> findSide(Map<String, String[]> params,
			MemberGroup bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<MemberGroup>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<MemberGroup> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<MemberGroup> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<MemberGroup> sp = SearchFilter.spec(filters,
				MemberGroup.class);
		return sp;
	}

	public List<MemberGroup> findList() {
		return dao.findAll(new Sort("seq", "id"));
	}

	public List<MemberGroup> findRealGroups() {
		return findList(new Integer[] { MemberGroup.GENERALS });
	}

	public List<MemberGroup> findList(Integer[] type) {
		if (ArrayUtils.isNotEmpty(type)) {
			return dao.findByTypeIn(Arrays.asList(type), new Sort("seq", "id"));
		} else {
			return dao.findAll(new Sort("seq", "id"));
		}
	}

	public MemberGroup get(Integer id) {
		return dao.findOne(id);
	}

	public MemberGroup getAnonymous() {
		return dao.findOne(0);
	}

	@Transactional
	public MemberGroup save(MemberGroup bean, Integer[] viewNodeIds,
			Integer[] contriNodeIds, Integer[] commentNodeIds, Integer siteId) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		nodeMemberGroupService.update(bean, viewNodeIds, contriNodeIds,
				commentNodeIds, siteId);
		return bean;
	}

	@Transactional
	public MemberGroup update(MemberGroup bean, Integer[] viewNodeIds,
			Integer[] contriNodeIds, Integer[] commentNodeIds, Integer siteId) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		nodeMemberGroupService.update(bean, viewNodeIds, contriNodeIds,
				commentNodeIds, siteId);
		return bean;
	}

	private MemberGroup doDelete(Integer id) {
		MemberGroup entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public MemberGroup delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public MemberGroup[] delete(Integer[] ids) {
		firePreDelete(ids);
		MemberGroup[] beans = new MemberGroup[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (MemberGroupDeleteListener listener : deleteListeners) {
				listener.preMemberGroupDelete(ids);
			}
		}
	}

	private List<MemberGroupDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(
			List<MemberGroupDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private NodeMemberGroupService nodeMemberGroupService;

	@Autowired
	public void setNodeMemberGroupService(
			NodeMemberGroupService nodeMemberGroupService) {
		this.nodeMemberGroupService = nodeMemberGroupService;
	}

	private MemberGroupDao dao;

	@Autowired
	public void setDao(MemberGroupDao dao) {
		this.dao = dao;
	}
}

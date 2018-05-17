package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
import com.jspxcms.core.domain.Tag;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.repository.TagDao;
import com.jspxcms.core.service.InfoTagService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.TagService;
import com.jspxcms.core.support.DeleteException;

/**
 * TagServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService, SiteDeleteListener {
	public Page<Tag> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(siteId, params), pageable);
	}

	public RowSide<Tag> findSide(Integer siteId, Map<String, String[]> params, Tag bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<Tag>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Tag> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Tag> spec(final Integer siteId, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Tag> fsp = SearchFilter.spec(filters, Tag.class);
		Specification<Tag> sp = new Specification<Tag>() {
			public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site").get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	public List<Tag> findList(Integer[] siteId, String[] node, Integer[] nodeId, Integer refers, Limitable limitable) {
		return dao.findList(siteId, node, nodeId, refers, limitable);
	}

	public Page<Tag> findPage(Integer[] siteId, String[] node, Integer[] nodeId, Integer refers, Pageable pageable) {
		return dao.findPage(siteId, node, nodeId, refers, pageable);
	}

	public List<Tag> findByName(String[] names, Integer[] siteIds) {
		return dao.findByName(names, siteIds);
	}

	public Tag findByName(Integer siteId, String name) {
		List<Tag> list = dao.findBySiteIdAndName(siteId, name);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public Tag get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Tag save(Tag bean, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Tag refer(String name, Integer siteId) {
		List<Tag> tags = dao.findBySiteIdAndName(siteId, name);
		Tag tag;
		if (tags.size() > 0) {
			tag = tags.get(0);
			tag.setRefers(tag.getRefers() + 1);
		} else {
			tag = new Tag();
			tag.setName(name);
			tag.setRefers(1);
			save(tag, siteId);
		}
		return tag;
	}

	@Transactional
	public List<Tag> refer(String[] names, Integer siteId) {
		if (ArrayUtils.isEmpty(names)) {
			return Collections.emptyList();
		}
		Set<String> tagSet = new HashSet<String>();
		List<Tag> tags = new ArrayList<Tag>(names.length);
		for (String name : names) {
			if (!tagSet.contains(name)) {
				tags.add(refer(name, siteId));
				tagSet.add(name);
			}
		}
		return tags;
	}

	@Transactional
	public void derefer(Tag tag) {
		tag.setRefers(tag.getRefers() - 1);
	}

	@Transactional
	public void derefer(Collection<Tag> tags) {
		for (Tag tag : tags) {
			derefer(tag);
		}
	}

	@Transactional
	public Tag update(Tag bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Tag delete(Integer id) {
		Tag entity = dao.findOne(id);
		infoTagService.deleteByTagId(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public Tag[] delete(Integer[] ids) {
		Tag[] beans = new Tag[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("tag.management");
			}
		}
	}

	private InfoTagService infoTagService;
	private SiteService siteService;

	@Autowired
	public void setInfoTagService(InfoTagService infoTagService) {
		this.infoTagService = infoTagService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private TagDao dao;

	@Autowired
	public void setDao(TagDao dao) {
		this.dao = dao;
	}
}

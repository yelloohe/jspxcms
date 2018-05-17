package com.jspxcms.ext.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.DeleteException;
import com.jspxcms.ext.domain.Friendlink;
import com.jspxcms.ext.domain.FriendlinkType;
import com.jspxcms.ext.listener.FriendlinkTypeDeleteListener;
import com.jspxcms.ext.repository.FriendlinkDao;
import com.jspxcms.ext.service.FriendlinkService;
import com.jspxcms.ext.service.FriendlinkTypeService;

/**
 * FriendlinkServiceImpl
 * 
 * @author yangxing
 * 
 */
@Service
@Transactional(readOnly = true)
public class FriendlinkServiceImpl implements FriendlinkService,
		FriendlinkTypeDeleteListener, SiteDeleteListener {

	public List<Friendlink> findList(Integer siteId,
			Map<String, String[]> params) {
		return dao.findAll(spec(siteId, params), new Sort("seq", "id"));
	}

	public RowSide<Friendlink> findSide(Integer siteId,
			Map<String, String[]> params, Friendlink bean, Integer position) {
		if (position == null) {
			return new RowSide<Friendlink>();
		}
		Sort sort = new Sort("seq", "id");
		Limitable limit = RowSide.limitable(position, sort);
		List<Friendlink> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Friendlink> spec(final Integer siteId,
			Map<String, String[]> params) {

		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Friendlink> fs = SearchFilter.spec(filters,
				Friendlink.class);
		Specification<Friendlink> sp = new Specification<Friendlink>() {
			public Predicate toPredicate(Root<Friendlink> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fs.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred,
							cb.equal(root.get("site").get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	public List<Friendlink> findList(Integer[] siteId, String[] type,
			Integer[] typeId, Boolean isWithLogo, Boolean isRecommend,
			Integer[] status, Limitable limitable) {
		return dao.findList(siteId, type, typeId, isWithLogo, isRecommend,
				status, limitable);
	}

	public Friendlink get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Friendlink save(Friendlink bean, Integer typeId, Integer siteId) {
		FriendlinkType type = friendlinkTypeService.get(typeId);
		bean.setType(type);
		Site site = siteService.get(siteId);
		bean.setSite(site);
		if (StringUtils.isNotBlank(bean.getLogo())) {
			bean.setWithLogo(true);
		} else {
			bean.setWithLogo(false);
		}
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Friendlink update(Friendlink bean, Integer typeId) {
		if (typeId != null) {
			FriendlinkType type = friendlinkTypeService.get(typeId);
			bean.setType(type);
		}
		if (StringUtils.isNotBlank(bean.getLogo())) {
			bean.setWithLogo(true);
		} else {
			bean.setWithLogo(false);
		}
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Friendlink[] batchUpdate(Integer[] id) {
		Friendlink[] beans = new Friendlink[id.length];
		for (int i = 0, len = id.length; i < len; i++) {
			beans[i] = get(id[i]);
			beans[i].setSeq(i);
			beans[i] = dao.save(beans[i]);
		}
		return beans;
	}

	@Transactional
	public Friendlink delete(Integer id) {
		Friendlink entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public Friendlink[] delete(Integer[] ids) {
		Friendlink[] beans = new Friendlink[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("friendlink.management");
			}
		}
	}

	public void preFriendlinkTypeDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countByTypeId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("friendlink.management");
			}
		}
	}

	private SiteService siteService;
	private FriendlinkTypeService friendlinkTypeService;

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	@Autowired
	public void setFriendlinkTypeService(
			FriendlinkTypeService friendlinkTypeService) {
		this.friendlinkTypeService = friendlinkTypeService;
	}

	private FriendlinkDao dao;

	@Autowired
	public void setDao(FriendlinkDao dao) {
		this.dao = dao;
	}
}

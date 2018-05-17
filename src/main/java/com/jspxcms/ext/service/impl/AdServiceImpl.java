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
import com.jspxcms.core.service.AttachmentRefService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.DeleteException;
import com.jspxcms.ext.domain.Ad;
import com.jspxcms.ext.domain.AdSlot;
import com.jspxcms.ext.listener.AdSlotDeleteListener;
import com.jspxcms.ext.repository.AdDao;
import com.jspxcms.ext.service.AdService;
import com.jspxcms.ext.service.AdSlotService;

@Service
@Transactional(readOnly = true)
public class AdServiceImpl implements AdService, SiteDeleteListener,
		AdSlotDeleteListener {
	public List<Ad> findList(Integer siteId, Map<String, String[]> params,
			Sort sort) {
		return dao.findAll(spec(siteId, params), sort);
	}

	public RowSide<Ad> findSide(Integer siteId, Map<String, String[]> params,
			Ad bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<Ad>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Ad> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Ad> spec(final Integer siteId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Ad> fsp = SearchFilter.spec(filters, Ad.class);
		Specification<Ad> sp = new Specification<Ad>() {
			public Predicate toPredicate(Root<Ad> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
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

	public List<Ad> findList(Integer[] siteId, String[] slot, Integer[] slotId,
			Limitable limitable) {
		return dao.findList(siteId, slot, slotId, limitable);
	}

	public Ad get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Ad save(Ad bean, Integer slotId, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		AdSlot slot = slotService.get(slotId);
		slot.addAd(bean);
		bean.setSlot(slot);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		attachmentRefService.update(bean.getAttachUrls(), Ad.ATTACH_TYPE,
				bean.getId());
		return bean;
	}

	@Transactional
	public Ad update(Ad bean, Integer slotId) {
		bean.getSlot().removeAd(bean);
		AdSlot slot = slotService.get(slotId);
		bean.setSlot(slot);
		bean.getSlot().addAd(bean);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		attachmentRefService.update(bean.getAttachUrls(), Ad.ATTACH_TYPE,
				bean.getId());
		return bean;
	}

	@Transactional
	public Ad delete(Integer id) {
		Ad entity = dao.findOne(id);
		if (entity != null) {
			entity.getSlot().removeAd(entity);
			dao.delete(entity);
			attachmentRefService.delete(Ad.ATTACH_TYPE, entity.getId());
		}
		return entity;
	}

	@Transactional
	public Ad[] delete(Integer[] ids) {
		Ad[] beans = new Ad[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("ad.management");
			}
		}
	}

	public void preAdSlotDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySlotId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("ad.management");
			}
		}
	}

	private AdSlotService slotService;
	private SiteService siteService;
	private AttachmentRefService attachmentRefService;

	@Autowired
	public void setSlotService(AdSlotService slotService) {
		this.slotService = slotService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	@Autowired
	public void setAttachmentRefService(
			AttachmentRefService attachmentRefService) {
		this.attachmentRefService = attachmentRefService;
	}

	private AdDao dao;

	@Autowired
	public void setDao(AdDao dao) {
		this.dao = dao;
	}
}

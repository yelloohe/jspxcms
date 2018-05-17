package com.jspxcms.ext.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.DeleteException;
import com.jspxcms.ext.domain.GuestbookType;
import com.jspxcms.ext.listener.GuestbookTypeDeleteListener;
import com.jspxcms.ext.repository.GuestbookTypeDao;
import com.jspxcms.ext.service.GuestbookTypeService;

/**
 * GuestbookTypeServiceImpl
 * 
 * @author yangxing
 * 
 */
@Service
@Transactional(readOnly = true)
public class GuestbookTypeServiceImpl implements GuestbookTypeService,
		SiteDeleteListener {

	public List<GuestbookType> findList(Integer siteId) {
		return dao.findBySiteId(siteId, new Sort("seq", "id"));
	}

	public List<GuestbookType> findList(Integer[] siteId, Limitable limitable) {
		return dao.getList(siteId, limitable);
	}

	public GuestbookType get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public GuestbookType save(GuestbookType bean, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		dao.save(bean);
		return bean;
	}

	@Transactional
	public GuestbookType update(GuestbookType bean) {
		return dao.save(bean);
	}

	@Transactional
	public GuestbookType[] batchUpdate(Integer[] id, String[] name,
			String[] number, String[] description) {
		GuestbookType[] beans = new GuestbookType[id.length];
		for (int i = 0, len = id.length; i < len; i++) {
			beans[i] = get(id[i]);
			beans[i].setSeq(i);
			beans[i].setName(name[i]);
			beans[i].setNumber(number[i]);
			beans[i].setDescription(description[i]);
			update(beans[i]);
		}
		return beans;
	}

	private GuestbookType doDelete(Integer id) {
		GuestbookType entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public GuestbookType delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public GuestbookType[] delete(Integer[] ids) {
		firePreDelete(ids);
		GuestbookType[] beans = new GuestbookType[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("guestbookType.management");
			}
		}
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (GuestbookTypeDeleteListener listener : deleteListeners) {
				listener.preGuestbookTypeDelete(ids);
			}
		}
	}

	private List<GuestbookTypeDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(
			List<GuestbookTypeDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private GuestbookTypeDao dao;
	private SiteService siteService;

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	@Autowired
	public void setDao(GuestbookTypeDao dao) {
		this.dao = dao;
	}
}

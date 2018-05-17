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
import com.jspxcms.ext.domain.FriendlinkType;
import com.jspxcms.ext.listener.FriendlinkTypeDeleteListener;
import com.jspxcms.ext.repository.FriendlinkTypeDao;
import com.jspxcms.ext.service.FriendlinkTypeService;

/**
 * FriendlinkTypeServiceImpl
 * 
 * @author yangxing
 * 
 */
@Service
@Transactional(readOnly = true)
public class FriendlinkTypeServiceImpl implements FriendlinkTypeService,
		SiteDeleteListener {
	public List<FriendlinkType> findList(Integer siteId) {
		return dao.findBySiteId(siteId, new Sort("seq", "id"));
	}

	public List<FriendlinkType> findList(Integer[] siteId, Limitable limitable) {
		return dao.getList(siteId, limitable);
	}

	public FriendlinkType get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public FriendlinkType save(FriendlinkType bean, Integer siteId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		dao.save(bean);
		return bean;
	}

	@Transactional
	public FriendlinkType update(FriendlinkType bean) {
		bean.applyDefaultValue();
		dao.save(bean);
		return bean;
	}

	@Transactional
	public FriendlinkType[] batchUpdate(Integer[] id, String[] name,
			String[] number) {
		FriendlinkType[] beans = new FriendlinkType[id.length];
		for (int i = 0, len = id.length; i < len; i++) {
			beans[i] = get(id[i]);
			beans[i].setSeq(i);
			beans[i].setName(name[i]);
			beans[i].setNumber(number[i]);
			update(beans[i]);
		}
		return beans;
	}

	private FriendlinkType doDelete(Integer id) {
		FriendlinkType entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public FriendlinkType delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public FriendlinkType[] delete(Integer[] ids) {
		firePreDelete(ids);
		FriendlinkType[] beans = new FriendlinkType[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
				throw new DeleteException("friendlinkType.management");
			}
		}
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (FriendlinkTypeDeleteListener listener : deleteListeners) {
				listener.preFriendlinkTypeDelete(ids);
			}
		}
	}

	private List<FriendlinkTypeDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(
			List<FriendlinkTypeDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private FriendlinkTypeDao dao;
	private SiteService siteService;

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	@Autowired
	public void setDao(FriendlinkTypeDao dao) {
		this.dao = dao;
	}
}

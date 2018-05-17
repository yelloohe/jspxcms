package com.jspxcms.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.repository.SiteDao;
import com.jspxcms.core.service.SiteShiroService;

/**
 * SiteServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class SiteShiroServiceImpl implements SiteShiroService {
	public List<Site> findByUserId(Integer userId) {
		return dao.findByUserId(userId);
	}

	public Site findByDomain(String domain) {
		List<Site> list = dao.findByDomain(domain);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public Site get(Integer id) {
		Site entity = dao.findOne(id);
		return entity;
	}

	private SiteDao dao;

	@Autowired
	public void setSiteDao(SiteDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service;

import java.util.List;

import com.jspxcms.core.domain.Site;

/**
 * SiteService
 * 
 * @author liufang
 * 
 */
public interface SiteShiroService {
	public List<Site> findByUserId(Integer userId);

	public Site findByDomain(String domain);

	public Site get(Integer id);
}

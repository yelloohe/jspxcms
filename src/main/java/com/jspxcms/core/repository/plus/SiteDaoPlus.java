package com.jspxcms.core.repository.plus;

import java.util.List;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Site;

/**
 * SiteDaoPlus
 * 
 * @author liufang
 * 
 */
public interface SiteDaoPlus {
	public List<Site> findByStatus(Integer parentId, String parentNumber, Integer[] status, Limitable limitable);
}

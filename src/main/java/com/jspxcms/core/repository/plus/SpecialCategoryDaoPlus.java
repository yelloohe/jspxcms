package com.jspxcms.core.repository.plus;

import java.util.List;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.SpecialCategory;

/**
 * SpecialCategoryDaoPlus
 * 
 * @author liufang
 * 
 */
public interface SpecialCategoryDaoPlus {
	public List<SpecialCategory> getList(Integer[] siteId, Limitable limitable);
}

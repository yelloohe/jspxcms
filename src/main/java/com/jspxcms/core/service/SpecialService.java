package com.jspxcms.core.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Special;
import com.jspxcms.core.domain.SpecialFile;
import com.jspxcms.core.domain.SpecialImage;

/**
 * SpecialService
 * 
 * @author liufang
 * 
 */
public interface SpecialService {
	public Page<Special> findAll(Integer siteId, Map<String, String[]> params,
			Pageable pageable);

	public RowSide<Special> findSide(Integer siteId,
			Map<String, String[]> params, Special bean, Integer position,
			Sort sort);

	public List<Special> findList(Integer[] siteId, Integer[] categoryId,
			Date beginDate, Date endDate, Boolean isWithImage,
			Boolean isRecommend, Limitable limitable);

	public Page<Special> findPage(Integer[] siteId, Integer[] categoryId,
			Date beginDate, Date endDate, Boolean isWithImage,
			Boolean isRecommend, Pageable pageable);

	public Special get(Integer id);

	public Special save(Special bean, Integer categoryId, Integer modelId,
			Integer creatorId, Map<String, String> customs,
			Map<String, String> clobs, List<SpecialImage> images,
			List<SpecialFile> files, Integer siteId);

	public Special update(Special bean, Integer categoryId, Integer modelId,
			Map<String, String> customs, Map<String, String> clobs,
			List<SpecialImage> images, List<SpecialFile> files);

	public Special delete(Integer id);

	public Special[] delete(Integer[] ids);

	public int updateViews(Integer id);

	public Special refer(Integer beanId);

	public List<Special> refer(Integer[] beanIds);

	public void derefer(Special bean);

	public void derefer(Collection<Special> beans);
}

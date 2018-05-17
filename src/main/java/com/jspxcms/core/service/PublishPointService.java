package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.PublishPoint;

public interface PublishPointService {
	public List<PublishPoint> findList(Map<String, String[]> params, Sort sort);

	public RowSide<PublishPoint> findSide(Map<String, String[]> params,
			PublishPoint bean, Integer position, Sort sort);

	public List<PublishPoint> findByType(Integer type);

	public PublishPoint get(Integer id);

	public PublishPoint save(PublishPoint bean);

	public PublishPoint update(PublishPoint bean);

	public PublishPoint delete(Integer id);

	public PublishPoint[] delete(Integer[] ids);
}

package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.repository.plus.PublishPointDaoPlus;

public interface PublishPointDao extends Repository<PublishPoint, Integer>,
		PublishPointDaoPlus {
	public List<PublishPoint> findAll(Specification<PublishPoint> spec,
			Sort sort);

	public List<PublishPoint> findAll(Specification<PublishPoint> spec,
			Limitable limit);

	public PublishPoint findOne(Integer id);

	public PublishPoint save(PublishPoint bean);

	public void delete(PublishPoint bean);

	// --------------------

	public List<PublishPoint> findByType(Integer type, Sort sort);
}

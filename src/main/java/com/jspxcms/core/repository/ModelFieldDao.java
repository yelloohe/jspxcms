package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.ModelField;

/**
 * ModelFieldDao
 * 
 * @author liufang
 * 
 */
public interface ModelFieldDao extends Repository<ModelField, Integer> {
	public List<ModelField> findAll(Specification<ModelField> spec, Sort sort);

	public List<ModelField> findAll(Specification<ModelField> spec,
			Limitable limit);

	public ModelField findOne(Integer id);

	public ModelField save(ModelField bean);

	public void delete(ModelField bean);

	// --------------------

}

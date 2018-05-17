package com.jspxcms.ext.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.repository.plus.QuestionItemDaoPlus;

public interface QuestionItemDao extends Repository<QuestionItem, Integer>, QuestionItemDaoPlus {
	public Page<QuestionItem> findAll(Specification<QuestionItem> spec, Pageable pageable);

	public List<QuestionItem> findAll(Specification<QuestionItem> spec, Limitable limitable);

	public QuestionItem findOne(Integer id);

	public QuestionItem save(QuestionItem bean);

	public void delete(QuestionItem bean);

	// --------------------

}

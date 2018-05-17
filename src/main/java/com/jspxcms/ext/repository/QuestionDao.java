package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.repository.plus.QuestionDaoPlus;

public interface QuestionDao extends Repository<Question, Integer>, QuestionDaoPlus {
	public Page<Question> findAll(Specification<Question> spec, Pageable pageable);

	public List<Question> findAll(Specification<Question> spec, Limitable limitable);

	public Question findOne(Integer id);

	public Question save(Question bean);

	public void delete(Question bean);

	// --------------------

	@Query("select count(*) from Question bean where bean.site.id in (?1)")
	public long countBySiteId(Collection<Integer> siteIds);
}

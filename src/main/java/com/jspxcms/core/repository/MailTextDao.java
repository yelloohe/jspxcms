package com.jspxcms.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.MailText;

public interface MailTextDao extends Repository<MailText, Integer> {
	public Page<MailText> findAll(Specification<MailText> spec, Pageable pageable);

	public List<MailText> findAll(Specification<MailText> spec, Limitable limitable);

	public MailText findOne(Integer id);

	public MailText save(MailText bean);

	public void delete(MailText bean);

	// --------------------

}

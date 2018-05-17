package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.MailOutbox;
import com.jspxcms.core.repository.plus.MailOutboxDaoPlus;

public interface MailOutboxDao extends Repository<MailOutbox, Integer>, MailOutboxDaoPlus {
	public Page<MailOutbox> findAll(Specification<MailOutbox> spec, Pageable pageable);

	public List<MailOutbox> findAll(Specification<MailOutbox> spec, Limitable limitable);

	public MailOutbox findOne(Integer id);

	public MailOutbox save(MailOutbox bean);

	public void delete(MailOutbox bean);
	
	// --------------------
	
	public List<MailOutbox> findBySenderIdIn(Collection<Integer> userIds);
}

package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.MailInbox;
import com.jspxcms.core.repository.plus.MailInboxDaoPlus;

public interface MailInboxDao extends Repository<MailInbox, Integer>, MailInboxDaoPlus {
	public Page<MailInbox> findAll(Specification<MailInbox> spec, Pageable pageable);

	public List<MailInbox> findAll(Specification<MailInbox> spec, Limitable limitable);

	public MailInbox findOne(Integer id);

	public MailInbox save(MailInbox bean);

	public void delete(MailInbox bean);

	// --------------------

	public List<MailInbox> findByReceiverIdIn(Collection<Integer> userIds);
}

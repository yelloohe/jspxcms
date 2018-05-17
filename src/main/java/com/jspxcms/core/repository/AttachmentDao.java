package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Attachment;
import com.jspxcms.core.repository.plus.AttachmentDaoPlus;

public interface AttachmentDao extends Repository<Attachment, Integer>,
		AttachmentDaoPlus {
	public Page<Attachment> findAll(Specification<Attachment> spec,
			Pageable pageable);

	public List<Attachment> findAll(Specification<Attachment> spec,
			Limitable limitable);

	public Attachment findOne(Integer id);

	public Attachment save(Attachment bean);

	public void delete(Attachment bean);

	// --------------------

	public List<Attachment> findByName(String name);

	@Modifying
	@Query("delete from Attachment bean where bean.site.id in (?1)")
	public int deleteBySiteId(Collection<Integer> siteIds);

	@Modifying
	@Query("update Attachment bean set bean.user.id = 0 where bean.user.id in (?1)")
	public int toAnonymous(Collection<Integer> userIds);
}

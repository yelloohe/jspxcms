package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.AttachmentRef;
import com.jspxcms.core.repository.plus.AttachmentRefDaoPlus;

public interface AttachmentRefDao extends Repository<AttachmentRef, Integer>,
		AttachmentRefDaoPlus {
	public List<AttachmentRef> findAll(Specification<AttachmentRef> spec,
			Sort sort);

	public List<AttachmentRef> findAll(Specification<AttachmentRef> spec,
			Limitable limit);

	public AttachmentRef findOne(Integer id);

	public AttachmentRef save(AttachmentRef bean);

	public void delete(AttachmentRef bean);

	public void delete(Iterable<AttachmentRef> beans);

	// --------------------

	public List<AttachmentRef> findByFtypeAndFid(String ftype, Integer fid);

	@Modifying
	@Query("delete from AttachmentRef bean where bean.site.id in (?1)")
	public int deleteBySiteId(Collection<Integer> siteIds);
}

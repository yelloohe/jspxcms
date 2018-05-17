package com.jspxcms.plug.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.plug.domain.Resume;

public interface ResumeDao extends Repository<Resume, Integer>, ResumeDaoPlus {
	public Page<Resume> findAll(Specification<Resume> spec, Pageable pageable);

	public List<Resume> findAll(Specification<Resume> spec, Limitable limitable);

	public Resume findOne(Integer id);

	public Resume save(Resume bean);

	public void delete(Resume bean);
	
	// --------------------

	@Modifying
	@Query("delete from Resume bean where bean.site.id in (?1)")
	public int deleteBySiteId(Collection<Integer> siteIds);
}

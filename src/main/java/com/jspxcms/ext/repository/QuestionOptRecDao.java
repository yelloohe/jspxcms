package com.jspxcms.ext.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.ext.domain.QuestionOptRec;
import com.jspxcms.ext.repository.plus.QuestionOptRecDaoPlus;

public interface QuestionOptRecDao extends Repository<QuestionOptRec, Integer>, QuestionOptRecDaoPlus {
	
	// --------------------

	@Modifying
	@Query("delete from QuestionOptRec bean where bean.option.id in (?1)")
	public int deleteBySiteId(Collection<Integer> optionIds);
}

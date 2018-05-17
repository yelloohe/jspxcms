package com.jspxcms.ext.repository.plus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Question;

public interface QuestionDaoPlus {
	public List<Question> findList(Integer userId, Integer historyUserId, Boolean inPeriod, Integer[] status,
			Integer[] siteId, Limitable limitable);

	public Page<Question> findPage(Integer userId, Integer historyUserId, Boolean inPeriod, Integer[] status,
			Integer[] siteId, Pageable pageable);

	/**
	 * 查询排列最前的问卷
	 * 
	 * @param siteId
	 * @return
	 */
	public Question findLatest(Integer[] status, Integer siteId);
}

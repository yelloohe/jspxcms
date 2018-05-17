package com.jspxcms.ext.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.ext.domain.Question;

public interface QuestionService {
	public Page<Question> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable);

	public RowSide<Question> findSide(Integer siteId, Map<String, String[]> params, Question bean, Integer position,
			Sort sort);

	public List<Question> findList(Integer userId, Integer historyUserId, Boolean inPeriod, Integer[] status,
			Integer[] siteId, Limitable limitable);

	public Page<Question> findPage(Integer userId, Integer historyUserId, Boolean inPeriod, Integer[] status,
			Integer[] siteId, Pageable pageable);

	public Question findLatest(Integer[] status, Integer siteId);

	public Question get(Integer id);

	public Question save(Question bean, String[] itemTitle, Boolean[] itemInput, Integer siteId);

	public Question update(Question bean, Integer[] itemId, String[] itemTitle, Boolean[] itemInput);

	public Question delete(Integer id);

	public Question[] delete(Integer[] ids);

	public void answer(Integer id, Integer userId, String ip, String cookie, Integer[] optionIds,
			Map<Integer, String> inputs);
}

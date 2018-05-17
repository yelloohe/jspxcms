package com.jspxcms.ext.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.domain.QuestionItem;

public interface QuestionItemService {
	public RowSide<QuestionItem> findSide(Map<String, String[]> params,
			QuestionItem bean, Integer position, Sort sort);

	public QuestionItem get(Integer id);

	public List<QuestionItem> save(String[] title, Boolean[] essay,
			Question question);

	public List<QuestionItem> update(Integer[] id, String[] title,
			Boolean[] essay, Question question);

	public QuestionItem update(QuestionItem bean, Integer[] optionId,
			String[] optionTitle);

	public QuestionItem delete(QuestionItem entity);
}

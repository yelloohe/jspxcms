package com.jspxcms.ext.service;

import java.util.List;

import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.domain.QuestionOption;

public interface QuestionOptionService {
	public QuestionOption get(Integer id);

	public List<QuestionOption> save(String[] title, QuestionItem item);

	public List<QuestionOption> update(Integer[] id, String[] title,
			QuestionItem item);

}

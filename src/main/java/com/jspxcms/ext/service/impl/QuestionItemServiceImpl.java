package com.jspxcms.ext.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.repository.QuestionItemDao;
import com.jspxcms.ext.service.QuestionItemService;
import com.jspxcms.ext.service.QuestionOptionService;

@Service
@Transactional(readOnly = true)
public class QuestionItemServiceImpl implements QuestionItemService {

	public RowSide<QuestionItem> findSide(Map<String, String[]> params,
			QuestionItem bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<QuestionItem>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<QuestionItem> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<QuestionItem> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<QuestionItem> sp = SearchFilter.spec(filters,
				QuestionItem.class);
		return sp;
	}

	public QuestionItem get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public List<QuestionItem> save(String[] title, Boolean[] essay,
			Question question) {
		int len = title != null ? title.length : 0;
		List<QuestionItem> list = new ArrayList<QuestionItem>(len);
		QuestionItem bean;
		for (int i = 0; i < len; i++) {
			bean = new QuestionItem();
			bean.setQuestion(question);
			bean.setTitle(title[i]);
			bean.setEssay(essay[i]);
			bean.setSeq(i);
			bean.applyDefaultValue();
			dao.save(bean);
			list.add(bean);
		}
		question.setItems(list);
		return list;
	}

	@Transactional
	public List<QuestionItem> update(Integer[] id, String[] title,
			Boolean[] essay, Question question) {
		int len = id != null ? id.length : 0;
		List<QuestionItem> list = new ArrayList<QuestionItem>(len);
		QuestionItem bean;
		// 修改和新增
		for (int i = 0; i < len; i++) {
			if (id[i] != null) {
				bean = dao.findOne(id[i]);
			} else {
				bean = new QuestionItem();
			}
			bean.setQuestion(question);
			bean.setTitle(title[i]);
			bean.setEssay(essay[i]);
			bean.setSeq(i);
			bean.applyDefaultValue();
			dao.save(bean);
			list.add(bean);
		}
		// 删除
		for (QuestionItem item : question.getItems()) {
			if (!ArrayUtils.contains(id, item.getId())) {
				delete(item);
			}
		}
		question.setItems(list);
		return list;
	}

	@Transactional
	public QuestionItem update(QuestionItem bean, Integer[] optionId,
			String[] optionTitle) {
		bean.applyDefaultValue();
		bean = dao.save(bean);

		optionService.update(optionId, optionTitle, bean);
		return bean;
	}

	@Transactional
	public QuestionItem delete(QuestionItem entity) {
		dao.delete(entity);
		return entity;
	}

	private QuestionOptionService optionService;

	@Autowired
	public void setOptionService(QuestionOptionService optionService) {
		this.optionService = optionService;
	}

	private QuestionItemDao dao;

	@Autowired
	public void setDao(QuestionItemDao dao) {
		this.dao = dao;
	}
}

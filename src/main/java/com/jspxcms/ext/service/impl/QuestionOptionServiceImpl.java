package com.jspxcms.ext.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.ext.domain.QuestionItem;
import com.jspxcms.ext.domain.QuestionOption;
import com.jspxcms.ext.repository.QuestionOptionDao;
import com.jspxcms.ext.service.QuestionOptionService;

@Service
@Transactional(readOnly = true)
public class QuestionOptionServiceImpl implements QuestionOptionService {

	public QuestionOption get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public List<QuestionOption> save(String[] title, QuestionItem item) {
		int len = title != null ? title.length : 0;
		List<QuestionOption> list = new ArrayList<QuestionOption>(len);
		QuestionOption bean;
		for (int i = 0; i < len; i++) {
			bean = new QuestionOption();
			bean.setItem(item);
			bean.setTitle(title[i]);
			bean.setSeq(i);
			bean.applyDefaultValue();
			dao.save(bean);
			list.add(bean);
		}
		item.setOptions(list);
		return list;
	}

	@Transactional
	public List<QuestionOption> update(Integer[] id, String[] title,
			QuestionItem item) {
		int len = id != null ? id.length : 0;
		List<QuestionOption> list = new ArrayList<QuestionOption>(len);
		QuestionOption bean;
		// 修改和新增
		for (int i = 0; i < len; i++) {
			if (id[i] != null) {
				bean = dao.findOne(id[i]);
			} else {
				bean = new QuestionOption();
			}
			bean.setItem(item);
			bean.setTitle(title[i]);
			bean.setSeq(i);
			bean.applyDefaultValue();
			dao.save(bean);
			list.add(bean);
		}
		// 删除
		for (QuestionOption option : item.getOptions()) {
			if (!ArrayUtils.contains(id, option.getId())) {
				delete(option);
			}
		}
		item.setOptions(list);
		return list;
	}

	@Transactional
	public QuestionOption delete(QuestionOption entity) {
		dao.delete(entity);
		return entity;
	}

	private QuestionOptionDao dao;

	@Autowired
	public void setDao(QuestionOptionDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.ext.service.impl;

import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.domain.QuestionRecord;
import com.jspxcms.ext.repository.QuestionRecordDao;
import com.jspxcms.ext.service.QuestionRecordService;

@Service
@Transactional(readOnly = true)
public class QuestionRecordServiceImpl implements QuestionRecordService, UserDeleteListener {

	public long countByUserId(Integer questionId, Integer userId, Integer interval) {
		if (interval != null && interval > 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -interval);
			return dao.countByUserId(questionId, userId, cal.getTime());
		} else {
			return dao.countByUserId(questionId, userId);
		}
	}

	public long countByIp(Integer questionId, String ip, String cookie, Integer interval) {
		if (interval != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -interval);
			return dao.countByIp(questionId, ip, cookie, cal.getTime());
		} else {
			return dao.countByIp(questionId, ip, cookie);
		}
	}

	public long countByCookie(Integer questionId, String cookie, Integer interval) {
		if (interval != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -interval);
			return dao.countByCookie(questionId, cookie, cal.getTime());
		} else {
			return dao.countByCookie(questionId, cookie);
		}
	}

	public QuestionRecord get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public QuestionRecord save(Question question, User user, String ip, String cookie) {
		QuestionRecord bean = new QuestionRecord();
		bean.setQuestion(question);
		bean.setUser(user);
		bean.setIp(ip);
		bean.setCookie(cookie);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public QuestionRecord update(QuestionRecord bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public QuestionRecord delete(Integer id) {
		QuestionRecord entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public QuestionRecord[] delete(Integer[] ids) {
		QuestionRecord[] beans = new QuestionRecord[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.deleteByUserId(Arrays.asList(ids));
		}
	}

	private QuestionRecordDao dao;

	@Autowired
	public void setDao(QuestionRecordDao dao) {
		this.dao = dao;
	}

}

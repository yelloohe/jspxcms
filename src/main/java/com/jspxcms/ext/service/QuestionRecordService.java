package com.jspxcms.ext.service;

import com.jspxcms.core.domain.User;
import com.jspxcms.ext.domain.Question;
import com.jspxcms.ext.domain.QuestionRecord;

public interface QuestionRecordService {
	public long countByUserId(Integer questionId, Integer userId,
			Integer interval);

	public long countByIp(Integer questionId, String ip, String cookie,
			Integer interval);

	public long countByCookie(Integer questionId, String cookie,
			Integer interval);

	public QuestionRecord get(Integer id);

	public QuestionRecord save(Question question, User user, String ip,
			String cookie);

	public QuestionRecord update(QuestionRecord bean);

	public QuestionRecord delete(Integer id);

	public QuestionRecord[] delete(Integer[] ids);
}

package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.QuestionRecord;
import com.jspxcms.ext.repository.plus.QuestionRecordDaoPlus;

public interface QuestionRecordDao extends Repository<QuestionRecord, Integer>,
		QuestionRecordDaoPlus {
	public Page<QuestionRecord> findAll(Specification<QuestionRecord> spec,
			Pageable pageable);

	public List<QuestionRecord> findAll(Specification<QuestionRecord> spec,
			Limitable limitable);

	public QuestionRecord findOne(Integer id);

	public QuestionRecord save(QuestionRecord bean);

	public void delete(QuestionRecord bean);

	// --------------------

	@Query("select count(*) from QuestionRecord bean where bean.question.id=?1 and bean.user.id=?2")
	public long countByUserId(Integer questionId, Integer userId);

	@Query("select count(*) from QuestionRecord bean where bean.question.id=?1 and bean.user.id=?2 and bean.date>?3")
	public long countByUserId(Integer questionId, Integer userId, Date after);

	@Query("select count(*) from QuestionRecord bean where bean.question.id=?1 and (bean.ip=?2 or bean.cookie=?3)")
	public long countByIp(Integer questionId, String ip, String cookie);

	@Query("select count(*) from QuestionRecord bean where bean.question.id=?1 and (bean.ip=?2 or bean.cookie=?3) and bean.date>?4")
	public long countByIp(Integer questionId, String ip, String cookie,
			Date after);

	@Query("select count(*) from QuestionRecord bean where bean.question.id=?1 and bean.cookie=?2")
	public long countByCookie(Integer questionId, String cookie);

	@Query("select count(*) from QuestionRecord bean where bean.question.id=?1 and bean.cookie=?2 and bean.date>?3")
	public long countByCookie(Integer questionId, String cookie, Date after);

	@Modifying
	@Query("delete from QuestionRecord bean where bean.question.id in (?1)")
	public int deleteByQuestionId(Collection<Integer> questionIds);

	@Modifying
	@Query("delete from QuestionRecord bean where bean.user.id in (?1)")
	public int deleteByUserId(Collection<Integer> userIds);
}

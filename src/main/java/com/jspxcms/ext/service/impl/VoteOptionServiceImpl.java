package com.jspxcms.ext.service.impl;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.ext.domain.Vote;
import com.jspxcms.ext.domain.VoteOption;
import com.jspxcms.ext.repository.VoteOptionDao;
import com.jspxcms.ext.service.VoteOptionService;

@Service
@Transactional(readOnly = true)
public class VoteOptionServiceImpl implements VoteOptionService {
	public VoteOption get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public VoteOption[] save(String[] title, Integer[] count, Vote vote) {
		int len = title != null ? title.length : 0;
		VoteOption[] beans = new VoteOption[len];
		VoteOption bean;
		for (int i = 0; i < len; i++) {
			bean = new VoteOption();
			bean.setVote(vote);
			bean.setTitle(title[i]);
			bean.setCount(count[i]);
			bean.setSeq(i);
			bean.applyDefaultValue();
			dao.save(bean);
			beans[i] = bean;
		}
		vote.setOptions(Arrays.asList(beans));
		return beans;
	}

	@Transactional
	public VoteOption[] update(Integer[] id, String[] title, Integer[] count,
			Vote vote) {
		int len = id != null ? id.length : 0;
		VoteOption[] beans = new VoteOption[len];
		VoteOption bean;
		// 修改和新增
		for (int i = 0; i < len; i++) {
			if (id[i] != null) {
				bean = dao.findOne(id[i]);
			} else {
				bean = new VoteOption();
			}
			bean.setVote(vote);
			bean.setTitle(title[i]);
			bean.setCount(count[i]);
			bean.setSeq(i);
			bean.applyDefaultValue();
			dao.save(bean);
			beans[i] = bean;
		}
		// 删除
		for (VoteOption item : vote.getOptions()) {
			if (!ArrayUtils.contains(id, item.getId())) {
				delete(item);
			}
		}
		vote.setOptions(Arrays.asList(beans));
		return beans;
	}

	@Transactional
	public VoteOption delete(VoteOption entity) {
		dao.delete(entity);
		return entity;
	}

	private VoteOptionDao dao;

	@Autowired
	public void setDao(VoteOptionDao dao) {
		this.dao = dao;
	}
}

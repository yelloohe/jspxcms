package com.jspxcms.core.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.ScoreBoard;
import com.jspxcms.core.domain.ScoreItem;
import com.jspxcms.core.listener.ScoreItemDeleteListener;
import com.jspxcms.core.repository.ScoreBoardDao;
import com.jspxcms.core.service.ScoreBoardService;
import com.jspxcms.core.service.ScoreItemService;

@Service
@Transactional(readOnly = true)
public class ScoreBoardServiceImpl implements ScoreBoardService,
		ScoreItemDeleteListener {
	public List<ScoreBoard> findList(String ftype, Integer fid) {
		return dao.findByFtypeAndFid(ftype, fid);
	}

	public ScoreBoard findOne(String ftype, Integer fid, Integer itemId) {
		return dao.findByFtypeAndFidAndItemId(ftype, fid, itemId);
	}

	public ScoreBoard get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public ScoreBoard scoring(String ftype, Integer fid, Integer itemId) {
		ScoreBoard board = dao.findByFtypeAndFidAndItemId(ftype, fid, itemId);
		if (board == null) {
			board = new ScoreBoard();
			ScoreItem item = scoreItemService.get(itemId);
			board.setItem(item);
			board.setFtype(ftype);
			board.setFid(fid);
			board.setVotes(1);
			save(board);
		} else {
			board.setVotes(board.getVotes() + 1);
		}
		return board;
	}

	@Transactional
	public ScoreBoard save(ScoreBoard bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public ScoreBoard update(ScoreBoard bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public ScoreBoard delete(Integer id) {
		ScoreBoard entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public ScoreBoard[] delete(Integer[] ids) {
		ScoreBoard[] beans = new ScoreBoard[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	@Transactional
	public int deleteByItemId(Integer itemId) {
		return dao.deleteByItemId(Arrays.asList(new Integer[] { itemId }));
	}

	public void preScoreItemDelete(Integer[] ids) {
		if (ids == null) {
			return;
		}
		dao.deleteByItemId(Arrays.asList(ids));
	}

	private ScoreItemService scoreItemService;

	@Autowired
	public void setScoreItemService(ScoreItemService scoreItemService) {
		this.scoreItemService = scoreItemService;
	}

	private ScoreBoardDao dao;

	@Autowired
	public void setDao(ScoreBoardDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoBuffer;
import com.jspxcms.core.domain.ScoreBoard;
import com.jspxcms.core.domain.ScoreItem;
import com.jspxcms.core.repository.InfoBufferDao;
import com.jspxcms.core.service.InfoBufferService;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.ScoreBoardService;
import com.jspxcms.core.service.ScoreItemService;
import com.jspxcms.core.service.VoteMarkService;

/**
 * InfoBufferServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class InfoBufferServiceImpl implements InfoBufferService {

	public InfoBuffer get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public InfoBuffer save(InfoBuffer bean, Info info) {
		bean.setInfo(info);
		bean.applyDefaultValue();
		info.setBuffer(bean);
		return bean;
	}

	@Transactional
	public int updateViews(Integer id) {
		Info info = infoQueryService.get(id);
		InfoBuffer buffer = get(id);
		if (buffer == null) {
			buffer = new InfoBuffer();
			save(buffer, info);
		}
		int views = info.getViews();
		int buffViews = buffer.getViews() + 1;
		if (buffViews >= info.getSite().getGlobal().getOther()
				.getBufferInfoViews()) {
			buffer.setViews(0);
			info.setViews(views + buffViews);
		} else {
			buffer.setViews(buffViews);
		}
		return views + buffViews;
	}

	@Transactional
	public int updateDownloads(Integer id) {
		Info info = infoQueryService.get(id);
		InfoBuffer buffer = get(id);
		if (buffer == null) {
			buffer = new InfoBuffer();
			save(buffer, info);
		}
		int downloads = info.getDownloads();
		int buffDownloads = buffer.getDownloads() + 1;
		// 根据缓冲设置
		if (buffDownloads >= info.getSite().getGlobal().getOther()
				.getBufferInfoDownloads()) {
			buffer.setDownloads(0);
			info.setDownloads(downloads + buffDownloads);
		} else {
			buffer.setDownloads(buffDownloads);
		}
		return downloads + buffDownloads;
	}

	@Transactional
	public int updateDiggs(Integer id, Integer userId, String ip, String cookie) {
		Info info = infoQueryService.get(id);
		InfoBuffer buffer = get(id);
		if (buffer == null) {
			buffer = new InfoBuffer();
			save(buffer, info);
		}
		int diggs = info.getDiggs();
		int buffDiggs = buffer.getDiggs() + 1;
		// 根据缓冲设置
		if (buffDiggs >= info.getSite().getGlobal().getOther()
				.getBufferInfoDiggs()) {
			buffer.setDiggs(0);
			info.setDiggs(diggs + buffDiggs);
		} else {
			buffer.setDiggs(buffDiggs);
		}
		voteMarkService.mark(Info.DIGG_MARK, id, userId, ip, cookie);
		return diggs + buffDiggs;
	}

	@Transactional
	public int updateBurys(Integer id, Integer userId, String ip, String cookie) {
		Info info = infoQueryService.get(id);
		InfoBuffer buffer = get(id);
		if (buffer == null) {
			buffer = new InfoBuffer();
			save(buffer, info);
		}
		// burys不常用，Info不保存burys的数据，直接保存在Buffer里面。
		int buffBurys = buffer.getBurys() + 1;
		buffer.setBurys(buffBurys);
		voteMarkService.mark(Info.DIGG_MARK, id, userId, ip, cookie);
		return buffBurys;
	}

	@Transactional
	public int updateScore(Integer id, Integer itemId, Integer userId,
			String ip, String cookie) {
		Info info = infoQueryService.get(id);
		InfoBuffer buffer = get(id);
		if (buffer == null) {
			buffer = new InfoBuffer();
			save(buffer, info);
		}
		ScoreItem item = scoreItemService.get(itemId);
		int score = info.getScore();
		int buffScore = buffer.getScore() + item.getScore();
		if (buffScore >= info.getSite().getGlobal().getOther()
				.getBufferInfoScore()) {
			buffer.setScore(0);
			info.setScore(score + buffScore);
		} else {
			buffer.setScore(buffScore);
		}
		ScoreBoard board = scoreBoardService.scoring(Info.SCORE_MARK, id,
				itemId);
		voteMarkService.mark(Info.SCORE_MARK, id, userId, ip, cookie);
		return board.getVotes();
	}

	private VoteMarkService voteMarkService;
	private ScoreItemService scoreItemService;
	private ScoreBoardService scoreBoardService;
	private InfoQueryService infoQueryService;
	private InfoBufferDao dao;

	@Autowired
	public void setVoteMarkService(VoteMarkService voteMarkService) {
		this.voteMarkService = voteMarkService;
	}

	@Autowired
	public void setScoreItemService(ScoreItemService scoreItemService) {
		this.scoreItemService = scoreItemService;
	}

	@Autowired
	public void setScoreBoardService(ScoreBoardService scoreBoardService) {
		this.scoreBoardService = scoreBoardService;
	}

	@Autowired
	public void setInfoQueryService(InfoQueryService infoQueryService) {
		this.infoQueryService = infoQueryService;
	}

	@Autowired
	public void setDao(InfoBufferDao dao) {
		this.dao = dao;
	}
}

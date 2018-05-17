package com.jspxcms.core.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.VoteMark;
import com.jspxcms.core.repository.VoteMarkDao;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.service.VoteMarkService;

@Service
@Transactional(readOnly = true)
public class VoteMarkServiceImpl implements VoteMarkService {
	public boolean isUserVoted(String ftype, Integer fid, Integer userId,
			Integer beforeHour) {
		return isVoted(ftype, fid, userId, null, null, beforeHour);
	}

	public boolean isIpVoted(String ftype, Integer fid, String ip,
			Integer beforeHour) {
		return isVoted(ftype, fid, null, ip, null, beforeHour);
	}

	public boolean isCookieVoted(String ftype, Integer fid, String cookie,
			Integer beforeHour) {
		return isVoted(ftype, fid, null, null, cookie, beforeHour);
	}

	public boolean isVoted(String ftype, Integer fid, Integer userId,
			String ip, String cookie, Integer beforeHour) {
		Date after = null;
		if (beforeHour != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -beforeHour);
			after = cal.getTime();
		}
		return dao.countMark(ftype, fid, userId, ip, cookie, after) > 0;
	}

	public VoteMark get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public VoteMark mark(String ftype, Integer fid, Integer userId, String ip,
			String cookie) {
		VoteMark bean = new VoteMark();
		if (userId != null) {
			User user = userService.get(userId);
			bean.setUser(user);
		}
		bean.setFtype(ftype);
		bean.setFid(fid);
		bean.setIp(ip);
		bean.setCookie(cookie);
		return save(bean);
	}

	@Transactional
	public int unmark(String ftype, Integer fid) {
		return dao.unmark(ftype, fid);
	}

	@Transactional
	public VoteMark save(VoteMark bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public VoteMark update(VoteMark bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public VoteMark delete(Integer id) {
		VoteMark entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public VoteMark[] delete(Integer[] ids) {
		VoteMark[] beans = new VoteMark[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private VoteMarkDao dao;

	@Autowired
	public void setDao(VoteMarkDao dao) {
		this.dao = dao;
	}
}

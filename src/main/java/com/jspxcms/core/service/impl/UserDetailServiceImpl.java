package com.jspxcms.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.User;
import com.jspxcms.core.domain.UserDetail;
import com.jspxcms.core.repository.UserDetailDao;
import com.jspxcms.core.service.UserDetailService;

@Service
@Transactional(readOnly = true)
public class UserDetailServiceImpl implements UserDetailService {
	public UserDetail get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public UserDetail save(UserDetail detail, User user, String ip) {
		detail.setUser(user);
		detail.setCreationIp(ip);
		detail.applyDefaultValue();
		detail = dao.save(detail);
		user.setDetail(detail);
		return detail;
	}

	@Transactional
	public UserDetail update(UserDetail bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	private UserDetailDao dao;

	@Autowired
	public void setDao(UserDetailDao dao) {
		this.dao = dao;
	}
}

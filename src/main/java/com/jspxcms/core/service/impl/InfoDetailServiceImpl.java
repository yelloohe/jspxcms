package com.jspxcms.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoDetail;
import com.jspxcms.core.repository.InfoDetailDao;
import com.jspxcms.core.service.InfoDetailService;

/**
 * InfoDetailServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class InfoDetailServiceImpl implements InfoDetailService {
	public InfoDetail get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public InfoDetail save(InfoDetail detail, Info info) {
		info.setDetail(detail);
		detail.setInfo(info);
		detail.applyDefaultValue();
		dao.save(detail);
		return detail;
	}

	@Transactional
	public InfoDetail update(InfoDetail bean, Info info) {
		bean.setInfo(info);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		info.setDetail(bean);
		return bean;
	}

	private InfoDetailDao dao;

	@Autowired
	public void setDao(InfoDetailDao dao) {
		this.dao = dao;
	}
}

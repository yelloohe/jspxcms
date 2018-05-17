package com.jspxcms.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoSpecial;
import com.jspxcms.core.domain.InfoSpecial.InfoSpecialId;
import com.jspxcms.core.domain.Special;
import com.jspxcms.core.repository.InfoSpecialDao;
import com.jspxcms.core.service.InfoSpecialService;
import com.jspxcms.core.service.SpecialService;

@Service
@Transactional(readOnly = true)
public class InfoSpecialServiceImpl implements InfoSpecialService {
	private InfoSpecial findOrCreate(Info info, Special special) {
		InfoSpecial bean = dao.findOne(new InfoSpecialId(info.getId(), special.getId()));
		if (bean == null) {
			bean = new InfoSpecial(info, special);
		}
		return bean;
	}

	@Transactional
	public void update(Info info, Integer[] specialIds) {
		// 为null不更新。要设置为空，请传空数组。
		if (specialIds == null) {
			return;
		}
		List<InfoSpecial> infoSpecials = info.getInfoSpecials();
		infoSpecials.clear();
		for (Integer id : specialIds) {
			infoSpecials.add(findOrCreate(info, specialService.get(id)));
		}
	}

	@Transactional
	public void deleteBySpecialId(Integer specialId) {
		Special special = specialService.get(specialId);
		for(Info info : dao.findByInfoSpecialsSpecialId(specialId)) {
			InfoSpecial infoSpecial = new InfoSpecial(info,special);
			info.getInfoSpecials().remove(infoSpecial);
			dao.delete(infoSpecial);
		}
	}

	private SpecialService specialService;

	@Autowired
	public void setSpecialService(SpecialService specialService) {
		this.specialService = specialService;
	}

	private InfoSpecialDao dao;

	@Autowired
	public void setDao(InfoSpecialDao dao) {
		this.dao = dao;
	}
}

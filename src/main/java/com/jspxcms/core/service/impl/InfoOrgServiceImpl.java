package com.jspxcms.core.service.impl;

import java.util.Arrays;
import java.util.SortedSet;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoOrg;
import com.jspxcms.core.domain.InfoOrg.InfoOrgId;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.listener.OrgDeleteListener;
import com.jspxcms.core.repository.InfoOrgDao;
import com.jspxcms.core.service.InfoOrgService;
import com.jspxcms.core.service.OrgService;

@Service
@Transactional(readOnly = true)
public class InfoOrgServiceImpl implements InfoOrgService, OrgDeleteListener {
	private InfoOrg findOrCreate(Info info, Org org, Boolean viewPerm) {
		InfoOrg bean = dao.findOne(new InfoOrgId(info.getId(), org.getId()));
		if (bean == null) {
			bean = new InfoOrg(info, org);
		}
		bean.setViewPerm(viewPerm);
		return bean;
	}

	@Transactional
	public void update(Info info, Integer[] viewOrgIds) {
		// 为null不更新。要设置为空，请传空数组。
		if (viewOrgIds == null) {
			return;
		}
		SortedSet<InfoOrg> infoOrgs = info.getInfoOrgs();
		// 先更新
		for (InfoOrg infoOrg : infoOrgs) {
			infoOrg.setViewPerm(ArrayUtils.contains(viewOrgIds, infoOrg.getOrg().getId()));
		}
		// 再新增
		for (Integer id : viewOrgIds) {
			infoOrgs.add(findOrCreate(info, orgService.get(id), true));
		}
	}

	public void preOrgDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		dao.deleteByOrgId(Arrays.asList(ids));
	}

	private OrgService orgService;

	@Autowired
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}

	private InfoOrgDao dao;

	@Autowired
	public void setDao(InfoOrgDao dao) {
		this.dao = dao;
	}
}

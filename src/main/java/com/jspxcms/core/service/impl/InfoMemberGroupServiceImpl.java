package com.jspxcms.core.service.impl;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoMemberGroup;
import com.jspxcms.core.domain.InfoMemberGroup.InfoMemberGroupId;
import com.jspxcms.core.domain.MemberGroup;
import com.jspxcms.core.listener.MemberGroupDeleteListener;
import com.jspxcms.core.repository.InfoMemberGroupDao;
import com.jspxcms.core.service.InfoMemberGroupService;
import com.jspxcms.core.service.MemberGroupService;

@Service
@Transactional(readOnly = true)
public class InfoMemberGroupServiceImpl implements InfoMemberGroupService, MemberGroupDeleteListener {

	private InfoMemberGroup findOrCreate(Info info, MemberGroup group, Boolean viewPerm) {
		InfoMemberGroup bean = dao.findOne(new InfoMemberGroupId(info.getId(), group.getId()));
		if (bean == null) {
			bean = new InfoMemberGroup(info, group);
		}
		bean.setViewPerm(viewPerm);
		return bean;
	}

	@Transactional
	public void update(Info info, Integer[] viewGroupIds) {
		// 为null不更新。要设置为空，请传空数组。
		if (viewGroupIds == null) {
			return;
		}
		Set<InfoMemberGroup> infoGroups = info.getInfoGroups();
		// 先更新
		for (InfoMemberGroup infoGroup : infoGroups) {
			infoGroup.setViewPerm(ArrayUtils.contains(viewGroupIds, infoGroup.getGroup().getId()));
		}
		// 再新增
		for (Integer id : viewGroupIds) {
			infoGroups.add(findOrCreate(info, groupService.get(id), true));
		}
	}

	@Override
	public void preMemberGroupDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		dao.deleteByGroupId(Arrays.asList(ids));
	}

	private MemberGroupService groupService;

	@Autowired
	public void setMemberGroupService(MemberGroupService groupService) {
		this.groupService = groupService;
	}

	private InfoMemberGroupDao dao;

	@Autowired
	public void setDao(InfoMemberGroupDao dao) {
		this.dao = dao;
	}
}

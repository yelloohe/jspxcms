package com.jspxcms.core.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.ScoreGroup;
import com.jspxcms.core.domain.ScoreItem;
import com.jspxcms.core.listener.ScoreItemDeleteListener;
import com.jspxcms.core.repository.ScoreItemDao;
import com.jspxcms.core.service.ScoreItemService;

@Service
@Transactional(readOnly = true)
public class ScoreItemServiceImpl implements ScoreItemService {
	public ScoreItem get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public ScoreItem[] save(String[] name, Integer[] score, String[] icon,
			ScoreGroup group) {
		int len = name != null ? name.length : 0;
		ScoreItem[] beans = new ScoreItem[len];
		ScoreItem bean;
		for (int i = 0; i < len; i++) {
			bean = new ScoreItem();
			bean.setGroup(group);
			bean.setSite(group.getSite());
			bean.setName(name[i]);
			bean.setScore(score[i]);
			bean.setIcon(icon[i]);
			bean.setSeq(i);
			bean.applyDefaultValue();
			dao.save(bean);
			beans[i] = bean;
		}
		group.setItems(Arrays.asList(beans));
		return beans;
	}

	@Transactional
	public ScoreItem[] update(Integer[] id, String[] name, Integer[] score,
			String[] icon, ScoreGroup group) {
		int len = id != null ? id.length : 0;
		ScoreItem[] beans = new ScoreItem[len];
		ScoreItem bean;
		// 修改和新增
		for (int i = 0; i < len; i++) {
			if (id[i] != null) {
				bean = dao.findOne(id[i]);
			} else {
				bean = new ScoreItem();
			}
			bean.setGroup(group);
			bean.setSite(group.getSite());
			bean.setName(name[i]);
			bean.setScore(score[i]);
			bean.setIcon(icon[i]);
			bean.setSeq(i);
			bean.applyDefaultValue();
			dao.save(bean);
			beans[i] = bean;
		}
		// 删除
		for (ScoreItem item : group.getItems()) {
			if (!ArrayUtils.contains(id, item.getId())) {
				delete(item);
			}
		}
		group.setItems(Arrays.asList(beans));
		return beans;
	}

	@Transactional
	public ScoreItem delete(ScoreItem bean) {
		firePreDelete(new Integer[] { bean.getId() });
		dao.delete(bean);
		return bean;
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (ScoreItemDeleteListener listener : deleteListeners) {
				listener.preScoreItemDelete(ids);
			}
		}
	}

	private List<ScoreItemDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(List<ScoreItemDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private ScoreItemDao dao;

	@Autowired
	public void setDao(ScoreItemDao dao) {
		this.dao = dao;
	}
}

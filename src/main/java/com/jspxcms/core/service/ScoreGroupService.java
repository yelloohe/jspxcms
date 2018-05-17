package com.jspxcms.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.ScoreGroup;

public interface ScoreGroupService {
	public List<ScoreGroup> findList(Integer siteId,
			Map<String, String[]> params, Sort sort);

	public RowSide<ScoreGroup> findSide(Integer siteId,
			Map<String, String[]> params, ScoreGroup bean, Integer position,
			Sort sort);

	public ScoreGroup findByNumber(Integer siteId, String number);

	public ScoreGroup findTopOne(Integer siteId);

	public boolean numberExist(String number, Integer siteId);

	public ScoreGroup get(Integer id);

	public ScoreGroup save(ScoreGroup bean, String[] name, Integer[] score,
			String[] icon, Integer siteId);

	public ScoreGroup update(ScoreGroup bean, Integer[] itemId,
			String[] itemName, Integer[] itemScore, String[] itemIcon);

	public ScoreGroup[] batchUpdate(Integer[] id, String[] name,
			String[] number, String[] description);

	public ScoreGroup delete(Integer id);

	public ScoreGroup[] delete(Integer[] ids);
}

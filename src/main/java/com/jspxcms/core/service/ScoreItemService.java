package com.jspxcms.core.service;

import com.jspxcms.core.domain.ScoreGroup;
import com.jspxcms.core.domain.ScoreItem;

public interface ScoreItemService {
	public ScoreItem get(Integer id);

	public ScoreItem[] save(String[] name, Integer[] score, String[] icon,
			ScoreGroup group);

	public ScoreItem[] update(Integer[] id, String[] name, Integer[] score,
			String[] icon, ScoreGroup group);

	public ScoreItem delete(ScoreItem bean);
}

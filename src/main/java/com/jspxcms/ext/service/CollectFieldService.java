package com.jspxcms.ext.service;

import java.util.List;

import com.jspxcms.ext.domain.CollectField;

public interface CollectFieldService {
	public CollectField get(Integer id);

	public List<CollectField> save(String[] code, String[] name,
			Integer[] type, Integer collectId, Integer siteId);

	public CollectField save(CollectField bean, Integer collectId,
			Integer siteId);

	public List<CollectField> update(Integer[] id, Integer[] sourceType,
			String[] sourceText, String[] sourceUrl, String[] dataPattern,
			Boolean[] dataReg, String[] dataAreaPattern, Boolean[] dataAreaReg,
			String[] filter, String[] downloadType, String[] imageParam,
			String[] dateFormat);

	public CollectField delete(Integer id);

	public CollectField[] delete(Integer[] ids);
}

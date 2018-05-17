package com.jspxcms.ext.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.ext.domain.Collect;
import com.jspxcms.ext.domain.CollectField;
import com.jspxcms.ext.repository.CollectFieldDao;
import com.jspxcms.ext.service.CollectFieldService;
import com.jspxcms.ext.service.CollectService;

@Service
@Transactional(readOnly = true)
public class CollectFieldServiceImpl implements CollectFieldService {

	public CollectField get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public List<CollectField> save(String[] code, String[] name,
			Integer[] type, Integer collectId, Integer siteId) {
		List<CollectField> list = new ArrayList<CollectField>();
		for (int i = 0, len = code.length; i < len; i++) {
			CollectField bean = new CollectField();
			bean.setCode(code[i]);
			bean.setName(name[i]);
			bean.setType(type[i]);
			save(bean, collectId, siteId);
			list.add(bean);
		}
		return list;
	}

	@Transactional
	public CollectField save(CollectField bean, Integer collectId,
			Integer siteId) {
		Collect collect = collectService.get(collectId);
		bean.setCollect(collect);
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public List<CollectField> update(Integer[] id, Integer[] sourceType,
			String[] sourceText, String[] sourceUrl, String[] dataPattern,
			Boolean[] dataReg, String[] dataAreaPattern, Boolean[] dataAreaReg,
			String[] filter, String[] downloadType, String[] imageParam,
			String[] dateFormat) {
		List<CollectField> list = new ArrayList<CollectField>();
		for (int i = 0, len = id.length; i < len; i++) {
			CollectField bean = dao.findOne(id[i]);
			bean.setSourceType(sourceType[i]);
			bean.setSourceText(sourceText[i]);
			bean.setSourceUrl(sourceUrl[i]);
			bean.setDataPattern(dataPattern[i]);
			bean.setDataReg(dataReg[i]);
			bean.setDataAreaPattern(dataAreaPattern[i]);
			bean.setDataAreaReg(dataAreaReg[i]);
			bean.setFilter(filter[i]);
			bean.setDownloadType(downloadType[i]);
			bean.setImageParam(imageParam[i]);
			bean.setDateFormat(dateFormat[i]);
			dao.save(bean);
			list.add(bean);
		}
		return list;
	}

	@Transactional
	public CollectField delete(Integer id) {
		CollectField entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public CollectField[] delete(Integer[] ids) {
		CollectField[] beans = new CollectField[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	private CollectService collectService;
	private SiteService siteService;

	@Autowired
	public void setCollectService(CollectService collectService) {
		this.collectService = collectService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private CollectFieldDao dao;

	@Autowired
	public void setDao(CollectFieldDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service.impl;

import com.jspxcms.core.domain.Attribute;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.repository.AttributeDao;
import com.jspxcms.core.service.AttributeService;
import com.jspxcms.core.service.InfoAttributeService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.support.DeleteException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * AttributeServiceImpl
 *
 * @author liufang
 */
@Service
@Transactional(readOnly = true)
public class AttributeServiceImpl implements AttributeService, SiteDeleteListener {
    public List<Attribute> findList(Integer siteId) {
        return dao.findBySiteId(siteId, new Sort("seq", "id"));
    }

    public List<Attribute> findByNumbers(String[] numbers, Integer[] siteIds) {
        return dao.findByNumbers(numbers, siteIds);
    }


    public Attribute findByNumber(Integer siteId, String number) {
        List<Attribute> list = dao.findBySiteIdAndNumber(siteId, number);
        return list.isEmpty() ? null : list.get(0);
    }

    public Attribute get(Integer id) {
        return dao.findOne(id);
    }

    public boolean numberExist(String number, Integer siteId) {
        return dao.countByNumber(number, siteId) > 0;
    }

    @Transactional
    public Attribute save(Attribute bean, Integer siteId) {
        Site site = siteService.get(siteId);
        bean.setSite(site);
        if (bean.getImageHeight() != null && bean.getImageWidth() != null) {
            bean.setWithImage(true);
        } else {
            bean.setWithImage(false);
        }
        bean.applyDefaultValue();
        dao.save(bean);
        return bean;
    }

    @Transactional
    public Attribute clone(Attribute src, Integer siteId) {
        Attribute dest = new Attribute();
        // 关联对象不要复制，只复制简单的属性
        BeanUtils.copyProperties(src, dest, "id", "site");
        return save(dest, siteId);
    }

    @Transactional
    public void importAttribute(List<Attribute> attributeList, Integer siteId) {
        for (Attribute bean : attributeList) {
            save(bean, siteId);
        }
    }

    @Transactional
    public Attribute update(Attribute bean) {
        if (bean.getImageHeight() != null && bean.getImageWidth() != null) {
            bean.setWithImage(true);
        } else {
            bean.setWithImage(false);
            bean.setImageWidth(null);
            bean.setImageHeight(null);
        }
        bean.applyDefaultValue();
        dao.save(bean);
        return bean;
    }

    @Transactional
    public Attribute[] batchUpdate(Integer[] id, String[] name, String[] number, Integer[] imageWidth,
                                   Integer[] imageHeight, Boolean[] scale, Boolean[] exact, Boolean[] watermark) {
        Attribute[] beans = new Attribute[id.length];
        for (int i = 0, len = id.length; i < len; i++) {
            beans[i] = get(id[i]);
            beans[i].setSeq(i);
            beans[i].setName(name[i]);
            beans[i].setNumber(number[i]);
            beans[i].setImageWidth(imageWidth[i]);
            beans[i].setImageHeight(imageHeight[i]);
            beans[i].setScale(scale[i]);
            beans[i].setExact(exact[i]);
            beans[i].setWatermark(watermark[i]);
            update(beans[i]);
        }
        return beans;
    }

    @Transactional
    public Attribute delete(Integer id) {
        Attribute entity = dao.findOne(id);
        infoAttrService.deleteByAttributeId(id);
        dao.delete(entity);
        return entity;
    }

    @Transactional
    public Attribute[] delete(Integer[] ids) {
        Attribute[] beans = new Attribute[ids.length];
        for (int i = 0; i < ids.length; i++) {
            beans[i] = delete(ids[i]);
        }
        return beans;
    }

    public void preSiteDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
                throw new DeleteException("attribute.management");
            }
        }

    }

    private InfoAttributeService infoAttrService;

    @Autowired
    public void setInfoAttrService(InfoAttributeService infoAttrService) {
        this.infoAttrService = infoAttrService;
    }

    private SiteService siteService;

    @Autowired
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    private AttributeDao dao;

    @Autowired
    public void setDao(AttributeDao dao) {
        this.dao = dao;
    }
}

package com.jspxcms.core.service;

import com.jspxcms.core.domain.Attribute;

import java.util.List;

/**
 * AttributeService
 *
 * @author liufang
 */
public interface AttributeService {
    public List<Attribute> findList(Integer siteId);

    public List<Attribute> findByNumbers(String[] numbers, Integer[] siteIds);

    public Attribute findByNumber(Integer siteId, String number);

    public Attribute get(Integer id);

    public boolean numberExist(String number, Integer siteId);

    public Attribute save(Attribute bean, Integer siteId);

    public Attribute clone(Attribute src, Integer siteId);

    public void importAttribute(List<Attribute> attributeList, Integer siteId);

    public Attribute update(Attribute bean);

    public Attribute[] batchUpdate(Integer[] id, String[] name,
                                   String[] number, Integer[] imageWidth, Integer[] imageHeight,
                                   Boolean[] scale, Boolean[] exact, Boolean[] watermark);

    public Attribute delete(Integer id);

    public Attribute[] delete(Integer[] ids);
}

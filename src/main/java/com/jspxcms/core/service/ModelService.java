package com.jspxcms.core.service;

import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Model;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * ModelService
 *
 * @author liufang
 */
public interface ModelService {
    public List<Model> findList(Integer siteId, String type, Map<String, String[]> params, Sort sort);

    public RowSide<Model> findSide(Integer siteId, String type, Map<String, String[]> params, Model bean,
                                   Integer position, Sort sort);

    public List<Model> findList(Integer siteId, String type);

    public Model findDefault(Integer siteId, String type);

    public List<Model> findByNumbers(String[] numbers, Integer[] siteIds);

    public Model findByTypeAndName(Integer siteId, String type, String name);

    public Model get(Integer id);

    /**
     * 一键开启静态化
     *
     * @param siteId
     */
    public void oneKeyEnableHtml(Integer siteId);

    /**
     * 一键关闭静态化
     *
     * @param siteId
     */
    public void oneKeyDisableHtml(Integer siteId);

    public Model save(Model bean, Integer siteId, Map<String, String> customs);

    public Model copy(Integer oid, Model bean, Integer siteId, Map<String, String> customs);

    public Model clone(Model model, Integer siteId);

    public void importModels(List<Model> modelList, Integer siteId);

    public Model[] batchUpdate(Integer[] id, String[] name, String[] number);

    public Model update(Model bean, Map<String, String> customs);

    public Model delete(Integer id);

    public Model[] delete(Integer[] ids);
}

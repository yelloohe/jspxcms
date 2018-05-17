package com.jspxcms.core.service;

import com.jspxcms.core.domain.InfoPush;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 信息推送Service
 * <p>
 * Created by PONY on 2017/8/5.
 */
public interface InfoPushService {
    /**
     * 获取推送分页列表
     *
     * @param siteId   推送源站点
     * @param params   查询参数
     * @param pageable 分页参数
     * @return 分页列表对象
     */
    public Page<InfoPush> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable);

    /**
     * 保存推送信息
     *
     * @param infoId     推送文档ID
     * @param fromSiteId 推送源站点ID
     * @param toSiteId   推送目标站点ID
     * @param userId     推送人ID
     * @return 推送对象
     */
    public InfoPush save(Integer infoId, Integer fromSiteId, Integer toSiteId, Integer userId);

    public InfoPush get(Integer id);
}

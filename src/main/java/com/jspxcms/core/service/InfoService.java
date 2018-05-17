package com.jspxcms.core.service;

import com.jspxcms.core.domain.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * InfoService
 *
 * @author liufang
 */
public interface InfoService {
    public Info save(Info bean, InfoDetail detail, Integer[] nodeIds,
                     Integer[] specialIds, Integer[] viewGroupIds, Integer[] viewOrgIds,
                     Map<String, String> customs, Map<String, String> clobs,
                     List<InfoImage> images, List<InfoFile> files, Integer[] attrIds,
                     Map<String, String> attrImages, String[] tagNames, Integer nodeId,
                     Integer creatorId, String status, Integer siteId, Integer fromSiteId);

    public Info clone(Info srcInfo, Integer siteId, Integer fromSiteId, Integer nodeId, Integer userId, String status);

    public List<Info> importInfo(List<Info> infoList, Integer userId, Integer siteId);

    public List<Info> push(Integer[] ids, Integer[] siteIds, Integer[] nodeIds, Integer userId);

    public List<InfoPush> pushDelete(Integer[] ids);

    public Info update(Info bean, InfoDetail detail, Integer[] nodeIds,
                       Integer[] specialIds, Integer[] viewGroupIds, Integer[] viewOrgIds,
                       Map<String, String> customs, Map<String, String> clobs,
                       List<InfoImage> images, List<InfoFile> files, Integer[] attrIds,
                       Map<String, String> attrImages, String[] tagNames, Integer nodeId,
                       User operator, boolean pass, boolean isContribute);

    public List<Info> pass(Integer[] ids, Integer userId, String opinion);

    public List<Info> reject(Integer[] ids, Integer userId, String opinion,
                             boolean rejectEnd);

    public List<Info> move(Integer[] ids, Integer nodeId);

    public int moveByNodeId(Collection<Integer> nodeIds, Integer nodeId);

    public List<Info> logicDelete(Integer[] ids);

    public List<Info> recall(Integer[] ids);

    public List<Info> archive(Integer[] ids);

    public List<Info> antiArchive(Integer[] ids);

    public List<Info> massWeixin(Integer[] ids);

    public Info delete(Integer id);

    public List<Info> delete(Integer[] ids);

    public int publish(Integer siteId);

    public int tobePublish(Integer siteId);

    public int expired(Integer siteId);
}

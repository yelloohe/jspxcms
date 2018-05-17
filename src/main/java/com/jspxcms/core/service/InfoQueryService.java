package com.jspxcms.core.service;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * InfoQueryService
 *
 * @author liufang
 */
public interface InfoQueryService {
    public Page<Info> findAll(Integer siteId, Integer mainNodeId, Integer nodeId, String treeNumber, Integer userId,
                              boolean allInfoPerm, int infoRightType, String status, Map<String, String[]> params, Pageable pageable);

    public RowSide<Info> findSide(Integer siteId, Integer mainNodeId, Integer nodeId, String treeNumber,
                                  Integer userId, boolean allInfoPerm, int infoPermType, String status, Map<String, String[]> params,
                                  Info bean, Integer position, Sort sort);

    public List<Info> findAll(Iterable<Integer> ids);

	public List<Info> findList(Integer[] modelId, Integer[] nodeId, Integer[] attrId, Integer[] specialId,
			Integer[] tagId, Integer[] siteId, Integer[] mainNodeId, Integer[] userId, Integer[] viewGroupId,
			Integer[] viewOrgId, String[] treeNumber, String[] specialTitle, String[] tagName, Integer[] priority,
			Date beginDate, Date endDate, String[] title, Integer[] includeId, Integer[] excludeId,
			Integer[] excludeMainNodeId, String[] excludeTreeNumber, Boolean isWithImage, String[] status,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6, Limitable limitable);

	public Page<Info> findPage(Integer[] modelId, Integer[] nodeId, Integer[] attrId, Integer[] specialId,
			Integer[] tagId, Integer[] siteId, Integer[] mainNodeId, Integer[] userId, Integer[] viewGroupId,
			Integer[] viewOrgId, String[] treeNumber, String[] specialTitle, String[] tagName, Integer[] priority,
			Date beginDate, Date endDate, String[] title, Integer[] includeId, Integer[] excludeId,
			Integer[] excludeMainNodeId, String[] excludeTreeNumber, Boolean isWithImage, String[] status,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6, Pageable pageable);


    public List<Info> findBySiteId(Integer siteId);

    public Info findNext(Integer id, boolean bySite);

    public Info findPrev(Integer id, boolean bySite);

    public boolean containsTitle(String title, Integer siteId);

    public long countByDate(Integer siteId, Date beginDate);

    public Info get(Integer id);
}

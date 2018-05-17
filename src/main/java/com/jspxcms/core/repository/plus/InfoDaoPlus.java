package com.jspxcms.core.repository.plus;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * InfoDaoPlus
 * 
 * @author liufang
 * 
 */
public interface InfoDaoPlus {
	public List<Info> findList(Integer[] modelId, Integer[] nodeId,
			Integer[] attrId, Integer[] specialId, Integer[] tagId,
			Integer[] siteId, Integer[] mainNodeId, Integer[] userId,
			Integer[] viewGroupId, Integer[] viewOrgId, String[] treeNumber,
			String[] specialTitle, String[] tagName, Integer[] priority,
			Date beginDate, Date endDate, String[] title, Integer[] includeId,
			Integer[] excludeId, Integer[] excludeMainNodeId,
			String[] excludeTreeNumber, Boolean isWithImage, String[] status,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4,
			Integer[] p5, Integer[] p6, Limitable limitable);

	public Page<Info> findPage(Integer[] modelId, Integer[] nodeId,
			Integer[] attrId, Integer[] specialId, Integer[] tagId,
			Integer[] siteId, Integer[] mainNodeId, Integer[] userId,
			Integer[] viewGroupId, Integer[] viewOrgId, String[] treeNumber,
			String[] specialTitle, String[] tagName, Integer[] priority,
			Date beginDate, Date endDate, String[] title, Integer[] includeId,
			Integer[] excludeId, Integer[] excludeMainNodeId,
			String[] excludeTreeNumber, Boolean isWithImage, String[] status,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4,
			Integer[] p5, Integer[] p6, Pageable pageable);

	public Info findNext(Integer siteId, Integer nodeId, Integer id,
			Date publishDate);

	public Info findPrev(Integer siteId, Integer nodeId, Integer id,
			Date publishDate);

	public List<Info> findForHtml(Integer siteId, Integer nodeId,
			String treeNumber, boolean forUpdate, Integer lastId, int maxResult);
}

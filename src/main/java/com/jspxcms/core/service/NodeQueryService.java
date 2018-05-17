package com.jspxcms.core.service;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.core.domain.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * NodeQueryService
 *
 * @author liufang
 */
public interface NodeQueryService {

    public List<Node> findList(Integer siteId);

    public List<Node> findList(Integer siteId, Integer parentId);

    public List<Node> findList(Integer siteId, Integer parentId,
                               Boolean isRealNode, Boolean isHidden);

	public List<Node> findList(Integer[] siteId, Integer parentId,
			String treeNumber, Boolean isRealNode, Boolean isHidden,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4,
			Integer[] p5, Integer[] p6, Limitable limitable);

	public Page<Node> findPage(Integer[] siteId, Integer parentId,
			String treeNumber, Boolean isRealNode, Boolean isHidden,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4,
			Integer[] p5, Integer[] p6, Pageable pageable);

    public List<Node> findByIds(Integer... ids);

    public List<Node> findByIds(Integer[] ids, Integer selfId);

    public List<Node> findChildren(Integer parentId);

    public List<Node> findChildren(Integer parentId, Boolean isRealNode,
                                   Boolean isHidden, Integer offset, Integer limit);

    public Node findRoot(Integer siteId);

    public Node findByNumber(Integer siteId, String number);

    public Node findByName(Integer siteId, String name);

    public List<Node> findByNumberLike(String[] numbers, Integer[] siteIds);

    public List<Node> findByNumber(String[] numbers, Integer[] siteIds);

    public List<Node> findList(Integer siteId, String treeNumber,
                               Integer parentId, Integer userId, boolean isAllNodePerm,
                               Map<String, String[]> params, Sort sort);

    public RowSide<Node> findSide(Integer siteId, String treeNumber,
                                  Integer parentId, Integer userId, boolean isAllNodePerm,
                                  Map<String, String[]> params, Node bean, Integer position, Sort sort);

    public Node get(Integer id);
}

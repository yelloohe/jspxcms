package com.jspxcms.core.service.impl;

import com.jspxcms.common.orm.LimitRequest;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.domain.NodeRole;
import com.jspxcms.core.repository.NodeDao;
import com.jspxcms.core.service.NodeQueryService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * NodeQueryServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class NodeQueryServiceImpl implements NodeQueryService {
	public List<Node> findList(Integer siteId, String treeNumber, Integer parentId, Integer userId,
			boolean isAllNodePerm, Map<String, String[]> params, Sort sort) {
		List<Node> list = dao.findAll(spec(siteId, treeNumber, parentId, userId, isAllNodePerm, params), sort);
		return list;
	}

	public RowSide<Node> findSide(Integer siteId, String treeNumber, Integer parentId, Integer userId,
			boolean isAllNodePerm, Map<String, String[]> params, Node bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<Node>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Node> list = dao.findAll(spec(siteId, treeNumber, parentId, userId, isAllNodePerm, params), limit);

		return RowSide.create(list, bean);
	}

	private Specification<Node> spec(final Integer siteId, final String treeNumber, final Integer parentId,
			final Integer userId, final boolean allNodePerm, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Node> fs = SearchFilter.spec(filters, Node.class);
		Specification<Node> sp = new Specification<Node>() {
			public Predicate toPredicate(Root<Node> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fs.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site").<Integer>get("id"), siteId));
				}
				if (StringUtils.isNotBlank(treeNumber)) {
					Path<String> tnPath = root.<String>get("treeNumber");
					pred = cb.and(pred, cb.like(tnPath, treeNumber + "%"));
				} else if (parentId != null) {
					pred = cb.and(pred, cb.equal(root.get("parent").<Integer>get("id"), parentId));
				}
				if (!allNodePerm) {
					Join<Node, NodeRole> nodeRoleJoin = root.join("nodeRoles");
					Path<Integer> userPath = nodeRoleJoin.join("role").join("userRoles").join("user")
							.<Integer>get("id");
					pred = cb.and(pred, cb.equal(userPath, userId));
					pred = cb.and(pred, cb.equal(nodeRoleJoin.get("nodePerm"), true));
					query.distinct(true);
				}
				return pred;
			}
		};
		return sp;
	}

	public List<Node> findList(Integer[] siteId, Integer parentId, String treeNumber, Boolean isRealNode,
			Boolean isHidden,Integer[] p0, Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6,
			Limitable limitable) {
		return dao.findList(siteId, parentId, treeNumber, isRealNode, isHidden, p0,p1, p2, p3, p4, p5, p6, limitable);
	}

	public Page<Node> findPage(Integer[] siteId, Integer parentId, String treeNumber, Boolean isRealNode,
			Boolean isHidden, Integer[] p0,Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4, Integer[] p5, Integer[] p6,
			Pageable pageable) {
		return dao.findPage(siteId, parentId, treeNumber, isRealNode, isHidden, p0, p1, p2, p3, p4, p5, p6, pageable);
	}

	public List<Node> findByIds(Integer... ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		return dao.findAll(Arrays.asList(ids));
	}

	public List<Node> findByIds(Integer[] ids, Integer selfId) {
		List<Node> list = new ArrayList<Node>();
		if (!ArrayUtils.isEmpty(ids)) {
			Set<Integer> idSet = new HashSet<Integer>();
			for (Integer id : ids) {
				if (!idSet.contains(id) && !id.equals(selfId)) {
					idSet.add(id);
					list.add(get(id));
				}
			}
		}
		list.add(get(selfId));
		return list;
	}

	public List<Node> findList(Integer siteId) {
		return findList(siteId, null, null, null);
	}

	public List<Node> findList(Integer siteId, Integer parentId) {
		return findList(siteId, parentId, null, null);
	}

	public List<Node> findList(Integer siteId, Integer parentId, Boolean isRealNode, Boolean isHidden) {
		String treeNumber = null;
		if (parentId != null) {
			Node node = get(parentId);
			if (node != null) {
				treeNumber = node.getTreeNumber();
			}
		}
		Sort sort = new Sort("treeNumber");
		Limitable limitable = new LimitRequest(null, null, sort);
		return dao.findList(new Integer[] { siteId }, null, treeNumber, isRealNode, isHidden, null, null,null, null, null,
				null, null, limitable);
	}

	public List<Node> findChildren(Integer parentId) {
		return findChildren(parentId, null, null, null, null);
	}

	public List<Node> findChildren(Integer parentId, Boolean isRealNode, Boolean isHidden, Integer offset, Integer limit) {
		if (parentId == null) {
			return Collections.emptyList();
		}
		Sort sort = new Sort("treeNumber");
		Limitable limitable = new LimitRequest(offset, limit, sort);
		return dao.findList(null, parentId, null, isRealNode, isHidden, null,null, null, null, null, null, null, limitable);
	}

	public Node findRoot(Integer siteId) {
		List<Node> list = dao.findBySiteIdAndParentIdIsNull(siteId);
		return !list.isEmpty() ? list.get(0) : null;
	}

	public Node findByNumber(Integer siteId, String number) {
		List<Node> list = dao.findBySiteIdAndNumber(siteId, number);
		return !list.isEmpty() ? list.get(0) : null;
	}

	public Node findByName(Integer siteId, String name) {
		List<Node> list = dao.findBySiteIdAndName(siteId, name);
		return !list.isEmpty() ? list.get(0) : null;
	}

	public List<Node> findByNumber(String[] numbers, Integer[] siteIds) {
		List<Node> list = dao.findByNumbers(numbers, siteIds);
		return list;
	}

	public List<Node> findByNumberLike(String[] numbers, Integer[] siteIds) {
		List<Node> list = dao.findByNumbersLike(numbers, siteIds);
		return list;
	}

	public Node get(Integer id) {
		return dao.findOne(id);
	}

	private NodeDao dao;

	@Autowired
	public void setDao(NodeDao dao) {
		this.dao = dao;
	}
}

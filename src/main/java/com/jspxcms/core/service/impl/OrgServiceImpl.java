package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Org;
import com.jspxcms.core.listener.OrgDeleteListener;
import com.jspxcms.core.repository.OrgDao;
import com.jspxcms.core.service.OrgService;

/**
 * OrgServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class OrgServiceImpl implements OrgService {
	public List<Org> findList(String topTreeNumber, Integer parentId,
			boolean showDescendants, Map<String, String[]> params, Sort sort) {
		String treeNumber = null;
		if (showDescendants) {
			if (parentId != null) {
				Org parent = dao.findOne(parentId);
				if (parent == null) {
					return Collections.emptyList();
				}
				treeNumber = parent.getTreeNumber();
			} else {
				treeNumber = "";
			}
		}
		return dao.findAll(spec(topTreeNumber, parentId, treeNumber, params),
				sort);
	}

	public RowSide<Org> findSide(String topTreeNumber, Integer parentId,
			boolean showDescendants, Map<String, String[]> params, Org bean,
			Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<Org>();
		}
		String treeNumber = null;
		if (showDescendants) {
			if (parentId != null) {
				Org parent = dao.findOne(parentId);
				if (parent == null) {
					return new RowSide<Org>();
				}
				treeNumber = parent.getTreeNumber();
			} else {
				treeNumber = "";
			}
			parentId = null;
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Org> list = dao.findAll(
				spec(topTreeNumber, parentId, treeNumber, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Org> spec(final String topTreeNumber,
			final Integer parentId, final String treeNumber,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Org> fs = SearchFilter.spec(filters, Org.class);
		Specification<Org> sp = new Specification<Org>() {
			public Predicate toPredicate(Root<Org> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fs.toPredicate(root, query, cb);
				if (StringUtils.isNotBlank(topTreeNumber)) {
					Path<String> tnPath = root.get("treeNumber");
					pred = cb.and(pred, cb.like(tnPath, topTreeNumber + "%"));
				}
				if (treeNumber != null) {
					Path<String> tnPath = root.get("treeNumber");
					pred = cb.and(pred, cb.like(tnPath, treeNumber + "%"));
				} else if (parentId != null) {
					Path<Integer> pidPath = root.get("parent").get("id");
					pred = cb.and(pred, cb.equal(pidPath, parentId));
				} else {
					pred = cb.and(pred, root.get("parent").isNull());
				}
				return pred;
			}
		};
		return sp;
	}

	public List<Org> findList() {
		return dao.findAll(new Sort("treeNumber"));
	}

	public List<Org> findList(String treeNumber) {
		if (StringUtils.isNotBlank(treeNumber)) {
			return dao.findByTreeNumberStartingWith(treeNumber, new Sort(
					"treeNumber"));
		} else {
			return dao.findAll(new Sort("treeNumber"));
		}
	}

	public Org findRoot() {
		List<Org> roots = dao.findByParentIdIsNull();
		if (roots.isEmpty()) {
			return null;
		} else {
			return roots.get(0);
		}
	}

	public Org get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Org save(Org bean, Integer parentId) {
		Org parent = null;
		if (parentId != null) {
			parent = dao.findOne(parentId);
			bean.setParent(parent);
			parent.addChild(bean);
		}
		bean.applyDefaultValue();
		treeSave(bean, parent);
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	private void treeSave(Org bean, Org parent) {
		bean.setTreeMax(Org.long2hex(0));
		if (parent == null) {
			String treeMax = dao.findMaxRootTreeNumber();
			long maxLong = Org.hex2long(treeMax);
			treeMax = Org.long2hex(maxLong + 1);
			bean.setTreeLevel(0);
			bean.setTreeNumber(treeMax);
			bean.setTreeMax(Org.long2hex(0));
		} else {
			bean.setTreeLevel(parent.getTreeLevel() + 1);
			String treeMax = parent.getTreeMax();
			bean.setTreeNumber(parent.getTreeNumber() + "-" + treeMax);
			long big = parent.getTreeMaxLong() + 1;
			parent.setTreeMax(Org.long2hex(big));
			dao.save(parent);
		}
	}

	@Transactional
	public Org update(Org bean, Integer parentId) {
		bean.applyDefaultValue();
		bean = dao.save(bean);

		Org parent = bean.getParent();
		if ((parent != null && !parent.getId().equals(parentId))
				|| (parent == null && parentId != null)) {
			move(new Integer[] { bean.getId() }, parentId);
		}

		return bean;
	}

	@Transactional
	public Org[] batchUpdate(Integer[] id, String[] name, String[] number,
			String[] phone, String[] address, boolean isUpdateTree) {
		Map<Integer, List<Integer>> listMap = new HashMap<Integer, List<Integer>>();
		Org[] beans = new Org[id.length];
		for (int i = 0, len = id.length; i < len; i++) {
			beans[i] = dao.findOne(id[i]);
			beans[i].setName(name[i]);
			beans[i].setNumber(number[i]);
			beans[i].setPhone(phone[i]);
			beans[i].setAddress(address[i]);
			dao.save(beans[i]);
			if (isUpdateTree) {
				Org parent = beans[i].getParent();
				Integer parentId;
				if (parent != null) {
					parentId = parent.getId();
				} else {
					parentId = -1;
				}
				List<Integer> list = listMap.get(parentId);
				if (list != null) {
					list.add(id[i]);
				} else {
					list = new ArrayList<Integer>();
					list.add(id[i]);
					listMap.put(parentId, list);
				}
			}
		}
		String parentTreeNumber, origTreeNumber, treeNumber;
		for (Entry<Integer, List<Integer>> entry : listMap.entrySet()) {
			Integer parentId = entry.getKey();
			List<Integer> ids = entry.getValue();
			if (parentId == -1) {
				int len = ids.size();
				if (dao.countRoot() != len) {
					continue;
				}
				dao.appendModifiedFlag("%");
				for (int i = 0; i < len; i++) {
					origTreeNumber = dao.findTreeNumber(ids.get(i));
					treeNumber = Org.long2hex(i);
					dao.updateTreeNumber(origTreeNumber + "%", treeNumber,
							treeNumber.length() + 2);
				}
			} else {
				parentTreeNumber = dao.findTreeNumber(parentId);
				dao.appendModifiedFlag(parentTreeNumber + "-%");
				int len = ids.size();
				if (dao.countByParentId(parentId) != len) {
					continue;
				}
				for (int i = 0; i < len; i++) {
					origTreeNumber = dao.findTreeNumber(ids.get(i));
					treeNumber = parentTreeNumber + "-" + Org.long2hex(i);
					dao.updateTreeNumber(origTreeNumber + "%", treeNumber,
							treeNumber.length() + 2);
				}
				// 修改父节点的treeMax
				dao.updateTreeMax(parentId, Org.long2hex(len));
			}
		}
		return beans;
	}

	@Transactional
	public int move(Integer[] ids, Integer id) {
		int count = 0;
		String modifiedTreeNumber, treeNumber;
		if (id == null) {
			long treeMax = Org.hex2long(dao.findMaxRootTreeNumber()) + 1;
			for (int i = 0, len = ids.length; i < len; i++) {
				treeNumber = dao.findTreeNumber(ids[i]);
				modifiedTreeNumber = Org.long2hex(treeMax++);
				count += dao.updateTreeNumber(treeNumber + "%",
						modifiedTreeNumber, treeNumber.length() + 1);
				dao.updateParentId(ids[i], id);
			}
		} else {
			Org parent = dao.findOne(id);
			String parentTreeNumber = parent.getTreeNumber();
			long treeMax = parent.getTreeMaxLong();
			for (int i = 0, len = ids.length; i < len; i++) {
				dao.updateTreeMax(id, Org.long2hex(treeMax + 1));
				treeNumber = dao.findTreeNumber(ids[i]);
				modifiedTreeNumber = parentTreeNumber + "-"
						+ Org.long2hex(treeMax++);
				count += dao.updateTreeNumber(treeNumber + "%",
						modifiedTreeNumber, treeNumber.length() + 1);
				dao.updateParentId(ids[i], id);
			}
		}
		return count;
	}

	private Org doDelete(Integer id) {
		Org entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}

	@Transactional
	public Org delete(Integer id) {
		firePreDelete(new Integer[] { id });
		return doDelete(id);
	}

	@Transactional
	public Org[] delete(Integer[] ids) {
		firePreDelete(ids);
		Org[] beans = new Org[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = doDelete(ids[i]);
		}
		return beans;
	}

	private void firePreDelete(Integer[] ids) {
		if (!CollectionUtils.isEmpty(deleteListeners)) {
			for (OrgDeleteListener listener : deleteListeners) {
				listener.preOrgDelete(ids);
			}
		}
	}

	private List<OrgDeleteListener> deleteListeners;

	@Autowired(required = false)
	public void setDeleteListeners(List<OrgDeleteListener> deleteListeners) {
		this.deleteListeners = deleteListeners;
	}

	private OrgDao dao;

	@Autowired
	public void setDao(OrgDao dao) {
		this.dao = dao;
	}
}

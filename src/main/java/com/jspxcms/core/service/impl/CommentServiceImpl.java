package com.jspxcms.core.service.impl;

import com.jspxcms.common.ip.IPSeeker;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Comment;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.CommentDao;
import com.jspxcms.core.service.CommentService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.UserService;
import com.jspxcms.core.support.Commentable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;

/**
 * CommentServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService, SiteDeleteListener, UserDeleteListener {
	public Page<Comment> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(siteId, params), pageable);
	}

	public RowSide<Comment> findSide(Integer siteId, Map<String, String[]> params, Comment bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<Comment>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Comment> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Comment> spec(final Integer siteId, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Comment> fsp = SearchFilter.spec(filters, Comment.class);
		Specification<Comment> sp = new Specification<Comment>() {
			public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site").get("id"), siteId));
				}
				return pred;
			}
		};
		return sp;
	}

	public List<Comment> findList(String ftype, Integer fid, Integer creatorId, Integer[] status, Integer[] siteId,
			Limitable limitable) {
		return dao.findList(ftype, fid, creatorId, status, siteId, limitable);
	}

	public Page<Comment> findPage(String ftype, Integer fid, Integer creatorId, Integer[] status, Integer[] siteId,
			Pageable pageable) {
		return dao.findPage(ftype, fid, creatorId, status, siteId, pageable);
	}

	public long countByDate(Integer siteId, Date beginDate) {
		return dao.countByDate(siteId, beginDate);
	}

	public Object getEntity(String entityName, Serializable id) {
		return dao.getEntity(entityName, id);
	}

	public Comment get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Comment save(Comment bean, Integer userId, Integer siteId, Integer parentId) {
		Site site = siteService.get(siteId);
		bean.setSite(site);
		User user = userService.get(userId);
		bean.setCreator(user);
		if (parentId != null) {
			Comment parent = get(parentId);
			bean.setParent(parent);
		}
		if (StringUtils.isNotBlank(bean.getIp())) {
			bean.setCountry(ipSeeker.getCountry(bean.getIp()));
			bean.setArea(ipSeeker.getArea(bean.getIp()));
		}

		bean.applyDefaultValue();
		bean = dao.save(bean);
		dao.flushAndRefresh(bean);
		if (bean.getStatus() == Comment.AUDITED) {
			Object anchor = bean.getAnchor();
			if (anchor instanceof Commentable) {
				((Commentable) anchor).addComments(1);
			}
		}
		return bean;
	}

	@Transactional
	public Comment update(Comment bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Comment delete(Integer id) {
		Comment entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public Comment[] delete(Integer[] ids) {
		Comment[] beans = new Comment[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	@Transactional
	public int deleteByFtypeAndFid(String ftype, Integer fid) {
        dao.clearParentByFtypeAndFid(ftype, fid);
        return dao.deleteByFtypeAndFid(ftype, fid);
	}

	@Transactional
	public Comment[] audit(Integer[] ids) {
		Comment[] beans = new Comment[ids.length];
		Comment bean;
		for (int i = 0; i < ids.length; i++) {
			bean = get(ids[i]);
			beans[i] = bean;
			if (bean.getStatus() == Comment.SAVED) {
				bean.setStatus(Comment.AUDITED);
				Object anchor = bean.getAnchor();
				if (anchor instanceof Commentable) {
					((Commentable) anchor).addComments(1);
				}
			}
		}
		return beans;
	}

	@Transactional
	public Comment[] unaudit(Integer[] ids) {
		Comment[] beans = new Comment[ids.length];
		Comment bean;
		for (int i = 0; i < ids.length; i++) {
			bean = get(ids[i]);
			beans[i] = get(ids[i]);
			if (bean.getStatus() == Comment.AUDITED) {
				bean.setStatus(Comment.SAVED);
				Object anchor = bean.getAnchor();
				if (anchor instanceof Commentable) {
					((Commentable) anchor).addComments(-1);
				}
			}
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		dao.deleteBySiteId(Arrays.asList(ids));
	}

	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		dao.deleteByCreatorId(Arrays.asList(ids));
		dao.deleteByAuditorId(Arrays.asList(ids));
	}

	private IPSeeker ipSeeker;
	private UserService userService;
	private SiteService siteService;

	@Autowired
	public void setIpSeeker(IPSeeker ipSeeker) {
		this.ipSeeker = ipSeeker;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private CommentDao dao;

	@Autowired
	public void setDao(CommentDao dao) {
		this.dao = dao;
	}
}

package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.file.FileHandler;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.common.web.PathResolver;
import com.jspxcms.core.domain.Attachment;
import com.jspxcms.core.domain.AttachmentRef;
import com.jspxcms.core.domain.PublishPoint;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.AttachmentDao;
import com.jspxcms.core.service.AttachmentRefService;
import com.jspxcms.core.service.AttachmentService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.UserService;

@Service
@Transactional(readOnly = true)
public class AttachmentServiceImpl implements AttachmentService,
		UserDeleteListener, SiteDeleteListener {
	public Page<Attachment> findAll(Integer siteId, boolean notUsed,
			Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(siteId, notUsed, params), pageable);
	}

	public RowSide<Attachment> findSide(Integer siteId, boolean notUsed,
			Map<String, String[]> params, Attachment bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<Attachment>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Attachment> list = dao.findAll(spec(siteId, notUsed, params),
				limit);
		return RowSide.create(list, bean);
	}

	private Specification<Attachment> spec(final Integer siteId,
			final boolean notUsed, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Attachment> fsp = SearchFilter.spec(filters,
				Attachment.class);
		Specification<Attachment> sp = new Specification<Attachment>() {
			public Predicate toPredicate(Root<Attachment> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (siteId != null) {
					pred = cb.and(pred, cb.equal(root.get("site")
							.<Integer> get("id"), siteId));
				}
				if (notUsed) {
					Join<Attachment, AttachmentRef> refJoin = root.join("refs",
							JoinType.LEFT);
					pred = cb.and(pred, refJoin.get("id").isNull());
				}
				return pred;
			}
		};
		return sp;
	}

	public Attachment findByName(String name) {
		List<Attachment> list = dao.findByName(name);
		if (!list.isEmpty()) {
			return list.iterator().next();
		} else {
			return null;
		}
	}

	public Attachment get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public Attachment save(String name, Long length, String ip, Integer userId,
			Integer siteId) {
		Attachment bean = findByName(name);
		if (bean != null) {
			// 重名的，不重复保存。
			return bean;
		}
		bean = new Attachment();
		bean.setName(name);
		bean.setLength(length);
		bean.setIp(ip);
		save(bean, userId, siteId);
		return bean;
	}

	@Transactional
	public Attachment save(Attachment bean, Integer userId, Integer siteId) {
		User user = userService.get(userId);
		bean.setUser(user);
		Site site = siteService.get(siteId);
		bean.setSite(site);
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Attachment update(Attachment bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Attachment delete(Integer id) {
		Attachment bean = dao.findOne(id);
		PublishPoint point = bean.getSite().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		dao.delete(bean);
		fileHandler.delete(bean.getName());
		return bean;
	}

	@Transactional
	public List<Attachment> delete(Integer[] ids) {
		List<Attachment> beans = new ArrayList<Attachment>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
		}
		return beans;
	}

	public void preSiteDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		List<Integer> list = Arrays.asList(ids);
		attachmentRefService.deleteBySiteId(list);
		dao.deleteBySiteId(list);
	}

	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}
		// 删除用户时，将附件上传人设置为匿名用户。
		dao.toAnonymous(Arrays.asList(ids));
	}

	private PathResolver pathResolver;
	private AttachmentRefService attachmentRefService;
	private UserService userService;
	private SiteService siteService;

	@Autowired
	public void setPathResolver(PathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}

	@Autowired
	public void setAttachmentRefService(
			AttachmentRefService attachmentRefService) {
		this.attachmentRefService = attachmentRefService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	private AttachmentDao dao;

	@Autowired
	public void setDao(AttachmentDao dao) {
		this.dao = dao;
	}
}

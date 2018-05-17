package com.jspxcms.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.ip.IPSeeker;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.OperationLog;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.core.repository.OperationLogDao;
import com.jspxcms.core.service.GlobalService;
import com.jspxcms.core.service.OperationLogService;
import com.jspxcms.core.service.SiteShiroService;
import com.jspxcms.core.service.UserShiroService;
import com.jspxcms.core.support.Context;

@Service
@Transactional(readOnly = true)
public class OperationLogServiceImpl implements OperationLogService, UserDeleteListener, SiteDeleteListener {
	public Page<OperationLog> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(siteId, params), pageable);
	}

	public RowSide<OperationLog> findSide(Integer siteId, Map<String, String[]> params, OperationLog bean,
			Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<OperationLog>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<OperationLog> list = dao.findAll(spec(siteId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<OperationLog> spec(final Integer siteId, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<OperationLog> fsp = SearchFilter.spec(filters, OperationLog.class);
		Specification<OperationLog> sp = new Specification<OperationLog>() {
			public Predicate toPredicate(Root<OperationLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<Integer> siteIdPath = root.get("site").<Integer>get("id");
				Predicate siteIdPred = cb.equal(siteIdPath, siteId);
				return cb.and(fsp.toPredicate(root, query, cb), siteIdPred);
			}
		};
		return sp;
	}

	public OperationLog get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public OperationLog operation(String name, String description, String text, Integer dataId, String ip,
			Integer userId, Integer siteId) {
		OperationLog bean = new OperationLog();
		bean.setName(name);
		bean.setDescription(description);
		bean.setText(text);
		bean.setIp(ip);
		bean.setDataId(dataId);
		bean.setType(OperationLog.TYPE_OPERATION);
		save(bean, userId, siteId);
		return bean;
	}

	@Transactional
	public OperationLog operation(String name, String description, String text, Integer dataId,
			HttpServletRequest request) {
		Integer siteId = Context.getCurrentSiteId();
		Integer userId = Context.getCurrentUserId();
		String ip = Servlets.getRemoteAddr(request);
		return operation(name, description, text, dataId, ip, userId, siteId);
	}

	@Transactional
	public OperationLog loginSuccess(String ip, Integer userId) {
		OperationLog bean = new OperationLog();
		bean.setName("login.success");
		bean.setIp(ip);
		bean.setType(OperationLog.TYPE_LOGIN);
		save(bean, userId, null);
		return bean;
	}

	@Transactional
	public OperationLog loginFailure(String description, String ip) {
		OperationLog bean = new OperationLog();
		bean.setName("login.failure");
		bean.setDescription(description);
		bean.setIp(ip);
		bean.setType(OperationLog.TYPE_LOGIN);
		save(bean, null, null);
		return bean;
	}

	@Transactional
	public OperationLog logout(String ip, String username) {
		OperationLog bean = new OperationLog();
		bean.setName("login.logout");
		bean.setIp(ip);
		bean.setType(OperationLog.TYPE_LOGIN);
		Integer userId = null;
		User user = userShiroService.findByUsername(username);
		if (user != null) {
			userId = user.getId();
		}
		save(bean, userId, null);
		return bean;
	}

	@Transactional
	public OperationLog save(OperationLog bean, Integer userId, Integer siteId) {
		if (userId != null) {
			bean.setUser(userShiroService.get(userId));
		} else {
			bean.setUser(userShiroService.getAnonymous());
		}
		Site site = null;
		if (siteId != null) {
			site = siteShiroService.get(siteId);
		}
		if (site == null) {
			site = globalService.findUnique().getSite();
		}
		bean.setSite(site);		
		if (StringUtils.isNotBlank(bean.getIp())) {
			bean.setCountry(ipSeeker.getCountry(bean.getIp()));
			bean.setArea(ipSeeker.getArea(bean.getIp()));
		}
		bean.applyDefaultValue();
		dao.save(bean);
		return bean;
	}

	@Transactional
	public OperationLog delete(Integer id) {
		OperationLog bean = dao.findOne(id);
		dao.delete(bean);
		return bean;
	}

	@Transactional
	public List<OperationLog> delete(Integer[] ids) {
		List<OperationLog> beans = new ArrayList<OperationLog>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
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
		dao.deleteByUserId(Arrays.asList(ids));
	}

	private UserShiroService userShiroService;
	private SiteShiroService siteShiroService;
	private GlobalService globalService;

	@Autowired
	public void setUserShiroService(UserShiroService userShiroService) {
		this.userShiroService = userShiroService;
	}

	@Autowired
	public void setSiteShiroService(SiteShiroService siteShiroService) {
		this.siteShiroService = siteShiroService;
	}

	@Autowired
	public void setGlobalService(GlobalService globalService) {
		this.globalService = globalService;
	}

	private IPSeeker ipSeeker;

	@Autowired
	public void setIpSeeker(IPSeeker ipSeeker) {
		this.ipSeeker = ipSeeker;
	}

	private OperationLogDao dao;

	@Autowired
	public void setDao(OperationLogDao dao) {
		this.dao = dao;
	}
}

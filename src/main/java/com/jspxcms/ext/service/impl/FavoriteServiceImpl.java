package com.jspxcms.ext.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.listener.UserDeleteListener;
import com.jspxcms.ext.domain.Favorite;
import com.jspxcms.ext.domain.InfoFavorite;
import com.jspxcms.ext.repository.FavoriteDao;
import com.jspxcms.ext.service.FavoriteService;

@Service
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService, UserDeleteListener {
	public Page<Favorite> findAll(Integer userId, Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(userId, params), pageable);
	}

	public List<Favorite> findAll(Integer userId, Map<String, String[]> params, Limitable limitable) {
		return dao.findAll(spec(userId, params), limitable);
	}

	public RowSide<Favorite> findSide(Integer userId, Map<String, String[]> params, Favorite bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<Favorite>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<Favorite> list = dao.findAll(spec(userId, params), limit);
		return RowSide.create(list, bean);
	}

	private Specification<Favorite> spec(final Integer userId, Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<Favorite> fsp = SearchFilter.spec(filters, Favorite.class);
		Specification<Favorite> sp = new Specification<Favorite>() {
			public Predicate toPredicate(Root<Favorite> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (userId != null) {
					pred = cb.and(pred, cb.equal(root.get("user").<Integer> get("id"), userId));
				}
				return pred;
			}
		};
		return sp;
	}

	public Favorite get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public InfoFavorite infoFavorite(Info info, User user) {
		InfoFavorite bean = dao.findByInfoIdAndUserId(info.getId(), user.getId());
		if (bean == null) {
			bean = new InfoFavorite();
			bean.setInfo(info);
			bean.setUser(user);
			save(bean);
			info.setFavorites(info.getFavorites() + 1);
		}
		return bean;
	}

	@Transactional
	public InfoFavorite infoUnfavorite(Info info, User user) {
		InfoFavorite bean = dao.findByInfoIdAndUserId(info.getId(), user.getId());
		if (bean != null) {
			delete(bean.getId());
			info.setFavorites(info.getFavorites() - 1);
		}
		return bean;
	}

	@Transactional
	public Favorite save(Favorite bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Favorite update(Favorite bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public Favorite delete(Integer id) {
		Favorite bean = dao.findOne(id);
		dao.delete(bean);
		return bean;
	}

	@Transactional
	public List<Favorite> delete(Integer[] ids) {
		List<Favorite> beans = new ArrayList<Favorite>(ids.length);
		for (Integer id : ids) {
			beans.add(delete(id));
		}
		return beans;
	}

	@Override
	public void preUserDelete(Integer[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			dao.deleteByUserId(Arrays.asList(ids));
		}
	}

	private FavoriteDao dao;

	@Autowired
	public void setDao(FavoriteDao dao) {
		this.dao = dao;
	}
}

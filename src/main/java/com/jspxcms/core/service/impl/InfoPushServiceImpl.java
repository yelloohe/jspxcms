package com.jspxcms.core.service.impl;

import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.InfoPush;
import com.jspxcms.core.listener.InfoDeleteListener;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.repository.InfoPushDao;
import com.jspxcms.core.service.InfoPushService;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * InfoPushServiceImpl
 *
 * @author liufang
 */
@Service
@Transactional(readOnly = true)
public class InfoPushServiceImpl implements InfoPushService, SiteDeleteListener, InfoDeleteListener {
    /**
     * @see InfoPushService#findAll(Integer, Map, Pageable)
     */
    public Page<InfoPush> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable) {
        return dao.findAll(spec(siteId, params), pageable);
    }

    private Specification<InfoPush> spec(final Integer siteId, Map<String, String[]> params) {
        Collection<SearchFilter> filters = SearchFilter.parse(params).values();
        final Specification<InfoPush> fsp = SearchFilter.spec(filters, InfoPush.class);
        Specification<InfoPush> sp = new Specification<InfoPush>() {
            public Predicate toPredicate(Root<InfoPush> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate pred = fsp.toPredicate(root, query, cb);
                if (siteId != null) {
                    pred = cb.and(pred, cb.equal(root.get("fromSite").get("id"), siteId));
                }
                return pred;
            }
        };
        return sp;
    }

    public InfoPush get(Integer id) {
        return dao.findOne(id);
    }

    /**
     * @see InfoPushService#save(Integer, Integer, Integer, Integer)
     */
    @Transactional
    public InfoPush save(Integer infoId, Integer fromSiteId, Integer toSiteId, Integer userId) {
        InfoPush bean = new InfoPush();
        bean.setInfo(infoQuery.get(infoId));
        bean.setFromSite(siteService.get(fromSiteId));
        bean.setToSite(siteService.get(toSiteId));
        bean.setUser(userService.get(userId));
        bean.applyDefaultValue();
        dao.save(bean);
        return bean;
    }

    @Override
    public void preInfoDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            dao.deleteByInfoId(Arrays.asList(ids));
        }
    }

    @Override
    public void preSiteDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            dao.deleteBySiteId(Arrays.asList(ids));
        }
    }

    @Autowired
    private InfoQueryService infoQuery;
    @Autowired
    private SiteService siteService;
    @Autowired
    private UserService userService;
    @Autowired
    private InfoPushDao dao;
}

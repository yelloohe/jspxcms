package com.jspxcms.ext.service.impl;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.listener.SiteDeleteListener;
import com.jspxcms.core.service.SiteService;
import com.jspxcms.core.service.VoteMarkService;
import com.jspxcms.core.support.DeleteException;
import com.jspxcms.ext.domain.Vote;
import com.jspxcms.ext.domain.VoteOption;
import com.jspxcms.ext.repository.VoteDao;
import com.jspxcms.ext.service.VoteOptionService;
import com.jspxcms.ext.service.VoteService;
import org.apache.commons.lang3.ArrayUtils;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class VoteServiceImpl implements VoteService, SiteDeleteListener {
    public Page<Vote> findAll(Integer siteId, Map<String, String[]> params, Pageable pageable) {
        return dao.findAll(spec(siteId, params), pageable);
    }

    public RowSide<Vote> findSide(Integer siteId, Map<String, String[]> params, Vote bean, Integer position, Sort sort) {
        if (position == null) {
            return new RowSide<Vote>();
        }
        Limitable limit = RowSide.limitable(position, sort);
        List<Vote> list = dao.findAll(spec(siteId, params), limit);
        return RowSide.create(list, bean);
    }

    private Specification<Vote> spec(final Integer siteId, Map<String, String[]> params) {
        Collection<SearchFilter> filters = SearchFilter.parse(params).values();
        final Specification<Vote> fsp = SearchFilter.spec(filters, Vote.class);
        Specification<Vote> sp = new Specification<Vote>() {
            public Predicate toPredicate(Root<Vote> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate pred = fsp.toPredicate(root, query, cb);
                if (siteId != null) {
                    pred = cb.and(pred, cb.equal(root.get("site").<Integer>get("id"), siteId));
                }
                return pred;
            }
        };
        return sp;
    }

    public List<Vote> findList(String[] number,Boolean inPeriod, Integer[] status, Integer[] siteId, Limitable limitable) {
        return dao.findList(number,inPeriod, status, siteId, limitable);
    }

    public Page<Vote> findPage(String[] number,Boolean inPeriod, Integer[] status, Integer[] siteId, Pageable pageable) {
        return dao.findPage(number, inPeriod, status, siteId, pageable);
    }

    public boolean numberExist(String number, Integer siteId) {
        return dao.countByNumber(number, siteId) > 0;
    }

    public Vote findByNumber(String number, Integer[] status, Integer siteId) {
        return dao.findByNumber(number, status, siteId);
    }

    public Vote findLatest(Integer[] status, Integer siteId) {
        return dao.findLatest(status, siteId);
    }

    public Vote get(Integer id) {
        return dao.findOne(id);
    }

    @Transactional
    public Vote save(Vote bean, String[] title, Integer[] count, Integer siteId) {
        Site site = siteService.get(siteId);
        bean.setSite(site);
        bean.applyDefaultValue();
        bean = dao.save(bean);

        voteOptionService.save(title, count, bean);
        return bean;
    }

    @Transactional
    public Vote update(Vote bean, Integer[] id, String[] title, Integer[] count) {
        bean.applyDefaultValue();
        bean = dao.save(bean);

        voteOptionService.update(id, title, count, bean);
        return bean;
    }

    @Transactional
    public Vote delete(Integer id) {
        Vote entity = dao.findOne(id);
        if (entity != null) {
            voteMarkService.unmark(Vote.MARK_CODE, id);
            dao.delete(entity);
        }
        return entity;
    }

    @Transactional
    public Vote[] delete(Integer[] ids) {
        Vote[] beans = new Vote[ids.length];
        for (int i = 0; i < ids.length; i++) {
            beans[i] = delete(ids[i]);
        }
        return beans;
    }

    @Transactional
    public Vote vote(Integer id, Integer[] optionIds, Integer userId,
                     String ip, String cookie) {
        Vote vote = get(id);
        int total = 0;
        for (VoteOption option : vote.getOptions()) {
            if (ArrayUtils.contains(optionIds, option.getId())) {
                option.setCount(option.getCount() + 1);
            }
            total += option.getCount();
        }
        vote.setTotal(total);
        voteMarkService.mark(Vote.MARK_CODE, id, userId, ip, cookie);
        return vote;
    }

    public void preSiteDelete(Integer[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            if (dao.countBySiteId(Arrays.asList(ids)) > 0) {
                throw new DeleteException("vote.management");
            }
        }
    }

    private VoteMarkService voteMarkService;
    private VoteOptionService voteOptionService;
    private SiteService siteService;

    @Autowired
    public void setVoteMarkService(VoteMarkService voteMarkService) {
        this.voteMarkService = voteMarkService;
    }

    @Autowired
    public void setVoteOptionService(VoteOptionService voteOptionService) {
        this.voteOptionService = voteOptionService;
    }

    @Autowired
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    private VoteDao dao;

    @Autowired
    public void setDao(VoteDao dao) {
        this.dao = dao;
    }
}

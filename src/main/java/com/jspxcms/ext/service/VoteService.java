package com.jspxcms.ext.service;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.ext.domain.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * 投票Service
 */
public interface VoteService {
    public Page<Vote> findAll(Integer siteId, Map<String, String[]> params,
                              Pageable pageable);

    public RowSide<Vote> findSide(Integer siteId, Map<String, String[]> params,
                                  Vote bean, Integer position, Sort sort);

    public List<Vote> findList(String[] number, Boolean inPeriod, Integer[] status, Integer[] siteId, Limitable limitable);

    public Page<Vote> findPage(String[] number, Boolean inPeriod, Integer[] status, Integer[] siteId, Pageable pageable);

    public boolean numberExist(String number, Integer siteId);

    public Vote findByNumber(String number, Integer[] status, Integer siteId);

    public Vote findLatest(Integer[] status, Integer siteId);

    public Vote get(Integer id);

    public Vote vote(Integer id, Integer[] optionIds, Integer userId,
                     String ip, String cookie);

    public Vote save(Vote bean, String[] title, Integer[] count, Integer siteId);

    public Vote update(Vote bean, Integer[] id, String[] title, Integer[] count);

    public Vote delete(Integer id);

    public Vote[] delete(Integer[] ids);

}

package com.jspxcms.core.repository;

import com.jspxcms.core.domain.InfoPush;
import com.jspxcms.core.repository.plus.InfoPushDaoPlus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;

/**
 * InfoPushDao
 *
 * @author liufang
 */
public interface InfoPushDao extends Repository<InfoPush, Integer>, InfoPushDaoPlus {
    public List<InfoPush> findAll(Specification<InfoPush> spec, Sort sort);

    public Page<InfoPush> findAll(Specification<InfoPush> spec, Pageable pageable);

    public InfoPush findOne(Integer id);

    public InfoPush save(InfoPush bean);

    public void delete(InfoPush bean);

    // --------------------

    @Modifying
    @Query("delete from InfoPush bean where bean.fromSite.id in (?1) or bean.toSite.id in (?1)")
    public int deleteBySiteId(Collection<Integer> siteIds);

    @Modifying
    @Query("delete from InfoPush bean where bean.info.id in (?1)")
    public int deleteByInfoId(Collection<Integer> infoIds);
}

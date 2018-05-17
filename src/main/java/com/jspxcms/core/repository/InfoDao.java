package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.repository.plus.InfoDaoPlus;

/**
 * InfoDao
 *
 * @author liufang
 */
public interface InfoDao extends Repository<Info, Integer>, InfoDaoPlus {
    public Page<Info> findAll(Specification<Info> spec, Pageable pageable);

    public List<Info> findAll(Specification<Info> spec, Limitable limitable);

    public List<Info> findAll(Iterable<Integer> ids);

    public Info findOne(Integer id);

    public Info save(Info bean);

    public void delete(Info bean);

    public void refresh(Info entity);

    public List<Info> findByNodeIdIn(Collection<Integer> nodeIds);

    public List<Info> findByCreatorIdIn(Collection<Integer> userIds);

    public List<Info> findByOrgIdIn(Collection<Integer> orgIds);

    public List<Info> findBySiteIdIn(Collection<Integer> siteIds);

    // --------------------

    /**
     * 通过站点ID获取文档列表
     *
     * @param siteId 站点ID
     * @return 文档列表
     */
    public List<Info> findBySiteId(Integer siteId);

    /**
     * 查询文档的发布数量
     *
     * @param siteId    站点ID
     * @param beginDate 起始时间
     * @return
     */
    @Query("select count(*) from Info bean where bean.site.id=?1 and bean.status='A' and bean.publishDate>=?2")
    public long countByDate(Integer siteId, Date beginDate);

    @Modifying
    @Query("update Info bean set bean.status='A' where bean.site.id=?1 and (bean.status='F' or bean.status='G') and bean.publishDate<=?2 and (bean.offDate is null or bean.offDate>=?2)")
    public int publish(Integer siteId, Date now);

    @Modifying
    @Query("update Info bean set bean.status='F' where bean.site.id=?1 and bean.status='A' and bean.publishDate>?2")
    public int tobePublish(Integer siteId, Date now);

    @Modifying
    @Query("update Info bean set bean.status='G' where bean.site.id=?1 and bean.status='A' and bean.offDate<?2)")
    public int expired(Integer siteId, Date now);

    @Query("from Info bean where bean.detail.title = ?1 and bean.site.id = ?2")
    public List<Info> findByTitle(String title, Integer siteId);

    @Modifying
    @Query("update Info bean set bean.node.id=?2 where bean.node.id in (?1)")
    public int moveByNodeId(Collection<Integer> nodeIds, Integer nodeId);

    @Query("select count(*) from Info bean where bean.creator.id in (?1)")
    public long countByUserId(Collection<Integer> userIds);

    @Query("select count(*) from Info bean where bean.node.id in (?1) and bean.status!='" + Info.DELETED + "'")
    public long countByNodeIdNotDeleted(Collection<Integer> nodeIds);

    @Query("select count(*) from Info bean where bean.org.id in (?1) and bean.status!='" + Info.DELETED + "'")
    public long countByOrgIdNotDeleted(Collection<Integer> orgIds);

    @Query("select count(*) from Info bean where (bean.site.id in (?1) or bean.fromSite.id in (?1)) and bean.status!='" + Info.DELETED + "'")
    public long countBySiteIdNotDeleted(Collection<Integer> siteIds);

}

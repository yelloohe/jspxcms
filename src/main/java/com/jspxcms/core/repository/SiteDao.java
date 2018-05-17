package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.repository.plus.SiteDaoPlus;

/**
 * SiteDao
 *
 * @author liufang
 */
public interface SiteDao extends Repository<Site, Integer>, SiteDaoPlus {

    public List<Site> findAll(Specification<Site> spec, Sort sort);

    public List<Site> findAll(Specification<Site> spec, Limitable limit);

    public List<Site> findAll(Sort sort);

    public Site findOne(Integer id);

    public Site save(Site bean);

    public void delete(Site bean);

    // --------------------

    public List<Site> findByTreeNumberStartingWith(String treeNumber);

    @Query("select count(*) from Site bean where bean.number = ?1")
    public long countByNumber(String number);

    @QueryHints(@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"))
    @Query("from Site bean where (bean.domain = ?1 or bean.mobileDomain = ?1) and bean.identifyDomain = 1 order by bean.treeNumber")
    public List<Site> findByDomain(String domain);

    @QueryHints(@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"))
    @Query("from Site bean where bean.number = ?1")
    public List<Site> findByNumber(String number);

    @QueryHints(@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true"))
    @Query("select distinct bean from Site bean join bean.roles role join role.userRoles userRoles where userRoles.user.id = ?1")
    public List<Site> findByUserId(Integer userId);

    @Query("select count(*) from Site bean where bean.org.id in ?1")
    public long countByOrgId(Collection<Integer> orgIds);

    @Query("select max(bean.treeNumber) from Site bean where bean.parent.id is null")
    public String findMaxRootTreeNumber();

    @Query("select count(*) from Site bean where bean.parent.id = ?1")
    public long countByParentId(Integer parentId);

    @Query("select count(*) from Site bean where bean.parent.id is null")
    public long countRoot();

    @Query("select bean.treeNumber from Site bean where bean.id = ?1")
    public String findTreeNumber(Integer id);

    @Modifying
    @Query("update Site bean set bean.treeNumber=CONCAT('*',bean.treeNumber) where bean.treeNumber like ?1")
    public int appendModifiedFlag(String treeNumberStart);

    @Modifying
    @Query("update Site bean set bean.treeLevel=(LENGTH(CONCAT(?2,SUBSTRING(bean.treeNumber,?3)))-4)/5, bean.treeNumber=CONCAT(?2,SUBSTRING(bean.treeNumber,?3)) where bean.treeNumber like ?1")
    public int updateTreeNumber(String treeNumber, String value, int len);

    @Modifying
    @Query("update Site bean set bean.parent.id = ?2 where bean.id = ?1")
    public int updateParentId(Integer id, Integer parentId);

    @Modifying
    @Query("update Site bean set bean.treeMax = ?2 where bean.id = ?1")
    public int updateTreeMax(Integer id, String treeMax);
}
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
import com.jspxcms.core.domain.Comment;
import com.jspxcms.core.repository.plus.CommentDaoPlus;

/**
 * CommentDao
 *
 * @author liufang
 */
public interface CommentDao extends Repository<Comment, Integer>,
        CommentDaoPlus {
    Page<Comment> findAll(Specification<Comment> spec, Pageable pageable);

    List<Comment> findAll(Specification<Comment> spec,
                          Limitable limitable);

    Comment findOne(Integer id);

    Comment save(Comment bean);

    void delete(Comment bean);

    // --------------------

    /**
     * 查询评论数量
     *
     * @param siteId    站点ID
     * @param beginDate 起始时间
     * @return 数据数量
     */
    @Query("select count(*) from Comment bean where bean.site.id=?1 and bean.creationDate>=?2")
    long countByDate(Integer siteId, Date beginDate);

    @Modifying
    @Query("update Comment bean set bean.parent = null where bean.ftype=?1 and bean.fid=?2")
    int clearParentByFtypeAndFid(String ftype, Integer fid);

    @Modifying
    @Query("delete from Comment bean where bean.ftype=?1 and bean.fid=?2")
    int deleteByFtypeAndFid(String ftype, Integer fid);

    @Modifying
    @Query("delete from Comment bean where bean.site.id in (?1)")
    int deleteBySiteId(Collection<Integer> siteIds);

    @Modifying
    @Query("delete from Comment bean where bean.creator.id in (?1)")
    int deleteByCreatorId(Collection<Integer> creatorIds);

    @Modifying
    @Query("delete from Comment bean where bean.auditor.id in (?1)")
    int deleteByAuditorId(Collection<Integer> auditorIds);
}

package com.jspxcms.core.repository.plus;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Comment;

/**
 * CommentDaoPlus
 * 
 * @author liufang
 * 
 */
public interface CommentDaoPlus {
	public List<Comment> findList(String ftype, Integer fid, Integer creatorId,
			Integer[] status, Integer[] siteId, Limitable limitable);

	public Page<Comment> findPage(String ftype, Integer fid, Integer creatorId,
			Integer[] status, Integer[] siteId, Pageable pageable);

	public Object getEntity(String entityName, Serializable id);

	public void flushAndRefresh(Comment entity);

}

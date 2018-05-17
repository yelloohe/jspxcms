package com.jspxcms.ext.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.ext.domain.Favorite;
import com.jspxcms.ext.domain.InfoFavorite;
import com.jspxcms.ext.repository.plus.FavoriteDaoPlus;

public interface FavoriteDao extends Repository<Favorite, Integer>, FavoriteDaoPlus {
	public Page<Favorite> findAll(Specification<Favorite> spec, Pageable pageable);

	public List<Favorite> findAll(Specification<Favorite> spec, Limitable limitable);

	public Favorite findOne(Integer id);

	public Favorite save(Favorite bean);

	public void delete(Favorite bean);

	// --------------------

	@Query("from InfoFavorite bean where bean.info.id=?1 and bean.user.id=?2")
	public InfoFavorite findByInfoIdAndUserId(Integer infoId, Integer userId);

	@Modifying
	@Query("delete from Favorite bean where bean.user.id in (?1)")
	public int deleteByUserId(Collection<Integer> userIds);
}

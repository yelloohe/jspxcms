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
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.repository.plus.NodeDaoPlus;

/**
 * NodeDao
 * 
 * @author liufang
 * 
 */
public interface NodeDao extends Repository<Node, Integer>, NodeDaoPlus {
	public List<Node> findAll(Specification<Node> spec, Sort sort);

	public List<Node> findAll(Specification<Node> spec, Limitable limit);

	public List<Node> findAll(Iterable<Integer> ids);

	public Node findOne(Integer id);

	public Node save(Node bean);

	public void delete(Node bean);

	public void refresh(Node entity);

	// --------------------

	public List<Node> findBySiteId(Integer siteId);

	public List<Node> findByInfoModelId(Integer modelId);

	public List<Node> findByNodeModelId(Integer modelId);

	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
	public List<Node> findBySiteIdAndParentIdIsNull(Integer siteId);

	@QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true") })
	public List<Node> findBySiteIdAndNumber(Integer siteId, String number);

	public List<Node> findBySiteIdAndName(Integer siteId, String name);

	@Query("select count(*) from Node bean where bean.parent.id = ?1")
	public long countByParentId(Integer parentId);

	@Query("select count(*) from Node bean where bean.site.id = ?1 and bean.parent.id is null")
	public long countRoot(Integer siteId);

	@Query("select bean.treeNumber from Node bean where bean.id = ?1")
	public String findTreeNumber(Integer id);

	@Modifying
	@Query("update Node bean set bean.treeNumber=CONCAT('*',bean.treeNumber) where bean.treeNumber like ?1 and bean.site.id = ?2")
	public int appendModifiedFlag(String treeNumberStart, Integer siteId);

	@Modifying
	@Query("update Node bean set bean.treeLevel=(LENGTH(CONCAT(?2,SUBSTRING(bean.treeNumber,?3)))-4)/5, bean.treeNumber=CONCAT(?2,SUBSTRING(bean.treeNumber,?3)) where bean.treeNumber like ?1 and bean.site.id = ?4")
	public int updateTreeNumber(String treeNumber, String value, int len, Integer siteId);

	@Modifying
	@Query("update Node bean set bean.parent.id = ?2 where bean.id = ?1")
	public int updateParentId(Integer id, Integer parentId);

	@Modifying
	@Query("update Node bean set bean.treeMax = ?2 where bean.id = ?1")
	public int updateTreeMax(Integer id, String treeMax);

	@Query("select count(*) from Node bean where bean.site.id in (?1)")
	public long countBySiteId(Collection<Integer> siteIds);

	@Query("select count(*) from Node bean where bean.creator.id in (?1)")
	public long countByCreatorId(Collection<Integer> creatorIds);

	@Query("select count(*) from Node bean where bean.nodeModel.id in (?1)")
	public long countByNodeModelId(Collection<Integer> nodeModelIds);

	@Query("select count(*) from Node bean where bean.infoModel.id in (?1)")
	public long countByInfoModelId(Collection<Integer> infoModelIds);

	@Query("select count(*) from Node bean where bean.workflow.id in (?1)")
	public long countByWorkflowId(Collection<Integer> workflowIds);
}

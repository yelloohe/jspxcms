package com.jspxcms.core.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.google.common.base.Objects;
import com.jspxcms.core.support.Siteable;

/**
 * Role
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_role")
public class Role implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 所有信息权限
	 */
	public static final int INFO_PERM_ALL = 1;
	/**
	 * 组织信息权限
	 */
	public static final int INFO_PERM_ORG = 2;
	/**
	 * 自身信息权限
	 */
	public static final int INFO_PERM_SELF = 3;

	@Transient
	public Set<Node> getInfoPerms() {
		Set<NodeRole> nodeRoles = getNodeRoles();
		Set<Node> nodes = new HashSet<Node>();
		for (NodeRole nr : nodeRoles) {
			if (nr.getInfoPerm()) {
				nodes.add(nr.getNode());
			}
		}
		return nodes;
	}

	@Transient
	public Set<Node> getNodePerms() {
		Set<NodeRole> nodeRoles = getNodeRoles();
		Set<Node> nodes = new HashSet<Node>();
		for (NodeRole nr : nodeRoles) {
			if (nr.getNodePerm()) {
				nodes.add(nr.getNode());
			}
		}
		return nodes;
	}

	@Transient
	public Set<User> getUsers() {
		Set<UserRole> userRoles = getUserRoles();
		if (userRoles == null) {
			return null;
		}
		Set<User> users = new HashSet<User>(userRoles.size());
		for (UserRole userRole : userRoles) {
			users.add(userRole.getUser());
		}
		return users;
	}

	@Transient
	public void applyDefaultValue() {
		if (getRank() == null) {
			setRank(999);
		}
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
		if (getInfoPermType() == null) {
			setInfoPermType(INFO_PERM_ALL);
		}
		if (getAllPerm() == null) {
			setAllPerm(true);
		}
		if (getAllInfoPerm() == null) {
			setAllInfoPerm(true);
		}
		if (getAllNodePerm() == null) {
			setAllNodePerm(true);
		}
		if (getInfoFinalPerm() == null) {
			setInfoFinalPerm(true);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Role)) {
			return false;
		}
		Role that = (Role) o;
		return Objects.equal(id, that.id);
	}

	private Integer id;
	private Set<UserRole> userRoles = new HashSet<UserRole>(0);
	private Set<NodeRole> nodeRoles = new HashSet<NodeRole>(0);

	private Site site;

	private String name;
	private String description;
	private Integer rank;
	private Integer seq;
	private String perms;
	private Boolean allPerm;
	private Boolean allInfoPerm;
	private Boolean allNodePerm;
	private Boolean infoFinalPerm;
	private Integer infoPermType;

	@Id
	@Column(name = "f_role_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_role", pkColumnValue = "cms_role", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_role")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "role")
	public Set<NodeRole> getNodeRoles() {
		return nodeRoles;
	}

	public void setNodeRoles(Set<NodeRole> nodeRoles) {
		this.nodeRoles = nodeRoles;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_site_id", nullable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Column(name = "f_name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "f_rank", nullable = false)
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Lob
	@Column(name = "f_perms")
	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	@Column(name = "f_is_all_perm", nullable = false, length = 1)
	public Boolean getAllPerm() {
		return allPerm;
	}

	public void setAllPerm(Boolean allPerm) {
		this.allPerm = allPerm;
	}

	@Column(name = "f_is_all_info_perm", nullable = false, length = 1)
	public Boolean getAllInfoPerm() {
		return allInfoPerm;
	}

	public void setAllInfoPerm(Boolean allInfoPerm) {
		this.allInfoPerm = allInfoPerm;
	}

	@Column(name = "f_is_all_node_perm", nullable = false, length = 1)
	public Boolean getAllNodePerm() {
		return allNodePerm;
	}

	public void setAllNodePerm(Boolean allNodePerm) {
		this.allNodePerm = allNodePerm;
	}

	@Column(name = "f_is_info_final_perm", nullable = false, length = 1)
	public Boolean getInfoFinalPerm() {
		return infoFinalPerm;
	}

	public void setInfoFinalPerm(Boolean infoFinalPerm) {
		this.infoFinalPerm = infoFinalPerm;
	}

	@Column(name = "f_info_perm_type", nullable = false)
	public Integer getInfoPermType() {
		return this.infoPermType;
	}

	public void setInfoPermType(Integer infoPermType) {
		this.infoPermType = infoPermType;
	}
}

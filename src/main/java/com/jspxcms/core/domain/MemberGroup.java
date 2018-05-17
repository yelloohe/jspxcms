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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.google.common.base.Objects;

/**
 * MemberGroup
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_member_group")
public class MemberGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// 0:普通组,1:游客组,2:IP组,3:待验证组
	/**
	 * 普通组
	 */
	public static final int GENERALS = 0;
	/**
	 * 游客组
	 */
	public static final int ANONYMOUS = 1;
	/**
	 * IP组
	 */
	public static final int IPS = 2;
	/**
	 * 待验证组
	 */
	public static final int UNVERIFIEDS = 3;

	@Transient
	public Set<Node> getViewNodes() {
		return getViewNodes(null);
	}

	@Transient
	public Set<Node> getViewNodes(Integer siteId) {
		Set<NodeMemberGroup> nodeGroups = getNodeGroups();
		Set<Node> nodes = new HashSet<Node>();
		for (NodeMemberGroup ng : nodeGroups) {
			Integer sid = ng.getNode().getSite().getId();
			if (ng.getViewPerm() && (siteId == null || siteId.equals(sid))) {
				nodes.add(ng.getNode());
			}
		}
		return nodes;
	}

	@Transient
	public Set<Node> getContriNodes() {
		return getContriNodes(null);
	}

	@Transient
	public Set<Node> getContriNodes(Integer siteId) {
		Set<NodeMemberGroup> nodeGroups = getNodeGroups();
		Set<Node> nodes = new HashSet<Node>();
		for (NodeMemberGroup ng : nodeGroups) {
			Integer sid = ng.getNode().getSite().getId();
			if (ng.getContriPerm() && (siteId == null || siteId.equals(sid))) {
				nodes.add(ng.getNode());
			}
		}
		return nodes;
	}

	@Transient
	public Set<Node> getCommentNodes() {
		return getCommentNodes(null);
	}

	@Transient
	public Set<Node> getCommentNodes(Integer siteId) {
		Set<NodeMemberGroup> nodeGroups = getNodeGroups();
		Set<Node> nodes = new HashSet<Node>();
		for (NodeMemberGroup ng : nodeGroups) {
			Integer sid = ng.getNode().getSite().getId();
			if (ng.getCommentPerm() && (siteId == null || siteId.equals(sid))) {
				nodes.add(ng.getNode());
			}
		}
		return nodes;
	}

	public void applyDefaultValue() {
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
		if (getType() == null) {
			setType(GENERALS);
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
		if (!(o instanceof MemberGroup)) {
			return false;
		}
		MemberGroup that = (MemberGroup) o;
		return Objects.equal(id, that.id);
	}

	private Integer id;

	private Set<NodeMemberGroup> nodeGroups = new HashSet<NodeMemberGroup>(0);

	private String name;
	private String description;
	private String ip;
	private Integer type;
	private Integer seq;

	@Id
	@Column(name = "f_membergroup_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_member_group", pkColumnValue = "cms_member_group", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_member_group")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group")
	public Set<NodeMemberGroup> getNodeGroups() {
		return nodeGroups;
	}

	public void setNodeGroups(Set<NodeMemberGroup> nodeGroups) {
		this.nodeGroups = nodeGroups;
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

	@Lob
	@Column(name = "f_ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "f_type", nullable = false)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

}

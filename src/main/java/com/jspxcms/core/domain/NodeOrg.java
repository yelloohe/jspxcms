package com.jspxcms.core.domain;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.Objects;
import com.jspxcms.core.domain.NodeOrg.NodeOrgId;

@Entity
@Table(name = "cms_node_org")
@IdClass(NodeOrgId.class)
public class NodeOrg implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getViewPerm() == null) {
			setViewPerm(false);
		}
	}

	public NodeOrg() {
	}

	public NodeOrg(Node node, Org org) {
		this.node = node;
		this.org = org;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_node_id", nullable = false)
	private Node node;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_org_id", nullable = false)
	private Org org;

	@Column(name = "f_is_view_perm", nullable = false, length = 1)
	private Boolean viewPerm;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Boolean getViewPerm() {
		return this.viewPerm;
	}

	public void setViewPerm(Boolean viewPerm) {
		this.viewPerm = viewPerm;
	}

	@SuppressWarnings("serial")
	public static class NodeOrgComparator implements Comparator<NodeOrg>,Serializable {
		public int compare(NodeOrg o1, NodeOrg o2) {
			return o1.getOrg().getTreeNumber().compareTo(o2.getOrg().getTreeNumber());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof NodeOrg)) {
			return false;
		}
		NodeOrg that = (NodeOrg) o;
		return Objects.equal(node, that.node) && Objects.equal(org, that.org);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(node, org);
	}

	public static class NodeOrgId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer node;
		Integer org;

		public NodeOrgId() {
		}

		public NodeOrgId(Integer node, Integer org) {
			this.node = node;
			this.org = org;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof NodeOrgId)) {
				return false;
			}
			NodeOrgId that = (NodeOrgId) o;
			return Objects.equal(node, that.node) && Objects.equal(org, that.org);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(node, org);
		}
	}
}

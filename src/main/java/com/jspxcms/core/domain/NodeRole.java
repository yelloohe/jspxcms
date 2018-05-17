package com.jspxcms.core.domain;

import java.io.Serializable;

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
import com.jspxcms.core.domain.NodeRole.NodeRoleId;

@Entity
@Table(name = "cms_node_role")
@IdClass(NodeRoleId.class)
public class NodeRole implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getNodePerm() == null) {
			setNodePerm(true);
		}
		if (getInfoPerm() == null) {
			setInfoPerm(true);
		}
	}

	public NodeRole() {
	}

	public NodeRole(Node node, Role role) {
		this.node = node;
		this.role = role;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_node_id", nullable = false)
	private Node node;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_role_id", nullable = false)
	private Role role;

	@Column(name = "f_is_node_perm", nullable = false, length = 1)
	private Boolean nodePerm;

	@Column(name = "f_is_info_perm", nullable = false, length = 1)
	private Boolean infoPerm;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Boolean getNodePerm() {
		return this.nodePerm;
	}

	public void setNodePerm(Boolean nodePerm) {
		this.nodePerm = nodePerm;
	}

	public Boolean getInfoPerm() {
		return this.infoPerm;
	}

	public void setInfoPerm(Boolean infoPerm) {
		this.infoPerm = infoPerm;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof NodeRole)) {
			return false;
		}
		NodeRole that = (NodeRole) o;
		return Objects.equal(node, that.node) && Objects.equal(role, that.role);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(node, role);
	}

	public static class NodeRoleId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer node;
		Integer role;

		public NodeRoleId() {
		}

		public NodeRoleId(Integer node, Integer role) {
			this.node = node;
			this.role = role;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof NodeRoleId)) {
				return false;
			}
			NodeRoleId that = (NodeRoleId) o;
			return Objects.equal(node, that.node) && Objects.equal(role, that.role);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(node, role);
		}

	}
}

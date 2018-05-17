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
import com.jspxcms.core.domain.NodeMemberGroup.NodeMemberGroupId;

@Entity
@Table(name = "cms_node_membergroup")
@IdClass(NodeMemberGroupId.class)
public class NodeMemberGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getViewPerm() == null) {
			setViewPerm(true);
		}
		if (getContriPerm() == null) {
			setContriPerm(true);
		}
		if (getCommentPerm() == null) {
			setCommentPerm(true);
		}
	}

	public NodeMemberGroup() {
	}

	public NodeMemberGroup(Node node, MemberGroup group) {
		this.node = node;
		this.group = group;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_node_id", nullable = false)
	private Node node;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_membergroup_id", nullable = false)
	private MemberGroup group;

	@Column(name = "f_is_view_perm", nullable = false, length = 1)
	private Boolean viewPerm;

	@Column(name = "f_is_contri_perm", nullable = false, length = 1)
	private Boolean contriPerm;

	@Column(name = "f_is_comment_perm", nullable = false, length = 1)
	private Boolean commentPerm;

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public MemberGroup getGroup() {
		return group;
	}

	public void setGroup(MemberGroup group) {
		this.group = group;
	}

	public Boolean getViewPerm() {
		return this.viewPerm;
	}

	public void setViewPerm(Boolean viewPerm) {
		this.viewPerm = viewPerm;
	}

	public Boolean getContriPerm() {
		return this.contriPerm;
	}

	public void setContriPerm(Boolean contriPerm) {
		this.contriPerm = contriPerm;
	}

	public Boolean getCommentPerm() {
		return this.commentPerm;
	}

	public void setCommentPerm(Boolean commentPerm) {
		this.commentPerm = commentPerm;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof NodeMemberGroup)) {
			return false;
		}
		NodeMemberGroup that = (NodeMemberGroup) o;
		return Objects.equal(node, that.node) && Objects.equal(group, that.group);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(node, group);
	}

	public static class NodeMemberGroupId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer node;
		Integer group;

		public NodeMemberGroupId() {
		}

		public NodeMemberGroupId(Integer node, Integer group) {
			this.node = node;
			this.group = group;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof NodeMemberGroupId)) {
				return false;
			}
			NodeMemberGroupId that = (NodeMemberGroupId) o;
			return Objects.equal(node, that.node) && Objects.equal(group, that.group);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(node, group);
		}

	}
}

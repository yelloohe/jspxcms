package com.jspxcms.core.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.jspxcms.core.domain.InfoNode.InfoNodeId;

@Entity
@Table(name = "cms_info_node")
@IdClass(InfoNodeId.class)
public class InfoNode implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public InfoNode() {
	}

	public InfoNode(Info info, Node node) {
		this.info = info;
		this.node = node;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_info_id", nullable = false)
	private Info info;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_node_id", nullable = false)
	private Node node;

	public Info getInfo() {
		return this.info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InfoNode)) {
			return false;
		}
		InfoNode that = (InfoNode) o;
		return Objects.equal(info, that.info) && Objects.equal(node, that.node);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(info, node);
	}

	public static class InfoNodeId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer info;
		Integer node;

		public InfoNodeId() {
		}

		public InfoNodeId(Integer info, Integer node) {
			this.info = info;
			this.node = node;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof InfoNodeId)) {
				return false;
			}
			InfoNodeId that = (InfoNodeId) o;
			return Objects.equal(info, that.info) && Objects.equal(node, that.node);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(info, node);
		}
	}
}

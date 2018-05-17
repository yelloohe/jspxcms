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
import com.jspxcms.core.domain.InfoMemberGroup.InfoMemberGroupId;

@Entity
@Table(name = "cms_info_membergroup")
@IdClass(InfoMemberGroupId.class)
public class InfoMemberGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getViewPerm() == null) {
			setViewPerm(false);
		}
	}

	public InfoMemberGroup() {
	}

	public InfoMemberGroup(Info info, MemberGroup group) {
		this.info = info;
		this.group = group;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_info_id", nullable = false)
	private Info info;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_membergroup_id", nullable = false)
	private MemberGroup group;

	@Column(name = "f_is_view_perm", nullable = false, length = 1)
	private Boolean viewPerm;

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InfoMemberGroup)) {
			return false;
		}
		InfoMemberGroup that = (InfoMemberGroup) o;
		return Objects.equal(info, that.info) && Objects.equal(group, that.group);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(info, group);
	}

	public static class InfoMemberGroupId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer info;
		Integer group;

		public InfoMemberGroupId() {
		}

		public InfoMemberGroupId(Integer info, Integer group) {
			this.info = info;
			this.group = group;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof InfoMemberGroupId)) {
				return false;
			}
			InfoMemberGroupId that = (InfoMemberGroupId) o;
			return Objects.equal(info, that.info) && Objects.equal(group, that.group);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(info, group);
		}
	}
}

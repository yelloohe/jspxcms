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
import com.jspxcms.core.domain.UserMemberGroup.UserMemberGroupId;

@Entity
@Table(name = "cms_user_membergroup")
@IdClass(UserMemberGroupId.class)
public class UserMemberGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public UserMemberGroup() {
	}

	public UserMemberGroup(User user, MemberGroup group) {
		this.user = user;
		this.group = group;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_membergroup_id", nullable = false)
	private MemberGroup group;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MemberGroup getGroup() {
		return group;
	}

	public void setGroup(MemberGroup group) {
		this.group = group;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UserMemberGroup)) {
			return false;
		}
		UserMemberGroup that = (UserMemberGroup) o;
		return Objects.equal(user, that.user) && Objects.equal(group, that.group);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(user, group);
	}

	public static class UserMemberGroupId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer user;
		Integer group;

		public UserMemberGroupId() {
		}

		public UserMemberGroupId(Integer user, Integer group) {
			this.user = user;
			this.group = group;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof UserMemberGroupId)) {
				return false;
			}
			UserMemberGroupId that = (UserMemberGroupId) o;
			return Objects.equal(user, that.user) && Objects.equal(group, that.group);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(user, group);
		}
	}

}

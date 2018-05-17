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
import com.jspxcms.core.domain.UserRole.UserRoleId;

@Entity
@Table(name = "cms_user_role")
@IdClass(UserRoleId.class)
public class UserRole implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public UserRole() {
	}

	public UserRole(User user, Role role) {
		this.user = user;
		this.role = role;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_role_id", nullable = false)
	private Role role;

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UserRole)) {
			return false;
		}
		UserRole that = (UserRole) o;
		return Objects.equal(user, that.user) && Objects.equal(role, that.role);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(user, role);
	}

	public static class UserRoleId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer user;
		Integer role;

		public UserRoleId() {
		}

		public UserRoleId(Integer user, Integer role) {
			this.user = user;
			this.role = role;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof UserRoleId)) {
				return false;
			}
			UserRoleId that = (UserRoleId) o;
			return Objects.equal(user, that.user) && Objects.equal(role, that.role);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(user, role);
		}
	}
}

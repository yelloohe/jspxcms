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
import com.jspxcms.core.domain.UserOrg.UserOrgId;

@Entity
@Table(name = "cms_user_org")
@IdClass(UserOrgId.class)
public class UserOrg implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public UserOrg() {
	}

	public UserOrg(User user, Org org) {
		this.user = user;
		this.org = org;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_org_id", nullable = false)
	private Org org;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UserOrg)) {
			return false;
		}
		UserOrg that = (UserOrg) o;
		return Objects.equal(user, that.user) && Objects.equal(org, that.org);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(user, org);
	}

	public static class UserOrgId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer user;
		Integer org;

		public UserOrgId() {
		}

		public UserOrgId(Integer user, Integer org) {
			this.user = user;
			this.org = org;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof UserOrgId)) {
				return false;
			}
			UserOrgId that = (UserOrgId) o;
			return Objects.equal(user, that.user) && Objects.equal(org, that.org);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(user, org);
		}
	}
}

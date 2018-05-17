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
import com.jspxcms.core.domain.InfoOrg.InfoOrgId;

@Entity
@Table(name = "cms_info_org")
@IdClass(InfoOrgId.class)
public class InfoOrg implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getViewPerm() == null) {
			setViewPerm(false);
		}
	}

	public InfoOrg() {
	}

	public InfoOrg(Info info, Org org) {
		this.info = info;
		this.org = org;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_info_id", nullable = false)
	private Info info;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_org_id", nullable = false)
	private Org org;

	@Column(name = "f_is_view_perm", nullable = false, length = 1)
	private Boolean viewPerm;

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InfoOrg)) {
			return false;
		}
		InfoOrg that = (InfoOrg) o;
		return Objects.equal(info, that.info) && Objects.equal(org, that.org);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(info, org);
	}

	public static class InfoOrgId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer info;
		Integer org;

		public InfoOrgId() {
		}

		public InfoOrgId(Integer info, Integer org) {
			this.info = info;
			this.org = org;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof InfoOrgId)) {
				return false;
			}
			InfoOrgId that = (InfoOrgId) o;
			return Objects.equal(info, that.info) && Objects.equal(org, that.org);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(info, org);
		}
	}

	@SuppressWarnings("serial")
	public static class InfoOrgComparator implements Comparator<InfoOrg>, Serializable {
		public int compare(InfoOrg o1, InfoOrg o2) {
			return o1.getOrg().getTreeNumber().compareTo(o2.getOrg().getTreeNumber());
		}
	}
}

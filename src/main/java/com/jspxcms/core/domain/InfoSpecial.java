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
import com.jspxcms.core.domain.InfoSpecial.InfoSpecialId;

/**
 * InfoSpecial
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_info_special")
@IdClass(InfoSpecialId.class)
public class InfoSpecial implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_info_id", nullable = false)
	private Info info;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_special_id", nullable = false)
	private Special special;

	public InfoSpecial() {
	}

	public InfoSpecial(Info info, Special special) {
		this.info = info;
		this.special = special;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Special getSpecial() {
		return special;
	}

	public void setSpecial(Special special) {
		this.special = special;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InfoSpecial)) {
			return false;
		}
		InfoSpecial that = (InfoSpecial) o;
		return Objects.equal(info, that.info) && Objects.equal(special, that.special);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(info, special);
	}

	public static class InfoSpecialId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer info;
		Integer special;

		public InfoSpecialId() {
		}

		public InfoSpecialId(Integer infoId, Integer specialId) {
			this.info = infoId;
			this.special = specialId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof InfoSpecialId)) {
				return false;
			}
			InfoSpecialId that = (InfoSpecialId) o;
			return Objects.equal(info, that.info) && Objects.equal(special, that.special);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(info, special);
		}
	}
}

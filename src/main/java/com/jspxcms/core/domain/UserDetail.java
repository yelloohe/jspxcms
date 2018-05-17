package com.jspxcms.core.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

/**
 * UserDetail
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_user_detail")
public class UserDetail implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getCreationDate() == null) {
			setCreationDate(new Timestamp(System.currentTimeMillis()));
		}
		if (getCreationIp() == null) {
			setCreationIp("127.0.0.1");
		}
		if (getLoginErrorCount() == null) {
			setLoginErrorCount(0);
		}
		if (getLogins() == null) {
			setLogins(0);
		}
		if (getWithAvatar() == null) {
			setWithAvatar(false);
		}
	}

	private Integer id;
	private User user;
	private Date validationDate;
	private Date loginErrorDate;
	private Integer loginErrorCount;
	private Date prevLoginDate;
	private String prevLoginIp;
	private Date lastLoginDate;
	private String lastLoginIp;
	private Date creationDate;
	private String creationIp;
	private Integer logins;

	private Boolean withAvatar;
	private String bio;
	private String comeFrom;
	private String qq;
	private String msn;
	private String weixin;

	@Id
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@MapsId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_validation_date", length = 19)
	public Date getValidationDate() {
		return validationDate;
	}

	public void setValidationDate(Date validationDate) {
		this.validationDate = validationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_login_error_date", length = 19)
	public Date getLoginErrorDate() {
		return loginErrorDate;
	}

	public void setLoginErrorDate(Date loginErrorDate) {
		this.loginErrorDate = loginErrorDate;
	}

	@Column(name = "f_login_error_count", nullable = false)
	public Integer getLoginErrorCount() {
		return loginErrorCount;
	}

	public void setLoginErrorCount(Integer loginErrorCount) {
		this.loginErrorCount = loginErrorCount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_prev_login_date", length = 19)
	public Date getPrevLoginDate() {
		return this.prevLoginDate;
	}

	public void setPrevLoginDate(Date prevLoginDate) {
		this.prevLoginDate = prevLoginDate;
	}

	@Length(max = 100)
	@Column(name = "f_prev_login_ip", length = 100)
	public String getPrevLoginIp() {
		return this.prevLoginIp;
	}

	public void setPrevLoginIp(String prevLoginIp) {
		this.prevLoginIp = prevLoginIp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_last_login_date", length = 19)
	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Length(max = 100)
	@Column(name = "f_last_login_ip", length = 100)
	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_creation_date", nullable = false, length = 19)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Length(max = 100)
	@Column(name = "f_creation_ip", length = 100)
	public String getCreationIp() {
		return this.creationIp;
	}

	public void setCreationIp(String creationIp) {
		this.creationIp = creationIp;
	}

	@Column(name = "f_logins", nullable = false)
	public Integer getLogins() {
		return this.logins;
	}

	public void setLogins(Integer logins) {
		this.logins = logins;
	}

	@Column(name = "f_is_with_avatar", nullable = false, length = 1)
	public Boolean getWithAvatar() {
		return withAvatar;
	}

	public void setWithAvatar(Boolean withAvatar) {
		this.withAvatar = withAvatar;
	}

	@Column(name = "f_bio")
	public String getBio() {
		return this.bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	@Length(max = 100)
	@Column(name = "f_come_from", length = 100)
	public String getComeFrom() {
		return this.comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	@Length(max = 100)
	@Column(name = "f_qq", length = 100)
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Length(max = 100)
	@Column(name = "f_msn", length = 100)
	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	@Length(max = 100)
	@Column(name = "f_weixin", length = 100)
	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

}

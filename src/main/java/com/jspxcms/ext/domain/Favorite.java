package com.jspxcms.ext.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.jspxcms.common.web.Anchor;
import com.jspxcms.core.domain.User;

@Entity
@Table(name = "cms_favorite")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype_", discriminatorType = DiscriminatorType.STRING)
public abstract class Favorite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public abstract Anchor getAnchor();

	@Transient
	public void applyDefaultValue() {
		if (getCreated() == null) {
			setCreated(new Timestamp(System.currentTimeMillis()));
		}
	}

	@Id
	@Column(name = "favorite_id_", unique = true, nullable = false)
	@TableGenerator(name = "cms_favorite", pkColumnValue = "cms_favorite", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "cms_favorite")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_", nullable = false)
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_")
	private Date created;

	public Favorite() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}

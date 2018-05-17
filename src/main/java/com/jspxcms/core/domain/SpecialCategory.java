package com.jspxcms.core.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.jspxcms.core.support.Siteable;

/**
 * SpecialCategory
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_special_category")
public class SpecialCategory implements java.io.Serializable, Siteable {
	private static final long serialVersionUID = 1L;

	@Transient
	public String getKeywords() {
		String keywords = getMetaKeywords();
		if (StringUtils.isBlank(keywords)) {
			return getName();
		} else {
			return keywords;
		}
	}

	@Transient
	public String getDescription() {
		String description = getDescription();
		if (StringUtils.isBlank(description)) {
			return getName();
		} else {
			return description;
		}
	}

	public void applyDefaultValue() {
		if (getCreationDate() == null) {
			setCreationDate(new Timestamp(System.currentTimeMillis()));
		}
		if (getViews() == null) {
			setViews(0);
		}
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
	}

	private Integer id;

	private Site site;

	private String name;
	private Integer seq;
	private Integer views;
	private String metaKeywords;
	private String metaDescription;
	private Date creationDate;

	// private Integer refers;

	public SpecialCategory() {
	}

	public SpecialCategory(String name, Site site) {
		this.name = name;
		this.site = site;
	}

	@Id
	@Column(name = "f_speccate_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_special_category", pkColumnValue = "cms_special_category", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_special_category")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_site_id", nullable = false)
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Column(name = "f_name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Column(name = "f_views", nullable = false)
	public Integer getViews() {
		return this.views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	@Column(name = "f_meta_keywords", length = 150)
	public String getMetaKeywords() {
		return this.metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	@Column(name = "f_meta_description", length = 450)
	public String getMetaDescription() {
		return this.metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_creation_date", nullable = false, length = 19)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	// @Basic(fetch=FetchType.LAZY)
	// @Formula("(select count(*) from cms_special t where t.f_speccate_id = f_speccate_id)")
	// public int getRefers() {
	// return this.refers;
	// }
	//
	// public void setRefers(Integer refers) {
	// this.refers = refers;
	// }
}

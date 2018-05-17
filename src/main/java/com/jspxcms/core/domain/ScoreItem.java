package com.jspxcms.core.domain;

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
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

/**
 * ScoreItem
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_score_item")
public class ScoreItem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public String getIconUrl() {
		String icon = getIcon();
		if (StringUtils.isBlank(icon)) {
			return icon;
		}
		return getSite().getFilesUrl(icon);
	}

	@Transient
	public void applyDefaultValue() {
		if (getScore() == null) {
			setScore(1);
		}
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
	}

	private Integer id;

	private Site site;
	private ScoreGroup group;

	private String name;
	private Integer score;
	private String icon;
	private Integer seq;

	@Id
	@Column(name = "f_scoreitem_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_scoreitem", pkColumnValue = "cms_scoreitem", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_scoreitem")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_scoregroup_id", nullable = false)
	public ScoreGroup getGroup() {
		return this.group;
	}

	public void setGroup(ScoreGroup group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_site_id", nullable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Column(name = "f_name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_score", nullable = false)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Column(name = "f_icon")
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}

package com.jspxcms.ext.domain;

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

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Siteable;

/**
 * Friendlink
 * 
 * @author yangxing
 * 
 */
@Entity
@Table(name = "cms_friendlink")
public class Friendlink implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 已审核
	 */
	public static final int AUDITED = 0;
	/**
	 * 未审核
	 */
	public static final int SAVED = 1;

	public void applyDefaultValue() {
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
		if (getStatus() == null) {
			setStatus(AUDITED);
		}
	}

	private Integer id;
	private FriendlinkType type;
	private Site site;

	private String name;
	private String url;
	private Integer seq;
	private String logo;
	private String description;
	private String email;
	private Boolean recommend;
	private Integer status;
	private Boolean withLogo;

	@Id
	@Column(name = "f_friendlink_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_friendlink", pkColumnValue = "cms_friendlink", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_friendlink")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_friendlinktype_id", nullable = false)
	public FriendlinkType getType() {
		return this.type;
	}

	public void setType(FriendlinkType type) {
		this.type = type;
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

	@Column(name = "f_url", nullable = false)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Column(name = "f_logo")
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Column(name = "f_description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "f_email", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "f_is_recommend", nullable = false, length = 1)
	public Boolean getRecommend() {
		return this.recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	@Column(name = "f_status", nullable = false, length = 1)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "f_is_with_logo", nullable = false, length = 1)
	public Boolean getWithLogo() {
		return withLogo;
	}

	public void setWithLogo(Boolean withLogo) {
		this.withLogo = withLogo;
	}

}

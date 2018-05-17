package com.jspxcms.ext.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Siteable;

/**
 * Ad 广告
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_ad")
public class Ad implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 附件类型
	 */
	public static final String ATTACH_TYPE = "ad";

	@Transient
	public Set<String> getAttachUrls() {
		Set<String> attachUrls = new HashSet<String>();
		if (StringUtils.isNotBlank(getImage())) {
			attachUrls.add(getImage());
		}
		if (StringUtils.isNoneBlank(getFlash())) {
			attachUrls.add(getFlash());
		}
		return attachUrls;
	}

	@Transient
	public void applyDefaultValue() {
		if (getSeq() == null) {
			setSeq(50);
		}
	}

	private Integer id;
	private AdSlot slot;
	private Site site;

	private String name;
	private Date beginDate;
	private Date endDate;
	private String url;
	private String text;
	private String script;
	private String image;
	private String flash;
	private Integer seq;

	@Id
	@Column(name = "f_ad_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_ad", pkColumnValue = "cms_ad", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_ad")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_adslot_id", nullable = false)
	public AdSlot getSlot() {
		return this.slot;
	}

	public void setSlot(AdSlot slot) {
		this.slot = slot;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_site_id", nullable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Column(name = "f_name", nullable = false, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_begin_date", length = 19)
	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_end_date", length = 19)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "f_url")
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "f_text")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "f_script")
	public String getScript() {
		return this.script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Column(name = "f_image")
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "f_flash")
	public String getFlash() {
		return this.flash;
	}

	public void setFlash(String flash) {
		this.flash = flash;
	}

	@Column(name = "f_seq")
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}

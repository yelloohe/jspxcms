package com.jspxcms.ext.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Siteable;

/**
 * 广告板位
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_ad_slot")
public class AdSlot implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public static final int TEXT = 1;
	public static final int IMAGE = 2;
	public static final int FLASH = 3;
	public static final int CODE = 4;

	@Transient
	public void addAd(Ad ad) {
		List<Ad> ads = getAds();
		if (ads == null) {
			ads = new ArrayList<Ad>();
			setAds(ads);
		}
		ads.add(ad);
	}

	@Transient
	public void removeAd(Ad ad) {
		List<Ad> ads = getAds();
		if (ads != null) {
			ads.remove(ad);
		}

	}

	@Transient
	public void applyDefaultValue() {
		if (getType() == null) {
			setType(TEXT);
		}
	}

	private Integer id;
	private List<Ad> ads = new ArrayList<Ad>(0);

	private Site site;

	private String name;
	private String number;
	private String description;
	private Integer type;
	private String template;
	private Integer width;
	private Integer height;

	@Id
	@Column(name = "f_adslot_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_ad_slot", pkColumnValue = "cms_ad_slot", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_ad_slot")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "slot")
	@OrderBy(value = "seq asc, id asc")
	public List<Ad> getAds() {
		return this.ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
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

	@Column(name = "f_number", nullable = false, length = 100)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "f_description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "f_type", nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "f_template", nullable = false)
	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Column(name = "f_width", nullable = false)
	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Column(name = "f_height", nullable = false)
	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}
}

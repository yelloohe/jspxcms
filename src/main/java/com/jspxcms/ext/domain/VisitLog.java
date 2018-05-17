package com.jspxcms.ext.domain;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.support.Siteable;

@Entity
@Table(name = "cms_visit_log")
public class VisitLog implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 访问来源：直接访问
	 */
	public static final String SOURCE_DIRECT = "DIRECT";
	/**
	 * 访问来源：其他来源
	 */
	public static final String SOURCE_OTHER = "OTHER";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");
	public static final DateFormat DATE_HH_FORMAT = new SimpleDateFormat(
			"yyyyMMddHH");
	public static final DateFormat DATE_HHMM_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmm");
	public static final DateFormat DATE_HHMMSS_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	@Transient
	public void applyDefaultValue() {
		if (getTime() == null) {
			setTime(new Timestamp(System.currentTimeMillis()));
		}
		if (getTimeString() == null) {
			setTimeString(DATE_HHMMSS_FORMAT.format(getTime()));
		}
	}

	private Integer id;

	private Site site;
	private User user;
	private String url;
	private String referrer;
	private String source;
	private String ip;
	private String country;
	private String area;
	private String cookie;
	private String userAgent;
	private String browser;
	private String os;
	private String device;
	private String timeString;
	private Date time;

	@Id
	@Column(name = "f_visitlog_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_visit_log", pkColumnValue = "cms_visit_log", initialValue = 1, allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_visit_log")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "f_url", nullable = false, length = 255)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "f_referrer", length = 255)
	public String getReferrer() {
		return this.referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	@Column(name = "f_source", length = 100)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "f_ip", length = 100)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "f_cookie", length = 100)
	public String getCookie() {
		return this.cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	@Column(name = "f_user_agent", length = 450)
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Column(name = "f_country", length = 100)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "f_area", length = 100)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "f_browser", length = 100)
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	@Column(name = "f_os", length = 100)
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	@Column(name = "f_device", length = 100)
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Column(name = "f_time_string", length = 10)
	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_time", nullable = false, length = 19)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}

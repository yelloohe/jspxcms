package com.jspxcms.core.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.jspxcms.core.support.Siteable;

/**
 * OperationLog
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_operation_log")
public class OperationLog implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 类型：操作日志
	 */
	public static final int TYPE_OPERATION = 1;
	/**
	 * 类型：登录日志
	 */
	public static final int TYPE_LOGIN = 2;
	/**
	 * 类型：登录失败
	 */
	public static final int TYPE_LOGIN_FAILURE = 3;

	@Transient
	public void applyDefaultValue() {
		if (StringUtils.isBlank(ip)) {
			setIp("localhost");
		}
		if (getTime() == null) {
			setTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	private Integer id;
	private Site site;
	private User user;

	private String name;
	private Integer dataId;
	private String description;
	private String text;
	private String ip;
	private String country;
	private String area;
	private Date time;
	private Integer type;

	@Id
	@Column(name = "f_operation_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_operation_log", pkColumnValue = "cms_operation_log", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_operation_log")
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
	@JoinColumn(name = "f_user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "f_name", nullable = false, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_data_id")
	public Integer getDataId() {
		return this.dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	@Column(name = "f_description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "f_text")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "f_ip", nullable = false, length = 100)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_time", nullable = false, length = 19)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "f_type", nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}

package com.jspxcms.core.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.jspxcms.common.file.FilesEx;
import com.jspxcms.core.support.Siteable;

/**
 * Attachment
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_attachment")
public class Attachment implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public boolean isUsed() {
		Set<AttachmentRef> refs = getRefs();
		return refs != null && !refs.isEmpty();
	}

	@Transient
	public String getSize() {
		Long length = getLength();
		return FilesEx.getSize(length);
	}

	@Transient
	public void applyDefaultValue() {
		if (getIp() == null) {
			setIp("localhost");
		}
		if (getTime() == null) {
			setTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	private Integer id;
	private Set<AttachmentRef> refs = new HashSet<AttachmentRef>(0);

	private Site site;
	private User user;

	private String name;
	private String ip;
	private Date time;
	private Long length;

	@Id
	@Column(name = "f_attachment_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_attachment", pkColumnValue = "cms_attachment", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_attachment")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, mappedBy = "attachment")
	public Set<AttachmentRef> getRefs() {
		return this.refs;
	}

	public void setRefs(Set<AttachmentRef> refs) {
		this.refs = refs;
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

	@Column(name = "f_ip", length = 100)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_time", nullable = false, length = 19)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "f_length")
	public Long getLength() {
		return this.length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

}

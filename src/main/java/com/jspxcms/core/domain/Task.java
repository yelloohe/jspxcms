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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.jspxcms.core.support.Siteable;

@Entity
@Table(name = "cms_task")
public class Task implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 运行中
	 */
	public static final int RUNNING = 0;
	/**
	 * 完成
	 */
	public static final int FINISH = 1;
	/**
	 * 终止
	 */
	public static final int ABORT = 2;
	/**
	 * 停止
	 */
	public static final int STOP = 3;
	/**
	 * 节点html生成
	 */
	public static final int NODE_HTML = 1;
	/**
	 * 信息html生成
	 */
	public static final int INFO_HTML = 2;
	/**
	 * 全文索引生成
	 */
	public static final int FULLTEXT = 3;

	@Transient
	public boolean isRunning() {
		return getStatus() == RUNNING;
	}

	@Transient
	public boolean isStop() {
		return getStatus() == STOP;
	}

	@Transient
	public void finish() {
		if (isRunning()) {
			setEndTime(new Timestamp(System.currentTimeMillis()));
			setStatus(FINISH);
		}
	}

	@Transient
	public void abort() {
		if (isRunning()) {
			setEndTime(new Timestamp(System.currentTimeMillis()));
			setStatus(ABORT);
		}
	}

	@Transient
	public void stop() {
		if (isRunning()) {
			setEndTime(new Timestamp(System.currentTimeMillis()));
			setStatus(STOP);
		}
	}

	@Transient
	public void add(int count) {
		setTotal(getTotal() + count);
	}

	@Transient
	public void applyDefaultValue() {
		if (getBeginTime() == null) {
			setBeginTime(new Timestamp(System.currentTimeMillis()));
		}
		if (getTotal() == null) {
			setTotal(0);
		}
		if (getStatus() == null) {
			setStatus(RUNNING);
		}
	}

	private Integer id;

	private User user;
	private Site site;

	private String name;
	private String description;
	private Date beginTime;
	private Date endTime;
	private Integer total;
	private Integer type;
	private Integer status;

	@Id
	@Column(name = "f_task_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_task", pkColumnValue = "cms_task", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_task")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_site_id", nullable = false)
	public Site getSite() {
		return site;
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

	@Lob
	@Column(name = "f_description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_begin_time", nullable = false, length = 19)
	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_end_time", length = 19)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "f_total", nullable = false)
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Column(name = "f_type", nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "f_status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}

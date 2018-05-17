package com.jspxcms.ext.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Siteable;

@Entity
@Table(name = "cms_question")
public class Question implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 无限制模式
	 */
	public static final int MODE_UNLIMITED = 0;
	/**
	 * 独立访客模式
	 */
	public static final int MODE_COOKIE = 1;
	/**
	 * 独立IP模式
	 */
	public static final int MODE_IP = 2;
	/**
	 * 独立用户模式
	 */
	public static final int MODE_USER = 3;

	/**
	 * 调查进行中
	 */
	public static final int PERIOD_STATUS_UNDERWAY = 0;
	/**
	 * 调查还未开始
	 */
	public static final int PERIOD_STATUS_BEFORE = 1;
	/**
	 * 调查已经结束
	 */
	public static final int PERIOD_STATUS_AFTER = 2;

	/**
	 * 启用状态
	 */
	public static final int NOMAL_STATUS = 0;
	/**
	 * 禁用状态
	 */
	public static final int DISABLED_STATUS = 1;

	@Transient
	public int getPeriodStatus() {
		long now = System.currentTimeMillis();
		if (getBeginDate() != null && getBeginDate().getTime() > now) {
			return PERIOD_STATUS_BEFORE;
		} else if (getEndDate() != null && getEndDate().getTime() < now) {
			return PERIOD_STATUS_AFTER;
		} else {
			return PERIOD_STATUS_UNDERWAY;
		}
	}

	@Transient
	public boolean isDisabled() {
		return getStatus() == DISABLED_STATUS;
	}

	@Transient
	public void applyDefaultValue() {
		if (getInterval() == null) {
			setInterval(0);
		}
		if (getCreationDate() == null) {
			setCreationDate(new Timestamp(System.currentTimeMillis()));
		}
		if (getTotal() == null) {
			setTotal(0);
		}
		if (getMode() == null) {
			setMode(MODE_COOKIE);
		}
		if (getStatus() == null) {
			setStatus(NOMAL_STATUS);
		}
	}

	private Integer id;
	private List<QuestionItem> items = new ArrayList<QuestionItem>(0);
	private Set<QuestionRecord> records = new HashSet<QuestionRecord>(0);

	private Site site;

	private String title;
	private String description;
	private Date beginDate;
	private Date endDate;
	private Integer interval;
	private Date creationDate;
	private Integer total;
	private Integer mode;
	private Integer status;

	@Id
	@Column(name = "f_question_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_question", pkColumnValue = "cms_question", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_question")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, mappedBy = "question")
	@OrderBy(value = "seq asc, id asc")
	public List<QuestionItem> getItems() {
		return this.items;
	}

	public void setItems(List<QuestionItem> items) {
		this.items = items;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, mappedBy = "question")
	public Set<QuestionRecord> getRecords() {
		return records;
	}

	public void setRecords(Set<QuestionRecord> records) {
		this.records = records;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_site_id", nullable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Column(name = "f_title", nullable = false, length = 150)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "f_description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_begin_date", length = 7)
	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_end_date", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "f_interval", nullable = false)
	public Integer getInterval() {
		return this.interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_creation_date", nullable = false)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "f_mode", nullable = false)
	public Integer getMode() {
		return this.mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	@Column(name = "f_total", nullable = false)
	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	@Column(name = "f_status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}

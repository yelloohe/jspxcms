package com.jspxcms.ext.domain;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.support.Siteable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Vote 投票实体类
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_vote")
public class Vote implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * VoteMark类型
	 */
	public static final String MARK_CODE = "Vote";
	/**
	 * 无限制模式
	 */
	public static final int UNLIMITED_MODE = 0;
	/**
	 * 独立访客模式
	 */
	public static final int COOKIE_MODE = 1;
	/**
	 * 独立IP模式
	 */
	public static final int IP_MODE = 2;
	/**
	 * 独立用户模式
	 */
	public static final int USER_MODE = 3;
	/**
	 * 启用状态
	 */
	public static final int NORMAL_STATUS = 0;
	/**
	 * 禁用状态
	 */
	public static final int DISABLED_STATUS = 1;

	@Transient
	public void applyDefaultValue() {
		if (getInterval() == null) {
			setInterval(0);
		}
		if (getCreationDate() == null) {
			setCreationDate(new Timestamp(System.currentTimeMillis()));
		}
		if (getMaxSelected() == null) {
			setMaxSelected(1);
		}
		if (getMode() == null) {
			setMode(COOKIE_MODE);
		}
		if (getTotal() == null) {
			setTotal(0);
		}
		if (getStatus() == null) {
			setStatus(NORMAL_STATUS);
		}
	}

	private Integer id;
	private List<VoteOption> options = new ArrayList<VoteOption>(0);

	private Site site;

	private String title;
	private String number;
	private String description;
	private Date creationDate;
	private Date beginDate;
	private Date endDate;
	private Integer interval;
	private Integer maxSelected;
	private Integer mode;
	private Integer total;
	private Integer status;

	@Id
	@Column(name = "f_vote_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_vote", pkColumnValue = "cms_vote", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_vote")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, mappedBy = "vote")
	@OrderBy("seq asc,id asc")
	public List<VoteOption> getOptions() {
		return this.options;
	}

	public void setOptions(List<VoteOption> options) {
		this.options = options;
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

	@Column(name = "f_number", length = 100)
	public String getNumber() {
		return this.number;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_creation_date", nullable = false)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	@Column(name = "f_interval", nullable = false)
	public Integer getInterval() {
		return this.interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	@Column(name = "f_max_selected", nullable = false)
	public Integer getMaxSelected() {
		return maxSelected;
	}

	public void setMaxSelected(Integer maxSelected) {
		this.maxSelected = maxSelected;
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

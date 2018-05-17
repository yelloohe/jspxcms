package com.jspxcms.core.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * WorkflowProcess
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_workflow_process")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "f_data_type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class WorkflowProcess implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public abstract String getName();

	@Transient
	public void passEnd() {
		setStep(null);
		setEndDate(new Timestamp(System.currentTimeMillis()));
		setRejection(false);
		setEnd(true);
	}

	@Transient
	public void rejectEnd() {
		setStep(null);
		setEndDate(new Timestamp(System.currentTimeMillis()));
		setRejection(true);
		setEnd(true);
	}

	@Transient
	public void applyDefaultValue() {
		if (getBeginDate() == null) {
			setBeginDate(new Timestamp(System.currentTimeMillis()));
		}
		if (getRejection() == null) {
			setRejection(false);
		}
		if (getEnd() == null) {
			setEnd(false);
		}
	}

	private Integer id;
	private Set<WorkflowLog> logs = new HashSet<WorkflowLog>(0);

	private Workflow workflow;
	private WorkflowStep step;
	private User user;
	private Site site;

	private Integer dataType;
	private Integer dataId;
	private Date beginDate;
	private Date endDate;
	private Boolean rejection;
	private Boolean end;

	@Id
	@Column(name = "f_workflowprocess_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_workflow_process", pkColumnValue = "cms_workflow_process", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_workflow_process")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "process")
	public Set<WorkflowLog> getLogs() {
		return this.logs;
	}

	public void setLogs(Set<WorkflowLog> logs) {
		this.logs = logs;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_workflow_id", nullable = false)
	public Workflow getWorkflow() {
		return this.workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_workflowstep_id", nullable = false)
	public WorkflowStep getStep() {
		return step;
	}

	public void setStep(WorkflowStep step) {
		this.step = step;
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
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Column(name = "f_data_type", nullable = false, length = 50, insertable = false, updatable = false)
	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	@Column(name = "f_data_id", nullable = false)
	public Integer getDataId() {
		return this.dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_begin_date", nullable = false, length = 19)
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

	@Column(name = "f_is_rejection", nullable = false)
	public Boolean getRejection() {
		return rejection;
	}

	public void setRejection(Boolean rejection) {
		this.rejection = rejection;
	}

	@Column(name = "f_is_end", nullable = false)
	public Boolean getEnd() {
		return this.end;
	}

	public void setEnd(Boolean end) {
		this.end = end;
	}
}

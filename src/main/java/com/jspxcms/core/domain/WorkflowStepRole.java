package com.jspxcms.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.Objects;
import com.jspxcms.core.domain.WorkflowStepRole.WorkflowStepRoleId;

@Entity
@Table(name = "cms_workflowstep_role")
@IdClass(WorkflowStepRoleId.class)
public class WorkflowStepRole implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getRoleIndex() == null) {
			setRoleIndex(0);
		}
	}

	public WorkflowStepRole() {
	}

	public WorkflowStepRole(WorkflowStep step, Role role) {
		this.step = step;
		this.role = role;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_role_id", nullable = false)
	private Role role;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_workflowstep_id", nullable = false)
	private WorkflowStep step;

	@Column(name = "f_role_index", nullable = false)
	private Integer roleIndex;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public WorkflowStep getStep() {
		return step;
	}

	public void setStep(WorkflowStep step) {
		this.step = step;
	}

	public Integer getRoleIndex() {
		return roleIndex;
	}

	public void setRoleIndex(Integer roleIndex) {
		this.roleIndex = roleIndex;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof WorkflowStepRole)) {
			return false;
		}
		WorkflowStepRole that = (WorkflowStepRole) o;
		return Objects.equal(role, that.role) && Objects.equal(step, that.step);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(role, step);
	}

	public static class WorkflowStepRoleId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer role;
		Integer step;

		public WorkflowStepRoleId() {
		}

		public WorkflowStepRoleId(Integer role, Integer step) {
			this.role = role;
			this.step = step;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof WorkflowStepRoleId)) {
				return false;
			}
			WorkflowStepRoleId that = (WorkflowStepRoleId) o;
			return Objects.equal(role, that.role) && Objects.equal(step, that.step);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(role, step);
		}
	}
}

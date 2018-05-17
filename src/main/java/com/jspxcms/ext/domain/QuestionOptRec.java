package com.jspxcms.ext.domain;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.jspxcms.ext.domain.QuestionOptRec.QuestionOptRecId;

@Entity
@Table(name = "cms_question_opt_rec")
@IdClass(QuestionOptRecId.class)
public class QuestionOptRec implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public QuestionOptRec() {
	}

	public QuestionOptRec(QuestionOption option, QuestionRecord record) {
		this.option = option;
		this.record = record;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_questionoption_id", nullable = false)
	private QuestionOption option;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_questionrecord_id", nullable = false)
	private QuestionRecord record;

	public QuestionOption getOption() {
		return this.option;
	}

	public void setOption(QuestionOption option) {
		this.option = option;
	}

	public QuestionRecord getRecord() {
		return this.record;
	}

	public void setRecord(QuestionRecord record) {
		this.record = record;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof QuestionOptRec)) {
			return false;
		}
		QuestionOptRec that = (QuestionOptRec) o;
		return Objects.equal(option, that.option) && Objects.equal(record, that.record);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(option, record);
	}

	public static class QuestionOptRecId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer option;
		Integer record;

		public QuestionOptRecId() {
		}

		public QuestionOptRecId(Integer option, Integer record) {
			this.option = option;
			this.record = record;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof QuestionOptRecId)) {
				return false;
			}
			QuestionOptRecId that = (QuestionOptRecId) o;
			return Objects.equal(option, that.option) && Objects.equal(record, that.record);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(option, record);
		}
	}

	@SuppressWarnings("serial")
	public static class QuestionOptRecComparator implements Comparator<QuestionOptRec>,Serializable {
		public int compare(QuestionOptRec o1, QuestionOptRec o2) {
			return o1.getRecord().getDate().compareTo(o2.getRecord().getDate());
		}
	}
}

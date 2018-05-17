package com.jspxcms.ext.domain;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.jspxcms.ext.domain.QuestionItemRec.QuestionItemRecId;

@Entity
@Table(name = "cms_question_item_rec")
@IdClass(QuestionItemRecId.class)
public class QuestionItemRec implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public QuestionItemRec() {
	}

	public QuestionItemRec(QuestionItem item, QuestionRecord record) {
		this.item = item;
		this.record = record;
	}

	public QuestionItemRec(QuestionItem item, QuestionRecord record, String answer) {
		this.item = item;
		this.record = record;
		this.answer = answer;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_questionitem_id", nullable = false)
	private QuestionItem item;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_questionrecord_id", nullable = false)
	private QuestionRecord record;

	@Column(name = "f_answer", length = 2000)
	private String answer;

	public QuestionItem getItem() {
		return this.item;
	}

	public void setItem(QuestionItem item) {
		this.item = item;
	}

	public QuestionRecord getRecord() {
		return this.record;
	}

	public void setRecord(QuestionRecord record) {
		this.record = record;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof QuestionItemRec)) {
			return false;
		}
		QuestionItemRec that = (QuestionItemRec) o;
		return Objects.equal(item, that.item) && Objects.equal(record, that.record);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(item, record);
	}

	public static class QuestionItemRecId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer record;
		Integer item;

		public QuestionItemRecId() {
		}

		public QuestionItemRecId(Integer item, Integer record) {
			this.item = item;
			this.record = record;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof QuestionItemRecId)) {
				return false;
			}
			QuestionItemRecId that = (QuestionItemRecId) o;
			return Objects.equal(item, that.item) && Objects.equal(record, that.record);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(item, record);
		}
	}

	@SuppressWarnings("serial")
	public static class QuestionItemRecComparator implements Comparator<QuestionItemRec>,Serializable {
		public int compare(QuestionItemRec o1, QuestionItemRec o2) {
			return o1.getRecord().getDate().compareTo(o2.getRecord().getDate());
		}
	}
}

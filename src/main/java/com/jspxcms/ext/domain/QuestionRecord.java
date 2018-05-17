package com.jspxcms.ext.domain;

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

import com.google.common.base.Objects;
import com.jspxcms.core.domain.User;

@Entity
@Table(name = "cms_question_record")
public class QuestionRecord implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
		if (getDate() == null) {
			setDate(new Timestamp(System.currentTimeMillis()));
		}
	}

	public void addItem(QuestionItem item, String answer) {
		QuestionItemRec bean = new QuestionItemRec(item, this, answer);
		itemRecs.add(bean);
		item.getItemRecs().add(bean);
	}

	public void removeItem(QuestionItem item) {
		QuestionItemRec bean = new QuestionItemRec(item, this);
		item.getItemRecs().remove(bean);
		itemRecs.remove(bean);
		bean.setItem(null);
		bean.setRecord(null);
	}

	public void addOption(QuestionOption option) {
		QuestionOptRec bean = new QuestionOptRec(option, this);
		optRecs.add(bean);
		option.getOptRecs().add(bean);
	}

	public void removeOption(QuestionOption option) {
		QuestionOptRec bean = new QuestionOptRec(option, this);
		option.getOptRecs().remove(bean);
		optRecs.remove(bean);
		bean.setOption(null);
		bean.setRecord(null);
	}

	private Integer id;
	private Set<QuestionItemRec> itemRecs = new HashSet<QuestionItemRec>(0);
	private Set<QuestionOptRec> optRecs = new HashSet<QuestionOptRec>(0);

	private Question question;
	private User user;

	private Date date;
	private String ip;
	private String cookie;

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof QuestionRecord)) {
			return false;
		}
		QuestionRecord that = (QuestionRecord) o;
		return Objects.equal(id, that.id);
	}

	@Id
	@Column(name = "f_questionrecord_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_question_record", pkColumnValue = "cms_question_record", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_question_record")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "record")
	public Set<QuestionItemRec> getItemRecs() {
		return itemRecs;
	}

	public void setItemRecs(Set<QuestionItemRec> itemRecs) {
		this.itemRecs = itemRecs;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "record")
	public Set<QuestionOptRec> getOptRecs() {
		return optRecs;
	}

	public void setOptRecs(Set<QuestionOptRec> optRecs) {
		this.optRecs = optRecs;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_question_id", nullable = false)
	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_date", nullable = false)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "f_ip", nullable = false, length = 100)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "f_cookie", nullable = false, length = 100)
	public String getCookie() {
		return this.cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}

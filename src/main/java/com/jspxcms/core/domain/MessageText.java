package com.jspxcms.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.common.base.Objects;

/**
 * The persistent class for the cms_message_text database table.
 * 
 */
@Entity
@Table(name = "cms_message_text")
public class MessageText implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof MessageText)) {
			return false;
		}
		MessageText bean = (MessageText) o;
		return Objects.equal(id, bean.getId());
	}

	@Id
	private Integer id;

	@MapsId
	@OneToOne
	@JoinColumn(name = "message_id_")
	private Message message;

	@Column(name = "subject_")
	private String subject;

	@Lob
	@Column(name = "text_")
	private String text;

	public MessageText() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
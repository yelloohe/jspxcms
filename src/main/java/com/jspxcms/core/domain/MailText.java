package com.jspxcms.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

/**
 * The persistent class for the cms_mail_text database table.
 * 
 */
@Entity
@Table(name = "cms_mail_text")
public class MailText implements Serializable {
	private static final long serialVersionUID = 1L;

	@Transient
	public void applyDefaultValue() {
	}

	@Id
	@TableGenerator(name = "cms_mail_text", pkColumnValue = "cms_mail_text", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "cms_mail_text")
	@Column(name = "mailtext_id_")
	private Integer id;

	@Column(name = "subject_")
	private String subject;

	@Column(name = "text_")
	private String text;

	public MailText() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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
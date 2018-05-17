package com.jspxcms.core.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

/**
 * The persistent class for the cms_mail_inbox database table.
 * 
 */
@Entity
@Table(name = "cms_mail_inbox")
public class MailInbox implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 获取标题。如果主题存在，则获取主题；如果主题不存在，则获取正文前150字符。
	 * 
	 * @return
	 */
	@Transient
	public String getTitle() {
		if (StringUtils.isNotBlank(getSubject())) {
			return getSubject();
		} else {
			return StringUtils.substring(getText(), 0, 150);
		}
	}

	@Transient
	public String getSubject() {
		MailText mailText = getMailText();
		return mailText != null ? mailText.getSubject() : null;
	}

	@Transient
	public String getText() {
		MailText mailText = getMailText();
		return mailText != null ? mailText.getText() : null;
	}

	@Transient
	public void applyDefaultValue() {
		if (getUnread() == null) {
			setUnread(true);
		}
		if (getReceiveTime() == null) {
			setReceiveTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	public MailInbox() {
	}

	public MailInbox(MailOutbox outbox) {
		setOutbox(outbox);
		setSender(outbox.getSender());
		setMailText(outbox.getMailText());
	}

	@Id
	@TableGenerator(name = "cms_mail_inbox", pkColumnValue = "cms_mail_inbox", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "cms_mail_inbox")
	@Column(name = "mailinbox_id_")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mailoutbox_id_")
	private MailOutbox outbox;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mailtext_id_")
	private MailText mailText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id_")
	private User receiver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id_")
	private User sender;

	@Column(name = "is_unread_")
	private Boolean unread;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "receive_time_")
	private Date receiveTime;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getUnread() {
		return this.unread;
	}

	public void setUnread(Boolean unread) {
		this.unread = unread;
	}

	public Date getReceiveTime() {
		return this.receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public MailOutbox getOutbox() {
		return this.outbox;
	}

	public void setOutbox(MailOutbox outbox) {
		this.outbox = outbox;
	}

	public MailText getMailText() {
		return this.mailText;
	}

	public void setMailText(MailText mailText) {
		this.mailText = mailText;
	}

	public User getReceiver() {
		return this.receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public User getSender() {
		return this.sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

}
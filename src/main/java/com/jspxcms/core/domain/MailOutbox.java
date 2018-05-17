package com.jspxcms.core.domain;

import java.io.Serializable;
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

import org.apache.commons.lang3.StringUtils;

/**
 * The persistent class for the cms_mail_outbox database table.
 * 
 */
@Entity
@Table(name = "cms_mail_outbox")
public class MailOutbox implements Serializable {
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

	/**
	 * 已读百分比。范围从0-100。
	 * 
	 * @return
	 */
	@Transient
	public float getReadPercent() {
		if (receiverNumber != null && receiverNumber > 0 && readNumber != null && readNumber > 0) {
			return (float) readNumber * 100 / receiverNumber;
		} else {
			return 0;
		}
	}

	@Transient
	public void applyDefaultValue() {
		if (getSendTime() == null) {
			setSendTime(new Timestamp(System.currentTimeMillis()));
		}
		if (getReceiverNumber() == null) {
			setReceiverNumber(0);
		}
		if (getReadNumber() == null) {
			setReadNumber(0);
		}
	}

	@Id
	@TableGenerator(name = "cms_mail_outbox", pkColumnValue = "cms_mail_outbox", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "cms_mail_outbox")
	@Column(name = "mailoutbox_id_")
	private Integer id;

	@OneToMany(mappedBy = "outbox", cascade = CascadeType.REMOVE)
	private Set<MailInbox> inboxs = new HashSet<MailInbox>(0);

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "mailtext_id_")
	private MailText mailText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id_")
	private User sender;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "send_time_")
	private Date sendTime;

	@Column(name = "receiver_number_")
	private Integer receiverNumber;

	@Column(name = "read_number_")
	private Integer readNumber;

	public MailOutbox() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReceiverNumber() {
		return this.receiverNumber;
	}

	public void setReceiverNumber(Integer receiverNumber) {
		this.receiverNumber = receiverNumber;
	}

	public Integer getReadNumber() {
		return readNumber;
	}

	public void setReadNumber(Integer readNumber) {
		this.readNumber = readNumber;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Set<MailInbox> getInboxs() {
		return this.inboxs;
	}

	public void setInboxs(Set<MailInbox> inboxs) {
		this.inboxs = inboxs;
	}

	public MailInbox addInbox(MailInbox inbox) {
		getInboxs().add(inbox);
		inbox.setOutbox(this);

		return inbox;
	}

	public MailInbox removeInbox(MailInbox inbox) {
		getInboxs().remove(inbox);
		inbox.setOutbox(null);

		return inbox;
	}

	public MailText getMailText() {
		return this.mailText;
	}

	public void setMailText(MailText mailText) {
		this.mailText = mailText;
	}

	public User getSender() {
		return this.sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

}
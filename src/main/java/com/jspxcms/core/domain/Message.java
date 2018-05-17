package com.jspxcms.core.domain;

import static com.jspxcms.core.constant.Constants.DYNAMIC_SUFFIX;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import com.jspxcms.core.constant.Constants;

/**
 * The persistent class for the cms_message database table.
 * 
 */
@Entity
@Table(name = "cms_message")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 通知类型
	 */
	public static final String NOTIFICATION_TYPE = "CMS_MESSAGE";
	/**
	 * 删除标志：未删除
	 */
	public static final int DELETION_NO = 0;
	/**
	 * 删除标志：发件人删除
	 */
	public static final int DELETION_SEND = 1;
	/**
	 * 删除标志：收件人删除
	 */
	public static final int DELETION_RECEIVE = 2;

	@Transient
	public String getUrl() {
		return getUrl(false, false);
	}

	@Transient
	public String getFullUrl() {
		return getUrl(true, false);
	}

	@Transient
	public String getBackendUrl() {
		return getUrl(false, true);
	}

	@Transient
	public String getBackendFullUrl() {
		return getUrl(true, true);
	}

	@Transient
	public String getUrl(boolean isFull, boolean isBackend) {
		Site site = getReceiver().getGlobal().getSite();
		StringBuilder sb = new StringBuilder();
		if (isFull) {
			sb.append(site.getProtocol()).append("://").append(site.getDomain());
			if (site.getPort() != null) {
				sb.append(":").append(site.getPort());
			}
		}
		String ctx = site.getContextPath();
		if (StringUtils.isNotBlank(ctx)) {
			sb.append(ctx);
		}
		if (isBackend) {
			sb.append(Constants.CMSCP).append("/core/homepage/message_contact.do?contactId=").append(getSender().getId());
		} else {
			sb.append("/my/message/contact/").append(getSender().getId()).append(DYNAMIC_SUFFIX);
		}
		return sb.toString();
	}

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
		MessageText messageText = getMessageText();
		return messageText != null ? messageText.getSubject() : null;
	}

	@Transient
	public String getText() {
		MessageText messageText = getMessageText();
		return messageText != null ? messageText.getText() : null;
	}

	@Transient
	public void applyDefaultValue() {
		if (getSendTime() == null) {
			setSendTime(new Timestamp(System.currentTimeMillis()));
		}
		if (getDeletionFlag() == null) {
			setDeletionFlag(DELETION_NO);
		}
		if (getUnread() == null) {
			setUnread(true);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Message)) {
			return false;
		}
		Message bean = (Message) o;
		return Objects.equal(id, bean.getId());
	}

	@Id
	@TableGenerator(name = "tg_cms_message", pkColumnValue = "cms_message", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_message")
	@Column(name = "message_id_")
	private Integer id;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "message", fetch = FetchType.LAZY)
	private MessageText messageText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id_")
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id_")
	private User receiver;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "send_time_", nullable = false)
	private Date sendTime;

	@Column(name = "deletion_flag_", nullable = false)
	private Integer deletionFlag;

	@Column(name = "is_unread_", nullable = false)
	private Boolean unread;

	public Message() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public MessageText getMessageText() {
		return this.messageText;
	}

	public void setMessageText(MessageText messageText) {
		this.messageText = messageText;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getDeletionFlag() {
		return this.deletionFlag;
	}

	public void setDeletionFlag(Integer deletionFlag) {
		this.deletionFlag = deletionFlag;
	}

	public Boolean getUnread() {
		return this.unread;
	}

	public void setUnread(Boolean unread) {
		this.unread = unread;
	}
}
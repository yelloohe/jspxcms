package com.jspxcms.ext.domain;

// Generated 2013-6-27 14:37:46 by Hibernate Tools 4.0.0

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.support.Siteable;

/**
 * Guestbook
 * 
 * @author yangxing
 * 
 */
@Entity
@Table(name = "cms_guestbook")
public class Guestbook implements Siteable, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 已审核
	 */
	public static final int AUDITED = 0;
	/**
	 * 未审核
	 */
	public static final int UNAUDIT = 1;
	/**
	 * 屏蔽
	 */
	public static final int SHIELD = 2;

	public void applyDefaultValue() {
		if (getCreationDate() == null) {
			setCreationDate(new Timestamp(System.currentTimeMillis()));
		}
		if (getStatus() == null) {
			setStatus(AUDITED);
		}
		if (getRecommend() == null) {
			setRecommend(false);
		}
	}

	private Integer id;
	/**
	 * 留言类别
	 */
	private GuestbookType type;
	/**
	 * 站点
	 */
	private Site site;
	/**
	 * 创建者
	 */
	private User creator;
	/**
	 * 回复者
	 */
	private User replyer;

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 正文
	 */
	private String text;
	/**
	 * 创建时间
	 */
	private Date creationDate;
	/**
	 * 创建IP
	 */
	private String creationIp;
	/**
	 * 创建国家
	 */
	private String creationCountry;
	/**
	 * 创建地区
	 */
	private String creationArea;
	/**
	 * 回复正文
	 */
	private String replyText;
	/**
	 * 回复日期
	 */
	private Date replyDate;
	/**
	 * 回复IP
	 */
	private String replyIp;
	/**
	 * 回复国家
	 */
	private String replyCountry;
	/**
	 * 回复地区
	 */
	private String replyArea;
	/**
	 * 是否回复
	 */
	private Boolean reply;
	/**
	 * 是否推荐
	 */
	private Boolean recommend;
	/**
	 * 状态。0:已审核;1:未审核;2:屏蔽
	 */
	private Integer status;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 性别。true:男;false:女
	 */
	private Boolean gender;
	/**
	 * 电话号码
	 */
	private String phone;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * QQ号码
	 */
	private String qq;
	/**
	 * 电子邮箱
	 */
	private String email;

	@Id
	@Column(name = "f_guestbook_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_guestbook", pkColumnValue = "cms_guestbook", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_guestbook")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_guestbooktype_id", nullable = false)
	public GuestbookType getType() {
		return this.type;
	}

	public void setType(GuestbookType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_site_id", nullable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_creator_id", nullable = false)
	public User getCreator() {
		return this.creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_replyer_id", nullable = false)
	public User getReplyer() {
		return this.replyer;
	}

	public void setReplyer(User replyer) {
		this.replyer = replyer;
	}

	@Column(name = "f_title", length = 150)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob
	@Column(name = "f_text")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_creation_date", nullable = false, length = 19)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "f_creation_ip", nullable = false, length = 100)
	public String getCreationIp() {
		return this.creationIp;
	}

	public void setCreationIp(String creationIp) {
		this.creationIp = creationIp;
	}

	@Column(name = "f_creation_country", length = 100)
	public String getCreationCountry() {
		return creationCountry;
	}

	public void setCreationCountry(String creationCountry) {
		this.creationCountry = creationCountry;
	}

	@Column(name = "f_creation_area", length = 100)
	public String getCreationArea() {
		return creationArea;
	}

	public void setCreationArea(String creationArea) {
		this.creationArea = creationArea;
	}

	@Lob
	@Column(name = "f_reply_text")
	public String getReplyText() {
		return this.replyText;
	}

	public void setReplyText(String replyText) {
		this.replyText = replyText;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "f_reply_date", length = 19)
	public Date getReplyDate() {
		return this.replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

	@Column(name = "f_reply_ip", length = 100)
	public String getReplyIp() {
		return this.replyIp;
	}

	public void setReplyIp(String replyIp) {
		this.replyIp = replyIp;
	}

	@Column(name = "f_reply_country", length = 100)
	public String getReplyCountry() {
		return replyCountry;
	}

	public void setReplyCountry(String replyCountry) {
		this.replyCountry = replyCountry;
	}

	@Column(name = "f_reply_area", length = 100)
	public String getReplyArea() {
		return replyArea;
	}

	public void setReplyArea(String replyArea) {
		this.replyArea = replyArea;
	}

	@Column(name = "f_is_reply", nullable = false, length = 1)
	public Boolean getReply() {
		return this.reply;
	}

	public void setReply(Boolean reply) {
		this.reply = reply;
	}

	@Column(name = "f_is_recommend", nullable = false, length = 1)
	public Boolean getRecommend() {
		return this.recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	@Column(name = "f_status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "f_username", length = 100)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "f_gender", length = 1)
	public Boolean getGender() {
		return this.gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	@Column(name = "f_phone", length = 100)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "f_mobile", length = 100)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "f_qq", length = 100)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "f_email", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

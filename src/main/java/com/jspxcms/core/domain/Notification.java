package com.jspxcms.core.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.jspxcms.common.util.Strings;
import com.jspxcms.core.constant.Constants;

/**
 * The persistent class for the cms_notification database table.
 * 
 */
@Entity
@Table(name = "cms_notification")
public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MULTI_NUMBER = 3;

	public static final String URL = "{url}";
	public static final String URL_TARGET = "{urlTarget}";

	public Notification() {
	}

	public Notification(String type, Integer key, User receiver) {
		this.type = type;
		this.key = key;
		this.receiver = receiver;
	}

	/**
	 * 获取消息内容， {url}替换成url的值
	 * 
	 * @return
	 */
	@Transient
	public String getContent() {
		String content = StringUtils.replace(text, "{id}", String.valueOf(id));
		content = StringUtils.replace(content, URL, url);
		return content;
	}

	/**
	 * 获取消息内容，{url}替换成backendUrl的值
	 * 
	 * @return
	 */
	@Transient
	public String getContentBackend() {
		String bu = backendUrl;
		if (StringUtils.isBlank(bu)) {
			bu = url;
		}
		String content = StringUtils.replace(text, "{id}", String.valueOf(id));
		content = StringUtils.replace(content, URL, bu);
		return content;
	}

	@Transient
	public void addSource(String sourceName, String sourceUrl) {
		String template = Constants.NOTIFICATION_SOURCE;
		template = StringUtils.replace(template, "{sourceName}", sourceName);
		template = StringUtils.replace(template, "{sourceUrl}", sourceUrl);
		if (!sources.contains(template)) {
			sources.add(template);
		}
	}

	@Transient
	public void processText(String template, String message, String targetName, String targetUrl) {
        message = StringEscapeUtils.escapeHtml4(message);
        StringBuilder sourcesBuilder = new StringBuilder();
		int n = sources.size();
		// 后添加的后面，倒叙获取
		for (int i = n - 1, min = i - MULTI_NUMBER; i >= 0 && i > min; i--) {
			sourcesBuilder.append(sources.get(i)).append(", ");
		}
		if (sourcesBuilder.length() >= 2) {
			sourcesBuilder.setLength(sourcesBuilder.length() - 2);
		}
		if (sources.size() > MULTI_NUMBER) {
			sourcesBuilder.append(StringUtils.replace(Constants.NOTIFICATION_SOURCE_MULTI, "{n}", String.valueOf(n)));
		}
		template = StringUtils.replace(template, "{sources}", sourcesBuilder.toString());
		template = StringUtils.replace(template, "{qty}", String.valueOf(qty));
		template = StringUtils.replace(template, "{message}", Strings.substring(message, 20, "..."));
		template = StringUtils.replace(template, "{targetName}", targetName);
		template = StringUtils.replace(template, "{targetUrl}", targetUrl);
		setText(template);
	}

	@Transient
	public void applyDefaultValue() {
		if (getSendTime() == null) {
			setSendTime(new Timestamp(System.currentTimeMillis()));
		}
	}

	@Id
	@TableGenerator(name = "cms_notification", pkColumnValue = "cms_notification", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "cms_notification")
	@Column(name = "notification_id_")
	private Integer id;

	@ElementCollection
	@CollectionTable(name = "cms_notification_source", joinColumns = @JoinColumn(name = "notification_id_"))
	@Column(name = "source_", nullable = false)
	@OrderColumn(name = "source_order_")
	private List<String> sources = new ArrayList<String>(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id_", nullable = false)
	private User receiver;

	@Column(name = "type_", nullable = false)
	private String type;

	@Column(name = "key_", nullable = false)
	private Integer key;

	@Column(name = "qty_", nullable = false)
	private int qty = 0;

	@Column(name = "url_")
	private String url;

	@Column(name = "backend_url_")
	private String backendUrl;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "send_time_")
	private Date sendTime;

	@Column(name = "text_")
	private String text;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	public String getBackendUrl() {
		return this.backendUrl;
	}

	public void setBackendUrl(String backendUrl) {
		this.backendUrl = backendUrl;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

}
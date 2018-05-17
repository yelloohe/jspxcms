package com.jspxcms.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

/**
 * SensitiveWord 敏感词
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_sensitive_word")
public class SensitiveWord implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 启用
	 */
	public static final int NORMAL = 0;
	/**
	 * 禁用
	 */
	public static final int DISABLED = 1;

	@Transient
	public void applyDefaultValue() {
		if (getStatus() == null) {
			setStatus(NORMAL);
		}
	}

	private Integer id;
	private String name;
	private String replacement;
	private Integer status;

	@Id
	@Column(name = "f_sensitiveword_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_sensitive_word", pkColumnValue = "cms_sensitive_word", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_sensitive_word")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "f_name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_replacement", length = 100)
	public String getReplacement() {
		return this.replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	@Column(name = "f_status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}

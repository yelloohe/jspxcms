package com.jspxcms.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;

import com.jspxcms.common.file.FilesEx;

/**
 * InfoFile
 * 
 * @author liufang
 * 
 */
@Embeddable
public class SpecialFile implements Serializable {
	private static final long serialVersionUID = 1L;

	public SpecialFile() {
	}

	public SpecialFile(String name, String file, Long length) {
		this.name = name;
		this.file = file;
		this.length = length;
		this.downloads = 0;
	}

	@Transient
	public String getSize() {
		Long length = getLength();
		return FilesEx.getSize(length);
	}

	@Transient
	public String getExtension() {
		return FilenameUtils.getExtension(getName());
	}

	private String name;
	private Long length;

	private String file;
	private Integer downloads;

	@Column(name = "f_name", nullable = false, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_length", nullable = false)
	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	@Column(name = "f_file", nullable = false)
	public String getFile() {
		return this.file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Column(name = "f_downloads", nullable = false)
	public Integer getDownloads() {
		return this.downloads;
	}

	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}
}

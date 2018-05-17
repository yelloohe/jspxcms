package com.jspxcms.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.jspxcms.common.web.Anchor;

/**
 * InfoComment
 * 
 * @author liufang
 * 
 */
@Entity
@DiscriminatorValue("Info")
public class InfoComment extends Comment {
	private static final long serialVersionUID = 1L;

	@Override
	@Transient
	public Anchor getAnchor() {
		return info;
	}

	private Info info;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_fid", nullable = false, insertable = false, updatable = false)
	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}
}

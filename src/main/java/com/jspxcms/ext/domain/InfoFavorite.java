package com.jspxcms.ext.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.jspxcms.common.web.Anchor;
import com.jspxcms.core.domain.Info;

@Entity
@DiscriminatorValue("InfoFavorite")
public class InfoFavorite extends Favorite {
	private static final long serialVersionUID = 1L;

	@Override
	public Anchor getAnchor() {
		return info;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "did_", nullable = false)
	private Info info;

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

}
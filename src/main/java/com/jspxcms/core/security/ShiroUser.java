package com.jspxcms.core.security;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 * 
 * @author liufang
 * 
 */
public class ShiroUser implements Serializable {
	private static final long serialVersionUID = 1L;
	public Integer id;
	public String username;

	public ShiroUser(Integer id, String username) {
		this.id = id;
		this.username = username;
	}

	@Override
	public String toString() {
		return username;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "username");
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, "username");
	}
}

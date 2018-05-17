package com.jspxcms.common.security;

import org.apache.commons.lang3.StringUtils;

/**
 * 明文证书
 * 
 * @author liufang
 * 
 */
public class PlainCredentialsDigest {
	public String encodePassword(String rawPass, String salt) {
		if (StringUtils.isNotBlank(rawPass)) {
			return rawPass;
		} else {
			return null;
		}
	}

	public boolean isPasswordValid(String encPass, String rawPass, String salt) {
		if (StringUtils.isBlank(encPass) && StringUtils.isBlank(rawPass)) {
			return true;
		}
		return StringUtils.equals(encPass, rawPass);
	}
}

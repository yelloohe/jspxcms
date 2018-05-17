package com.jspxcms.common.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.Assert;

/**
 * 证书加密适配器
 * 
 * @author liufang
 * 
 */
public class CredentialsMatcherAdapter implements CredentialsMatcher {
	private CredentialsDigest credentialsDigest;

	public CredentialsMatcherAdapter(CredentialsDigest credentialsDigest) {
		Assert.notNull(credentialsDigest);
		this.credentialsDigest = credentialsDigest;
	}

	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		String plainCredentials, credentials;
		byte[] saltByte = null;
		Object tokenCredentials = token.getCredentials();
		if (tokenCredentials == null) {
			plainCredentials = null;
		} else if (tokenCredentials instanceof char[]) {
			plainCredentials = new String((char[]) tokenCredentials);
		} else if (tokenCredentials instanceof String) {
			plainCredentials = (String) tokenCredentials;
		} else {
			throw new IllegalArgumentException(
					"credentials only support String or char[].");
		}
		if (info instanceof SaltedAuthenticationInfo) {
			Object salt = ((SaltedAuthenticationInfo) info)
					.getCredentialsSalt();
			if (salt == null) {
				saltByte = null;
			} else if (salt instanceof ByteSource) {
				saltByte = ((ByteSource) salt).getBytes();
			} else if (salt instanceof byte[]) {
				saltByte = (byte[]) salt;
			} else {
				throw new IllegalArgumentException("salt only support byte[].");
			}
		}
		Object infoCredentials = info.getCredentials();
		if (infoCredentials == null) {
			credentials = null;
		} else if (infoCredentials instanceof String) {
			credentials = (String) infoCredentials;
		} else if (infoCredentials instanceof char[]) {
			credentials = new String((char[]) infoCredentials);
		} else {
			throw new IllegalArgumentException(
					"credentials only support String or char[].");
		}
		return credentialsDigest.matches(credentials, plainCredentials,
				saltByte);
	}
}

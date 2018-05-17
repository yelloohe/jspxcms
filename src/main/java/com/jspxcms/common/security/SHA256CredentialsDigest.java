package com.jspxcms.common.security;

/**
 * SHA256证书加密
 * 
 * @author liufang
 * 
 */
public class SHA256CredentialsDigest extends HashCredentialsDigest implements
		CredentialsDigest {
	@Override
	protected byte[] digest(byte[] input, byte[] salt) {
		return Digests.sha256(input, salt, HASH_INTERATIONS);
	}
}
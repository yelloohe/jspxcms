package com.jspxcms.core.security.oauth;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 保存在Session中的OAuthToken没有找到
 * 
 * @author liufang
 *
 */
public class SessionTokenNotFoundException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new AuthenticationException.
	 */
	public SessionTokenNotFoundException() {
		super();
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param message
	 *            the reason for the exception
	 */
	public SessionTokenNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public SessionTokenNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param message
	 *            the reason for the exception
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public SessionTokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}

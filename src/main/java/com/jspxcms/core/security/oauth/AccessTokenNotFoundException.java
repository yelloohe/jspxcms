package com.jspxcms.core.security.oauth;

import org.apache.shiro.authc.AuthenticationException;

/**
 * OAUTH的Access Token没有找到
 * 
 * @author liufang
 *
 */
public class AccessTokenNotFoundException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new AuthenticationException.
	 */
	public AccessTokenNotFoundException() {
		super();
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param message
	 *            the reason for the exception
	 */
	public AccessTokenNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public AccessTokenNotFoundException(Throwable cause) {
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
	public AccessTokenNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}

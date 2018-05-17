package com.jspxcms.core.security.oauth;

import org.apache.shiro.authc.AuthenticationException;

/**
 * OAUTH的Open ID没有找到
 * 
 * @author liufang
 *
 */
public class OpenIDNotFoundException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new AuthenticationException.
	 */
	public OpenIDNotFoundException() {
		super();
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param message
	 *            the reason for the exception
	 */
	public OpenIDNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public OpenIDNotFoundException(Throwable cause) {
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
	public OpenIDNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}

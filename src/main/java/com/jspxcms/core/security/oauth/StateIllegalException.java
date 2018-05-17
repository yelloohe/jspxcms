package com.jspxcms.core.security.oauth;

import org.apache.shiro.authc.AuthenticationException;

/**
 * OAUTH的State不合法
 * 
 * @author liufang
 *
 */
public class StateIllegalException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new AuthenticationException.
	 */
	public StateIllegalException() {
		super();
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param message
	 *            the reason for the exception
	 */
	public StateIllegalException(String message) {
		super(message);
	}

	/**
	 * Constructs a new AuthenticationException.
	 *
	 * @param cause
	 *            the underlying Throwable that caused this exception to be
	 *            thrown.
	 */
	public StateIllegalException(Throwable cause) {
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
	public StateIllegalException(String message, Throwable cause) {
		super(message, cause);
	}
}

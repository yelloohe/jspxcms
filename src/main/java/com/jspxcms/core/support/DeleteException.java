package com.jspxcms.core.support;

/**
 * DeleteException 删除异常
 * 
 * @author liufang
 * 
 */
public class DeleteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DeleteException() {
		super();
	}

	public DeleteException(String s) {
		super(s);
	}

	public DeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeleteException(Throwable cause) {
		super(cause);
	}
}

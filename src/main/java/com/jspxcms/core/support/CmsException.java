package com.jspxcms.core.support;

/**
 * CmsException CMS异常
 * 
 * @author liufang
 * 
 */
public class CmsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String[] args;

	public CmsException() {
		super();
	}

	public CmsException(String s) {
		super(s);
	}

	public CmsException(String s, String... args) {
		super(s);
		this.args = args;
	}

	public CmsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CmsException(Throwable cause) {
		super(cause);
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

}

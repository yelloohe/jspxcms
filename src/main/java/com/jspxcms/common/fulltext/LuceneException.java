package com.jspxcms.common.fulltext;

import org.springframework.core.NestedRuntimeException;

/**
 * Lucene异常
 * 
 * @author liufang
 * 
 */
public class LuceneException extends NestedRuntimeException {
	private static final long serialVersionUID = 1L;

	public LuceneException(String msg) {
		super(msg);
	}

	public LuceneException(String msg, Throwable ex) {
		super(msg, ex);
	}
}

package com.jspxcms.common.web.tags;

/**
 * RadiosTag
 * 
 * @author liufang
 * 
 */
public class RadiosTag extends AbstractMultiCheckedElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getInputType() {
		return "radio";
	}
}

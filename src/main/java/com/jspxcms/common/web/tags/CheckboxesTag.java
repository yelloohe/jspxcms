package com.jspxcms.common.web.tags;

/**
 * CheckboxesTag
 * 
 * @author liufang
 * 
 */
public class CheckboxesTag extends AbstractMultiCheckedElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getInputType() {
		return "checkbox";
	}
}

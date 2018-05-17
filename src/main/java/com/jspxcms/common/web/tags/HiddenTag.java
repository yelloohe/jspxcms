package com.jspxcms.common.web.tags;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

/**
 * HiddenTag
 * 
 * @author liufang
 * 
 */
public class HiddenTag extends AbstractHtmlElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter writer) throws JspException {
		writer.startTag("input");
		writer.writeAttribute("type", "hidden");
		writer.writeOptionalAttributeValue("id", getId());
		writer.writeOptionalAttributeValue("name", getName());
		writer.writeOptionalAttributeValue("value", getVal());
		writeOptionalAttributes(writer);
		writer.endTag();
		return SKIP_BODY;
	}

	protected String getVal() {
		if (getValue() != null) {
			return HtmlUtils.htmlEscape(getValue().toString());
		} else if (getDefault() != null) {
			return HtmlUtils.htmlEscape(getDefault().toString());
		} else {
			return null;
		}
	}

	private String name;
	private Object value;
	private Object def;

	protected String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	protected Object getDefault() {
		return def;
	}

	public void setDefault(Object def) {
		this.def = def;
	}

}

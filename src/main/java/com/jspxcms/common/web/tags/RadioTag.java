package com.jspxcms.common.web.tags;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

/**
 * RadioTag
 * 
 * @author liufang
 * 
 */
public class RadioTag extends AbstractHtmlInputElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter writer) throws JspException {
		writer.startTag("input");
		writer.writeAttribute("type", "radio");
		writer.writeAttribute("value", getVal());
		writer.writeOptionalAttributeValue("id", getId());
		writer.writeOptionalAttributeValue("name", getName());
		if (isChecked()) {
			writer.writeAttribute("checked", "checked");
		}
		writeOptionalAttributes(writer);
		writer.endTag();
		return SKIP_BODY;
	}

	protected String getVal() {
		return value != null ? HtmlUtils.htmlEscape(value.toString()) : null;
	}

	protected boolean isChecked() {
		String checkedValue = null;
		if (checked != null) {
			checkedValue = checked.toString();
		} else if (def != null) {
			checkedValue = def.toString();
		}
		if (checkedValue != null && value != null) {
			return checkedValue.equals(value.toString());
		} else {
			return false;
		}
	}

	private String name;
	// required
	private Object value;
	private Object checked;
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

	protected Object getChecked() {
		return checked;
	}

	public void setChecked(Object checked) {
		this.checked = checked;
	}

	protected Object getDefault() {
		return def;
	}

	public void setDefault(Object def) {
		this.def = def;
	}

}

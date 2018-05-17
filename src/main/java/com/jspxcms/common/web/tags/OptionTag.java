package com.jspxcms.common.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

/**
 * OptionTag
 * 
 * @author liufang
 * 
 */
public class OptionTag extends AbstractHtmlElementBodyTag {
	private static final long serialVersionUID = 1L;

	private void renderOption(String body, TagWriter writer)
			throws JspException {
		writer.startTag("option");
		writer.writeAttribute("value", getVal());
		if (isSelected(body)) {
			writer.writeOptionalAttributeValue("selected", "selected");
		}
		writer.writeOptionalAttributeValue(DISABLED_ATTRIBUTE, getDisabled());
		writeOptionalAttributes(writer);
		writer.appendValue(StringUtils.isNotEmpty(body) ? body : getVal());
		writer.endTag();
	}

	@Override
	protected void renderFromBodyContent(BodyContent bodyContent,
			TagWriter writer) throws JspException {
		String body = bodyContent.getString();
		renderOption(body, writer);
	}

	@Override
	protected void renderDefaultContent(TagWriter writer) throws JspException {
		renderOption(null, writer);
	}

	protected String getVal() {
		if (getValue() != null) {
			return HtmlUtils.htmlEscape(getValue().toString());
		} else {
			return null;
		}
	}

	protected boolean isSelected(String body) {
		String selectedValue = null;
		if (getSelected() != null) {
			selectedValue = getSelected().toString();
		} else if (getDefault() != null) {
			selectedValue = getDefault().toString();
		}
		boolean selected = false;
		if (StringUtils.isNotBlank(selectedValue)) {
			String val = getValue() != null ? getValue().toString() : body;
			selected = StringUtils.equals(selectedValue, val);
		}
		return selected;
	}

	private Object value;
	private Object selected;
	private Object def;

	/**
	 * The name of the '<code>disabled</code>' attribute.
	 */
	private static final String DISABLED_ATTRIBUTE = "disabled";

	private String disabled;

	protected Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	protected Object getSelected() {
		return selected;
	}

	public void setSelected(Object selected) {
		this.selected = selected;
	}

	protected Object getDefault() {
		return def;
	}

	public void setDefault(Object def) {
		this.def = def;
	}

	/**
	 * Set the value of the '<code>disabled</code>' attribute.
	 * <p>
	 * May be a runtime expression.
	 * 
	 * @param disabled
	 *            the value of the '<code>disabled</code>' attribute
	 */
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	/**
	 * Get the value of the '<code>disabled</code>' attribute.
	 */
	protected String getDisabled() {
		return this.disabled;
	}

}

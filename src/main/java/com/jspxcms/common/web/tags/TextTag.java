package com.jspxcms.common.web.tags;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

/**
 * TextTag
 * 
 * @author liufang
 * 
 */
public class TextTag extends AbstractHtmlInputElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter writer) throws JspException {
		writer.startTag("input");
		writer.writeAttribute("type", "text");
		writer.writeOptionalAttributeValue("id", getId());
		writer.writeOptionalAttributeValue("name", getName());
		writer.writeOptionalAttributeValue("value", getVal());

		writer.writeOptionalAttributeValue(SIZE_ATTRIBUTE, getSize());
		writer.writeOptionalAttributeValue(MAXLENGTH_ATTRIBUTE, getMaxlength());
		writer.writeOptionalAttributeValue(ALT_ATTRIBUTE, getAlt());
		writer.writeOptionalAttributeValue(ONSELECT_ATTRIBUTE, getOnselect());
		writer.writeOptionalAttributeValue(AUTOCOMPLETE_ATTRIBUTE,
				getAutocomplete());
		writeOptionalAttributes(writer);
		writer.endTag();
		return SKIP_BODY;
	}

	protected String getVal() {
		String val = null;
		if (getValue() != null) {
			val = getValue().toString();
			if (StringUtils.isEmpty(val)) {
				val = null;
			}
		}
		if (val == null && getDefault() != null) {
			val = getDefault().toString();
		}
		return HtmlUtils.htmlEscape(val);
	}

	private Object value;
	private Object def;

	public static final String SIZE_ATTRIBUTE = "size";
	public static final String MAXLENGTH_ATTRIBUTE = "maxlength";
	public static final String ALT_ATTRIBUTE = "alt";
	public static final String ONSELECT_ATTRIBUTE = "onselect";
	public static final String READONLY_ATTRIBUTE = "readonly";
	public static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";

	private String name;

	private String size;
	private String maxlength;
	private String alt;
	private String onselect;
	private String autocomplete;

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

	/**
	 * Set the value of the '<code>size</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Get the value of the '<code>size</code>' attribute.
	 */
	protected String getSize() {
		return this.size;
	}

	/**
	 * Set the value of the '<code>maxlength</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * Get the value of the '<code>maxlength</code>' attribute.
	 */
	protected String getMaxlength() {
		return this.maxlength;
	}

	/**
	 * Set the value of the '<code>alt</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * Get the value of the '<code>alt</code>' attribute.
	 */
	protected String getAlt() {
		return this.alt;
	}

	/**
	 * Set the value of the '<code>onselect</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setOnselect(String onselect) {
		this.onselect = onselect;
	}

	/**
	 * Get the value of the '<code>onselect</code>' attribute.
	 */
	protected String getOnselect() {
		return this.onselect;
	}

	/**
	 * Set the value of the '<code>autocomplete</code>' attribute. May be a
	 * runtime expression.
	 */
	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}

	/**
	 * Get the value of the '<code>autocomplete</code>' attribute.
	 */
	protected String getAutocomplete() {
		return this.autocomplete;
	}

}

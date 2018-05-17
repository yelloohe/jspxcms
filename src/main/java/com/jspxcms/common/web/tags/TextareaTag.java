package com.jspxcms.common.web.tags;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

/**
 * TagIdGenerator
 * 
 * @author liufang
 * 
 */
public class TextareaTag extends AbstractHtmlInputElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter writer) throws JspException {
		writer.startTag("textarea");
		writer.writeOptionalAttributeValue("id", getId());
		writer.writeOptionalAttributeValue("name", getName());
		writer.writeOptionalAttributeValue(ROWS_ATTRIBUTE, getRows());
		writer.writeOptionalAttributeValue(COLS_ATTRIBUTE, getCols());
		writer.writeOptionalAttributeValue(ONSELECT_ATTRIBUTE, getOnselect());
		writeOptionalAttributes(writer);
		String val = getVal();
		val = StringUtils.isNotBlank(val) ? val : "";
		writer.appendValue(val);
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

	public static final String ROWS_ATTRIBUTE = "rows";
	public static final String COLS_ATTRIBUTE = "cols";
	public static final String ONSELECT_ATTRIBUTE = "onselect";

	private String rows;
	private String cols;
	private String onselect;

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
	 * Set the value of the '<code>rows</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setRows(String rows) {
		this.rows = rows;
	}

	/**
	 * Get the value of the '<code>rows</code>' attribute.
	 */
	protected String getRows() {
		return this.rows;
	}

	/**
	 * Set the value of the '<code>cols</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setCols(String cols) {
		this.cols = cols;
	}

	/**
	 * Get the value of the '<code>cols</code>' attribute.
	 */
	protected String getCols() {
		return this.cols;
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
}

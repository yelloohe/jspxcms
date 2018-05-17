package com.jspxcms.common.web.tags;

import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * AbstractHtmlInputElementTag
 * 
 * @author liufang
 * 
 */
public abstract class AbstractHtmlInputElementTag extends
		AbstractHtmlElementTag {
	private static final long serialVersionUID = 1L;

	/**
	 * Adds input-specific optional attributes as defined by this base class.
	 */
	@Override
	protected void writeOptionalAttributes(TagWriter tagWriter)
			throws JspException {
		super.writeOptionalAttributes(tagWriter);
		tagWriter.writeOptionalAttributeValue(ONFOCUS_ATTRIBUTE, getOnfocus());
		tagWriter.writeOptionalAttributeValue(ONBLUR_ATTRIBUTE, getOnblur());
		tagWriter
				.writeOptionalAttributeValue(ONCHANGE_ATTRIBUTE, getOnchange());
		tagWriter.writeOptionalAttributeValue(ACCESSKEY_ATTRIBUTE,
				getAccesskey());
		tagWriter
				.writeOptionalAttributeValue(DISABLED_ATTRIBUTE, getDisabled());
		tagWriter
				.writeOptionalAttributeValue(READONLY_ATTRIBUTE, getReadonly());
	}

	/**
	 * The name of the '<code>onfocus</code>' attribute.
	 */
	public static final String ONFOCUS_ATTRIBUTE = "onfocus";

	/**
	 * The name of the '<code>onblur</code>' attribute.
	 */
	public static final String ONBLUR_ATTRIBUTE = "onblur";

	/**
	 * The name of the '<code>onchange</code>' attribute.
	 */
	public static final String ONCHANGE_ATTRIBUTE = "onchange";

	/**
	 * The name of the '<code>accesskey</code>' attribute.
	 */
	public static final String ACCESSKEY_ATTRIBUTE = "accesskey";

	/**
	 * The name of the '<code>disabled</code>' attribute.
	 */
	public static final String DISABLED_ATTRIBUTE = "disabled";

	/**
	 * The name of the '<code>readonly</code>' attribute.
	 */
	public static final String READONLY_ATTRIBUTE = "readonly";

	private String onfocus;

	private String onblur;

	private String onchange;

	private String accesskey;

	private String disabled;

	private String readonly;

	/**
	 * Set the value of the '<code>onfocus</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	/**
	 * Get the value of the '<code>onfocus</code>' attribute.
	 */
	protected String getOnfocus() {
		return this.onfocus;
	}

	/**
	 * Set the value of the '<code>onblur</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	/**
	 * Get the value of the '<code>onblur</code>' attribute.
	 */
	protected String getOnblur() {
		return this.onblur;
	}

	/**
	 * Set the value of the '<code>onchange</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	/**
	 * Get the value of the '<code>onchange</code>' attribute.
	 */
	protected String getOnchange() {
		return this.onchange;
	}

	/**
	 * Set the value of the '<code>accesskey</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	/**
	 * Get the value of the '<code>accesskey</code>' attribute.
	 */
	protected String getAccesskey() {
		return this.accesskey;
	}

	/**
	 * Set the value of the '<code>disabled</code>' attribute. May be a runtime
	 * expression.
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

	/**
	 * Sets the value of the '<code>readonly</code>' attribute. May be a runtime
	 * expression.
	 * 
	 * @see #isReadonly()
	 */
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	/**
	 * Gets the value of the '<code>readonly</code>' attribute. May be a runtime
	 * expression.
	 * 
	 * @see #isReadonly()
	 */
	protected String getReadonly() {
		return this.readonly;
	}

}

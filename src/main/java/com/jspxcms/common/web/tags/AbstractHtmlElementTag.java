package com.jspxcms.common.web.tags;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

/**
 * AbstractHtmlElementTag
 * 
 * @author liufang
 * 
 */
public abstract class AbstractHtmlElementTag extends RequestContextAwareTag
		implements DynamicAttributes {
	private static final long serialVersionUID = 1L;

	protected TagWriter createTagWriter() {
		return new TagWriter(this.pageContext);
	}

	@Override
	protected final int doStartTagInternal() throws Exception {
		return writeTagContent(createTagWriter());
	}

	protected abstract int writeTagContent(TagWriter writer)
			throws JspException;

	/**
	 * Writes the default attributes configured via this base class to the
	 * supplied {@link TagWriter}. Subclasses should call this when they want
	 * the base attribute set to be written to the output.
	 */
	protected void writeOptionalAttributes(TagWriter w) throws JspException {
		w.writeOptionalAttributeValue(STYLE_ATTRIBUTE, getStyle());
		w.writeOptionalAttributeValue(LANG_ATTRIBUTE, getLang());
		w.writeOptionalAttributeValue(TITLE_ATTRIBUTE, getTitle());
		w.writeOptionalAttributeValue(DIR_ATTRIBUTE, getDir());
		w.writeOptionalAttributeValue(TABINDEX_ATTRIBUTE, getTabindex());
		w.writeOptionalAttributeValue(ONCLICK_ATTRIBUTE, getOnclick());
		w.writeOptionalAttributeValue(ONDBLCLICK_ATTRIBUTE, getOndblclick());
		w.writeOptionalAttributeValue(ONMOUSEDOWN_ATTRIBUTE, getOnmousedown());
		w.writeOptionalAttributeValue(ONMOUSEUP_ATTRIBUTE, getOnmouseup());
		w.writeOptionalAttributeValue(ONMOUSEOVER_ATTRIBUTE, getOnmouseover());
		w.writeOptionalAttributeValue(ONMOUSEMOVE_ATTRIBUTE, getOnmousemove());
		w.writeOptionalAttributeValue(ONMOUSEOUT_ATTRIBUTE, getOnmouseout());
		w.writeOptionalAttributeValue(ONKEYPRESS_ATTRIBUTE, getOnkeypress());
		w.writeOptionalAttributeValue(ONKEYUP_ATTRIBUTE, getOnkeyup());
		w.writeOptionalAttributeValue(ONKEYDOWN_ATTRIBUTE, getOnkeydown());
		if (!CollectionUtils.isEmpty(this.dynamicAttributes)) {
			for (String attr : this.dynamicAttributes.keySet()) {
				String displayValue = ObjectUtils
						.getDisplayString(this.dynamicAttributes.get(attr));
				w.writeOptionalAttributeValue(attr,
						HtmlUtils.htmlEscape(displayValue));
			}
		}
	}

	public static final String STYLE_ATTRIBUTE = "style";
	public static final String LANG_ATTRIBUTE = "lang";
	public static final String TITLE_ATTRIBUTE = "title";
	public static final String DIR_ATTRIBUTE = "dir";
	public static final String TABINDEX_ATTRIBUTE = "tabindex";
	public static final String ONCLICK_ATTRIBUTE = "onclick";
	public static final String ONDBLCLICK_ATTRIBUTE = "ondblclick";
	public static final String ONMOUSEDOWN_ATTRIBUTE = "onmousedown";
	public static final String ONMOUSEUP_ATTRIBUTE = "onmouseup";
	public static final String ONMOUSEOVER_ATTRIBUTE = "onmouseover";
	public static final String ONMOUSEMOVE_ATTRIBUTE = "onmousemove";
	public static final String ONMOUSEOUT_ATTRIBUTE = "onmouseout";
	public static final String ONKEYPRESS_ATTRIBUTE = "onkeypress";
	public static final String ONKEYUP_ATTRIBUTE = "onkeyup";
	public static final String ONKEYDOWN_ATTRIBUTE = "onkeydown";

	private String style;
	private String lang;
	private String title;
	private String dir;
	private String tabindex;
	private String onclick;
	private String ondblclick;
	private String onmousedown;
	private String onmouseup;
	private String onmouseover;
	private String onmousemove;
	private String onmouseout;
	private String onkeypress;
	private String onkeyup;
	private String onkeydown;
	private Map<String, Object> dynamicAttributes;

	/**
	 * Set the value of the '<code>style</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Get the value of the '<code>style</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getStyle() {
		return this.style;
	}

	/**
	 * Set the value of the '<code>lang</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * Get the value of the '<code>lang</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getLang() {
		return this.lang;
	}

	/**
	 * Set the value of the '<code>title</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the value of the '<code>title</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getTitle() {
		return this.title;
	}

	/**
	 * Set the value of the '<code>dir</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	 * Get the value of the '<code>dir</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getDir() {
		return this.dir;
	}

	/**
	 * Set the value of the '<code>tabindex</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}

	/**
	 * Get the value of the '<code>tabindex</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getTabindex() {
		return this.tabindex;
	}

	/**
	 * Set the value of the '<code>onclick</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	/**
	 * Get the value of the '<code>onclick</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getOnclick() {
		return this.onclick;
	}

	/**
	 * Set the value of the '<code>ondblclick</code>' attribute. May be a
	 * runtime expression.
	 */
	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	/**
	 * Get the value of the '<code>ondblclick</code>' attribute. May be a
	 * runtime expression.
	 */
	protected String getOndblclick() {
		return this.ondblclick;
	}

	/**
	 * Set the value of the '<code>onmousedown</code>' attribute. May be a
	 * runtime expression.
	 */
	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	/**
	 * Get the value of the '<code>onmousedown</code>' attribute. May be a
	 * runtime expression.
	 */
	protected String getOnmousedown() {
		return this.onmousedown;
	}

	/**
	 * Set the value of the '<code>onmouseup</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}

	/**
	 * Get the value of the '<code>onmouseup</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getOnmouseup() {
		return this.onmouseup;
	}

	/**
	 * Set the value of the '<code>onmouseover</code>' attribute. May be a
	 * runtime expression.
	 */
	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	/**
	 * Get the value of the '<code>onmouseover</code>' attribute. May be a
	 * runtime expression.
	 */
	protected String getOnmouseover() {
		return this.onmouseover;
	}

	/**
	 * Set the value of the '<code>onmousemove</code>' attribute. May be a
	 * runtime expression.
	 */
	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}

	/**
	 * Get the value of the '<code>onmousemove</code>' attribute. May be a
	 * runtime expression.
	 */
	protected String getOnmousemove() {
		return this.onmousemove;
	}

	/**
	 * Set the value of the '<code>onmouseout</code>' attribute. May be a
	 * runtime expression.
	 */
	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	/**
	 * Get the value of the '<code>onmouseout</code>' attribute. May be a
	 * runtime expression.
	 */
	protected String getOnmouseout() {
		return this.onmouseout;
	}

	/**
	 * Set the value of the '<code>onkeypress</code>' attribute. May be a
	 * runtime expression.
	 */
	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}

	/**
	 * Get the value of the '<code>onkeypress</code>' attribute. May be a
	 * runtime expression.
	 */
	protected String getOnkeypress() {
		return this.onkeypress;
	}

	/**
	 * Set the value of the '<code>onkeyup</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	/**
	 * Get the value of the '<code>onkeyup</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getOnkeyup() {
		return this.onkeyup;
	}

	/**
	 * Set the value of the '<code>onkeydown</code>' attribute. May be a runtime
	 * expression.
	 */
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	/**
	 * Get the value of the '<code>onkeydown</code>' attribute. May be a runtime
	 * expression.
	 */
	protected String getOnkeydown() {
		return this.onkeydown;
	}

	/**
	 * Get the map of dynamic attributes.
	 */
	protected Map<String, Object> getDynamicAttributes() {
		return this.dynamicAttributes;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDynamicAttribute(String uri, String localName, Object value)
			throws JspException {
		if (this.dynamicAttributes == null) {
			this.dynamicAttributes = new HashMap<String, Object>();
		}
		if (!isValidDynamicAttribute(localName, value)) {
			throw new IllegalArgumentException("Attribute " + localName + "=\""
					+ value + "\" is not allowed");
		}
		dynamicAttributes.put(localName, value);
	}

	/**
	 * Whether the given name-value pair is a valid dynamic attribute.
	 */
	protected boolean isValidDynamicAttribute(String localName, Object value) {
		return true;
	}
}

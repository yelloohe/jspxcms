package com.jspxcms.common.web.tags;

import javax.servlet.jsp.JspException;

import org.springframework.util.Assert;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * OptionsTag
 * 
 * @author liufang
 * 
 */
public class OptionsTag extends AbstractHtmlElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter writer) throws JspException {
		if (items != null) {
			OptionsWriter optionWriter = new OptionsWriter(getSelectedValue(),
					items, itemValue, itemLabel);
			optionWriter.writeOptions(writer);
		}
		return SKIP_BODY;
	}

	protected Object getSelectedValue() {
		return selected != null ? selected : def;
	}

	private Object items;
	private String itemValue;
	private String itemLabel;
	private boolean disabled;
	private Object selected;
	private Object def;

	public void setItems(Object items) {
		this.items = items;
	}

	protected Object getItems() {
		return this.items;
	}

	public void setItemValue(String itemValue) {
		Assert.hasText(itemValue, "'itemValue' must not be empty");
		this.itemValue = itemValue;
	}

	protected String getItemValue() {
		return this.itemValue;
	}

	public void setItemLabel(String itemLabel) {
		Assert.hasText(itemLabel, "'itemLabel' must not be empty");
		this.itemLabel = itemLabel;
	}

	protected String getItemLabel() {
		return this.itemLabel;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	protected Boolean getDisabled() {
		return this.disabled;
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

	private class OptionsWriter extends OptionWriter {
		public OptionsWriter(Object selectedValue, Object optionSource,
				String valueProperty, String labelProperty) {
			super(selectedValue, optionSource, valueProperty, labelProperty);
		}

		@Override
		protected boolean isOptionDisabled() throws JspException {
			return getDisabled();
		}

		@Override
		protected void writeCommonAttributes(TagWriter tagWriter)
				throws JspException {
			writeOptionalAttributes(tagWriter);
		}
	}
}

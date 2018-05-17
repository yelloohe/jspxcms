package com.jspxcms.common.web.tags;

import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

/**
 * OptionWriter
 * 
 * @author liufang
 * 
 */
class OptionWriter {
	private final Object seletedValue;
	private final Object optionSource;
	private final String valueProperty;
	private final String labelProperty;

	public OptionWriter(Object selectedValue, Object optionSource,
			String valueProperty, String labelProperty) {
		Assert.notNull(optionSource, "'optionSource' must not be null");
		this.seletedValue = selectedValue;
		this.optionSource = optionSource;
		this.valueProperty = valueProperty;
		this.labelProperty = labelProperty;
	}

	@SuppressWarnings("rawtypes")
	public void writeOptions(TagWriter tagWriter) throws JspException {
		if (this.optionSource.getClass().isArray()) {
			renderFromArray(tagWriter);
		} else if (this.optionSource instanceof Collection) {
			renderFromCollection(tagWriter);
		} else if (this.optionSource instanceof Map) {
			renderFromMap(tagWriter);
		} else if (this.optionSource instanceof Class
				&& ((Class) this.optionSource).isEnum()) {
			renderFromEnum(tagWriter);
		} else {
			throw new JspException("Type ["
					+ this.optionSource.getClass().getName()
					+ "] is not valid for option items");
		}
	}

	private void renderFromArray(TagWriter tagWriter) throws JspException {
		doRenderFromCollection(CollectionUtils.arrayToList(this.optionSource),
				tagWriter);
	}

	@SuppressWarnings("rawtypes")
	private void renderFromMap(TagWriter tagWriter) throws JspException {
		Map<?, ?> optionMap = (Map) this.optionSource;
		for (Map.Entry entry : optionMap.entrySet()) {
			Object mapKey = entry.getKey();
			Object mapValue = entry.getValue();
			Object renderValue = (this.valueProperty != null ? PropertyAccessorFactory
					.forBeanPropertyAccess(mapKey).getPropertyValue(
							this.valueProperty) : mapKey);
			Object renderLabel = (this.labelProperty != null ? PropertyAccessorFactory
					.forBeanPropertyAccess(mapValue).getPropertyValue(
							this.labelProperty) : mapValue);
			renderOption(tagWriter, mapKey, renderValue, renderLabel);
		}
	}

	@SuppressWarnings("rawtypes")
	private void renderFromCollection(TagWriter tagWriter) throws JspException {
		doRenderFromCollection((Collection) this.optionSource, tagWriter);
	}

	@SuppressWarnings("rawtypes")
	private void renderFromEnum(TagWriter tagWriter) throws JspException {
		doRenderFromCollection(
				CollectionUtils.arrayToList(((Class) this.optionSource)
						.getEnumConstants()), tagWriter);
	}

	@SuppressWarnings("rawtypes")
	private void doRenderFromCollection(Collection optionCollection,
			TagWriter tagWriter) throws JspException {
		for (Object item : optionCollection) {
			BeanWrapper wrapper = PropertyAccessorFactory
					.forBeanPropertyAccess(item);
			Object value;
			if (this.valueProperty != null) {
				value = wrapper.getPropertyValue(this.valueProperty);
			} else if (item instanceof Enum) {
				value = ((Enum<?>) item).name();
			} else {
				value = item;
			}
			Object label = (this.labelProperty != null ? wrapper
					.getPropertyValue(this.labelProperty) : item);
			renderOption(tagWriter, item, value, label);
		}
	}

	private void renderOption(TagWriter tagWriter, Object item, Object value,
			Object label) throws JspException {
		tagWriter.startTag("option");
		writeCommonAttributes(tagWriter);

		String valueDisplayString = HtmlUtils.htmlEscape(value.toString());
		String labelDisplayString = HtmlUtils.htmlEscape(label.toString());

		valueDisplayString = processOptionValue(valueDisplayString);

		if (!valueDisplayString.equals(labelDisplayString)) {
			tagWriter.writeAttribute("value", valueDisplayString);
		}

		if (isOptionSelected(item, value)) {
			tagWriter.writeAttribute("selected", "selected");
		}
		if (isOptionDisabled()) {
			tagWriter.writeAttribute("disabled", "disabled");
		}
		tagWriter.appendValue(labelDisplayString);
		tagWriter.endTag();
	}

	protected String processOptionValue(String resolvedValue) {
		return resolvedValue;
	}

	private boolean isOptionSelected(Object item, Object value) {
		if (seletedValue != null) {
			return seletedValue.equals(item)
					|| seletedValue.equals(value)
					|| (value != null && seletedValue.toString().equals(
							value.toString()));
		}
		return false;
	}

	protected boolean isOptionDisabled() throws JspException {
		return false;
	}

	protected void writeCommonAttributes(TagWriter tagWriter)
			throws JspException {
	}
}

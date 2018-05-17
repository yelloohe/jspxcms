package com.jspxcms.common.web.tags;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

/**
 * AbstractMultiCheckedElementTag
 * 
 * @author liufang
 * 
 */
public abstract class AbstractMultiCheckedElementTag extends AbstractHtmlInputElementTag {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	protected int writeTagContent(TagWriter writer) throws JspException {
		if (items.getClass().isArray()) {
			Object[] itemsArray = (Object[]) items;
			for (int i = 0; i < itemsArray.length; i++) {
				Object item = itemsArray[i];
				writeObjectEntry(writer, itemValue, itemLabel, item, i);
			}
		} else if (items instanceof Collection) {
			final Collection optionCollection = (Collection) items;
			int itemIndex = 0;
			for (Iterator it = optionCollection.iterator(); it.hasNext(); itemIndex++) {
				Object item = it.next();
				writeObjectEntry(writer, itemValue, itemLabel, item, itemIndex);
			}
		} else if (items instanceof Map) {
			final Map optionMap = (Map) items;
			int itemIndex = 0;
			for (Iterator it = optionMap.entrySet().iterator(); it.hasNext(); itemIndex++) {
				Map.Entry entry = (Map.Entry) it.next();
				writeMapEntry(writer, itemValue, itemLabel, entry, itemIndex);
			}
		} else {
			throw new IllegalArgumentException("Attribute 'items' must be an array, a Collection or a Map");
		}
		return SKIP_BODY;
	}

	private void writeObjectEntry(TagWriter tagWriter, String valueProperty, String labelProperty, Object item,
			int itemIndex) throws JspException {

		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
		Object renderValue;
		if (valueProperty != null) {
			renderValue = wrapper.getPropertyValue(valueProperty);
		} else if (item instanceof Enum) {
			renderValue = ((Enum<?>) item).name();
		} else {
			renderValue = item;
		}
		Object renderLabel = (labelProperty != null ? wrapper.getPropertyValue(labelProperty) : item);
		writeElementTag(tagWriter, item, renderValue, renderLabel, itemIndex);
	}

	@SuppressWarnings("rawtypes")
	private void writeMapEntry(TagWriter tagWriter, String valueProperty, String labelProperty, Map.Entry entry,
			int itemIndex) throws JspException {

		Object mapKey = entry.getKey();
		Object mapValue = entry.getValue();
		BeanWrapper mapKeyWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapKey);
		BeanWrapper mapValueWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapValue);
		Object renderValue = (valueProperty != null ? mapKeyWrapper.getPropertyValue(valueProperty) : mapKey.toString());
		Object renderLabel = (labelProperty != null ? mapValueWrapper.getPropertyValue(labelProperty) : mapValue
				.toString());
		writeElementTag(tagWriter, mapKey, renderValue, renderLabel, itemIndex);
	}

	private void writeElementTag(TagWriter writer, Object item, Object value, Object label, int itemIndex)
			throws JspException {

		if (itemIndex > 0) {
			if (StringUtils.isNotEmpty(getDelimiter())) {
				try {
					this.pageContext.getOut().write(getDelimiter());
				} catch (IOException e) {
					throw new JspException("Unable to write to JspWriter", e);
				}
			}
		}
		writer.startTag("label");
		if (StringUtils.isNotBlank(labelClass)) {
			writer.writeAttribute("class", labelClass);
		}
		writer.startTag("input");
		writer.writeAttribute("type", getInputType());
		writer.writeOptionalAttributeValue("name", getName());
		if (isChecked(item, value)) {
			writer.writeAttribute("checked", "checked");
		}
		if (value != null) {
			writer.writeOptionalAttributeValue("value", HtmlUtils.htmlEscape(value.toString()));
		}

		writeOptionalAttributes(writer);
		writer.endTag();
		if (label != null) {
			writer.appendValue(HtmlUtils.htmlEscape(label.toString()));
		}
		writer.endTag();
	}

	protected boolean isChecked(Object item, Object value) {
		for (Object checked : getCheckedArray()) {
			if (ObjectUtils.equals(item, checked)) {
				return true;
			}
			if (ObjectUtils.equals(value, checked)) {
				return true;
			}
			if (item.getClass().equals(checked.getClass())) {
				BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(checked);
				if (value.equals(wrapper.getPropertyValue(itemValue))) {
					return true;
				}
			}
		}
		return false;
	}

	protected Object[] getCheckedArray() {
		Object[] checkedArray;
		Object checkedValue = getChecked();
		if (checkedValue == null) {
			checkedValue = getDefault();
		}
		if (checkedValue == null) {
			checkedArray = new Object[0];
		} else {
			if (checkedValue.getClass().isArray()) {
				// 防止int[]等数组导致异常
				checkedArray = (Object[]) checkedValue;
			} else if (checkedValue instanceof Collection) {
				checkedArray = ((Collection<?>) checkedValue).toArray();
			} else {
				checkedArray = new Object[] { checkedValue };
			}
		}
		return checkedArray;
	}

	/**
	 * Return the type of the HTML input element to generate: "checkbox" or "radio".
	 */
	protected abstract String getInputType();

	private String name;

	// required
	private Object items;
	private String itemValue;
	private String itemLabel;
	private Object checked;
	private Object def;
	private String labelClass;
	private String delimiter = "";

	// private Object[] checkedArray;

	protected String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setItems(Object items) {
		Assert.notNull(items, "'items' must not be null");
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

	protected Object getChecked() {
		return checked;
	}

	public void setChecked(Object checked) {
		this.checked = checked;
	}

	public Object getDefault() {
		return def;
	}

	public void setDefault(Object def) {
		this.def = def;
	}

	public String getLabelClass() {
		return labelClass;
	}

	public void setLabelClass(String labelClass) {
		this.labelClass = labelClass;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getDelimiter() {
		return this.delimiter;
	}
}

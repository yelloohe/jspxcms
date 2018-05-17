package com.jspxcms.common.web.tags;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * CheckboxTag
 * 
 * @author liufang
 * 
 */
public class CheckboxTag extends AbstractHtmlInputElementTag {
	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter writer) throws JspException {
		writer.startTag("input");
		writer.writeAttribute("type", "checkbox");
		writer.writeOptionalAttributeValue("id", getId());
		if (isChecked()) {
			writer.writeAttribute("checked", "checked");
		}
		String hiddenId = getId();
		if (StringUtils.isNotBlank(hiddenId)) {
			hiddenId = "_chk_" + hiddenId;
		} else {
			hiddenId = TagIdGenerator.nextId("_chk_" + getName(), pageContext);
		}
		String onclick = "$('#" + hiddenId + "').val(this.checked);";
		if (StringUtils.isNotBlank(getOnclick())) {
			onclick += getOnclick();
		}
		writer.writeAttribute("onclick", onclick);
		setOnclick(null);
		writeOptionalAttributes(writer);
		writer.endTag();

		writer.startTag("input");
		writer.writeAttribute("type", "hidden");
		writer.writeAttribute("id", hiddenId);
		writer.writeAttribute("name", getName());
		writer.writeAttribute("value", String.valueOf(isChecked()));
		writer.endTag();
		return SKIP_BODY;
	}

	protected boolean isChecked() {
		boolean checked = false;
		if (value != null) {
			checked = Boolean.parseBoolean(value.toString());
		} else if (def != null) {
			checked = Boolean.parseBoolean(def.toString());
		}
		return checked;
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

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getDefault() {
		return def;
	}

	public void setDefault(Object def) {
		this.def = def;
	}

}

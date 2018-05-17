package com.jspxcms.common.web;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * 通用的日期编辑器。使用 {@link DateTime#parse(String)} 格式化，支持多种符合ISO标准日期格式。如'yyyy-MM-dd', 'yyyy-MM-ddTHH:mm:ss'等。
 * <p>
 * 一般日期转换必须指定具体某一种格式，比如'yyyy-MM-dd'或'yyyy-MM-dd HH:mm:ss'，如果指定为前一种，则无法处理后一种格式。
 * 
 * @author liufang
 * 
 * @see DateTime
 */
public class DateEditor extends PropertyEditorSupport {
	public static Date parse(String text) {
		if (StringUtils.isBlank(text)) {
			// Treat empty String as null value.
			return null;
		}
		DateTime dt = DateTime.parse(text);
		// 根据日期长度，转换成不同的日期类型
		if (text.length() > 10) {
			return new java.sql.Timestamp(dt.getMillis());
		} else {
			return new java.sql.Date(dt.getMillis());
		}
	}

	/**
	 * 将日期转换成字符串
	 */
	@Override
	public String getAsText() {
		Date date = (Date) getValue();
		DateTime dt = new DateTime(date.getTime());
		String text = "";
		if (date != null) {
			if (date instanceof java.sql.Timestamp) {
				text = ISODateTimeFormat.dateHourMinuteSecond().print(dt);
			} else {
				text = ISODateTimeFormat.date().print(dt);
			}
		}
		return text;
	}

	/**
	 * 将字符串转换成日期
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(parse(text));
	}
}

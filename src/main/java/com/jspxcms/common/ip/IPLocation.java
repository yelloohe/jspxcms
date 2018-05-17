package com.jspxcms.common.ip;

import org.apache.commons.lang3.StringUtils;

public class IPLocation {
	/**
	 * 未知地址
	 */
	public static final String UNKNOWN = "UNKNOWN";
	/**
	 * 本地网络
	 */
	public static final String LAN = "LAN";

	private String country;
	private String area;

	public IPLocation() {
		country = area = "";
	}

	public IPLocation(String country, String area) {
		setCountry(country);
		setArea(area);
	}

	public IPLocation getCopy() {
		IPLocation copy = new IPLocation();
		copy.setCountry(country);
		copy.setArea(area);
		return copy;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = check(country);
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = check(area);
	}

	/**
	 * 如果为局域网，纯真IP地址库的地区会显示CZ88.NET,这里把它去掉
	 * 
	 * @param s
	 * @return
	 */
	private String check(String s) {
		if ("CZ88.NET".equals(StringUtils.trim(s))) {
			return UNKNOWN;
		} else if ("IANA".equals(StringUtils.trim(s))) {
			return LAN;
		} else if ("保留地址用于本地回送".equals(StringUtils.trim(s))) {
			return LAN;
		} else {
			return s;
		}
	}
}

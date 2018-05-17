package com.jspxcms.core.constant;

import com.jspxcms.core.commercial.Enterprise;

public class Versions {
	/**
	 * 当前版本
	 */
	public static final String VERSION = "8.5.0";

	/**
	 * 当前版本
	 * 
	 * @return
	 */
	public static String getVersion() {
		return Enterprise.isEp() ? VERSION + "-enterprise" : VERSION;
	}

}

package com.jspxcms.core.commercial;

/**
 * 企业版标识
 * 
 * @author liufang
 *
 */
public class Enterprise {
	/**
	 * 是否企业版
	 */
	public static final boolean EP = true;

	/**
	 * 是否企业版。静态变量直接引用，会在编译期绑定变量值，导致版本显示错误。使用静态方法可以避免这个问题。
	 * 
	 * @return
	 */
	public static boolean isEp() {
		return EP;
	}
}

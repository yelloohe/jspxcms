package com.jspxcms.common.web;

/**
 * 路径获取接口
 * 
 * @author liufang
 * 
 */
public interface PathResolver {
	public String getPath(String uri);

	public String getPath(String uri, String prefix);
}

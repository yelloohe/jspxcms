package com.jspxcms.core.service;

import java.util.Map;

import com.jspxcms.core.domain.Info;

/**
 * InfoAttributeService
 * 
 * @author liufang
 * 
 */
public interface InfoAttributeService {
	public void update(Info info, Integer[] attrIds, Map<String, String> attrImages);

	public int deleteByAttributeId(Integer attributeId);
}

package com.jspxcms.core.service;

import java.util.Map;

import com.jspxcms.core.domain.Global;
import com.jspxcms.core.support.Configurable;

/**
 * GlobalService
 * 
 * @author liufang
 * 
 */
public interface GlobalService {
	public Global findUnique();

	public Global update(Global bean, Integer uploadsPublishPointId);

	public void updateConf(Configurable conf);

	public void updateCustoms(Global global, Map<String, String> map, Map<String, String> clobMap);
}

package com.jspxcms.core.service;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoDetail;

/**
 * InfoDetailService
 * 
 * @author liufang
 * 
 */
public interface InfoDetailService {
	public InfoDetail get(Integer id);

	public InfoDetail save(InfoDetail detail, Info info);

	public InfoDetail update(InfoDetail bean, Info info);
}

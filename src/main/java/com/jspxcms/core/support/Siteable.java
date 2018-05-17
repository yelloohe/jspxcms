package com.jspxcms.core.support;

import com.jspxcms.core.domain.Site;

/**
 * 具有站点属性的实体。除了User、Org、MemberGroup大部分实体都具有站点属性。在验证数据是否属于当前站点、评论等操作上会使用到。
 * 
 * @author liufang
 * 
 */
public interface Siteable {
	/**
	 * 获取实体所属的站点
	 * 
	 * @return
	 */
	public Site getSite();

	/**
	 * 获取实体ID
	 * 
	 * @return
	 */
	public Number getId();
}

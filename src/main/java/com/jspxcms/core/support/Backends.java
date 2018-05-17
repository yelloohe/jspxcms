package com.jspxcms.core.support;

/**
 * 后台工具类
 * 
 * @author liufang
 *
 */
public class Backends {
	/**
	 * 验证数据是否属于当前站点。如果不属于当前站点数据，则抛出{@see CmsException}
	 * 
	 * @param bean
	 *            待验证对象
	 * @param siteId
	 *            当前站点ID
	 */
	public static void validateDataInSite(Siteable bean, Integer siteId) {
		if (bean != null && bean.getSite() != null
				&& !siteId.equals(bean.getSite().getId())) {
			throw new CmsException("error.dataNotInSite", bean.getClass()
					.getName(), String.valueOf(bean.getId()),
					String.valueOf(bean.getSite().getId()),
					String.valueOf(siteId));
		}
	}
}

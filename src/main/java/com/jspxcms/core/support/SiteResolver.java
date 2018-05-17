package com.jspxcms.core.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.core.domain.Site;
import com.jspxcms.core.service.SiteService;

public class SiteResolver {
	public void resolveSite(String siteNumber) {
		resolveSite(siteNumber, null);
	}

	/**
	 * 解析当前站点。
	 * 
	 * @param siteNumber
	 *            站点代码
	 * @param siteable
	 *            带站点信息的对象
	 * @return 解析所得的Site对象
	 */
	public void resolveSite(String siteNumber, Siteable siteable) {
		Site site = null;
		if (siteNumber != null) {
			site = siteService.findByNumber(siteNumber);
			if (site == null) {
				throw new IllegalStateException("Site number not found: "
						+ siteNumber);
			}
			Context.setCurrentSite(site);
			// 手机域名存在，并且是手机客户端访问
			if(StringUtils.isNotBlank(site.getMobileDomain()) && Context.isMobile()) {
				Context.setMobile(true);
			} else {
				Context.setMobile(false);
			}
		} else {
			site = Context.getCurrentSite();
		}
		if (siteable != null) {
			if (!siteable.getSite().getId().equals(site.getId())) {
				String className = siteable.getClass().getName();
				String dataId = String.valueOf(siteable.getId());
				String dataSiteId = String.valueOf(siteable.getSite().getId());
				String siteId = String.valueOf(site.getId());
				throw new CmsException("error.dataNotInSite", className,
						dataId, dataSiteId, siteId);
			}
		}
	}

	private SiteService siteService;

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}

package com.jspxcms.plug.web.fore;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.jssdk.JSSDKAPI;
import com.foxinmy.weixin4j.jssdk.JSSDKConfigurator;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.foxinmy.weixin4j.type.TicketType;
import com.jspxcms.common.web.Servlets;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.SiteWeixin;
import com.jspxcms.core.support.Context;
import com.jspxcms.core.support.WeixinProxyFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信JS-SDK获取配置
 */
@Controller
public class WeixinJssdk {
    private static final Logger logger = LoggerFactory.getLogger(WeixinJssdk.class);

    @RequestMapping(value = "/jssdk_share")
    public void configShear(String url, String debug, HttpServletRequest request, HttpServletResponse response) {
        Site site = Context.getCurrentSite();
        SiteWeixin sw = site.getWeixin();
        if (StringUtils.isBlank(sw.getAppid()) || StringUtils.isBlank(sw.getSecret())) {
            Servlets.writeHtml(response, "weixin appid or weixin secret did not set!");
            return;
        }
        WeixinProxy weixinProxy = weixinProxyFactory.get(sw.getAppid(), sw.getSecret());

        if (StringUtils.isBlank(url)) {
            Servlets.writeHtml(response, "url connot be blank!");
            return;
        }
        JSSDKConfigurator jssdk = new JSSDKConfigurator(weixinProxy.getTicketManager(TicketType.jsapi));
        jssdk.appId(sw.getAppid());
        jssdk.apis(JSSDKAPI.SHARE_APIS);
        if (StringUtils.isNotBlank(debug) && "true".equals(debug)) {
            jssdk.debugMode();
        }
        String result;
        try {
            result = jssdk.toJSONConfig(url);
        } catch (WeixinException e) {
            result = e.getMessage();
            logger.error(null, e);
        }
        Servlets.writeHtml(response, result);
    }

    @Autowired
    private WeixinProxyFactory weixinProxyFactory;
}

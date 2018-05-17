package com.jspxcms.core.domain;

import com.jspxcms.core.support.Configurable;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众号配置
 */
public class SiteWeixin implements Configurable {

    public SiteWeixin() {
    }

    public SiteWeixin(Map<String, String> customs) {
        this.customs = customs;
    }

    public SiteWeixin(Site site) {
        this.site = site;
        this.customs = site.getCustoms();
    }

    public String getAppid() {
        return getCustoms().get(APPID);
    }

    public void setAppid(String appid) {
        if (StringUtils.isNotBlank(appid)) {
            getCustoms().put(APPID, appid);
        } else {
            getCustoms().remove(APPID);
        }
    }

    public String getSecret() {
        return getCustoms().get(SECRET);
    }

    public void setSecret(String secret) {
        if (StringUtils.isNotBlank(secret)) {
            getCustoms().put(SECRET, secret);
        } else {
            getCustoms().remove(SECRET);
        }
    }

    public Map<String, String> getCustoms() {
        if (customs == null) {
            customs = new HashMap<String, String>();
        }
        return customs;
    }

    public void setCustoms(Map<String, String> customs) {
        this.customs = customs;
    }

    public String getPrefix() {
        return PREFIX;
    }

    /**
     * 配置前缀
     */
    public static final String PREFIX = "sys_weixin_";
    /**
     * 微信公众号APPID
     */
    public static final String APPID = PREFIX + "appid";
    /**
     * 微信公众号SECRET
     */
    public static final String SECRET = PREFIX + "secret";

    private Map<String, String> customs;
    private Site site;
}

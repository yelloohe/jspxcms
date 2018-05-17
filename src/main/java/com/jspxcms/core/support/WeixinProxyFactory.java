package com.jspxcms.core.support;

import com.foxinmy.weixin4j.cache.FileCacheStorager;
import com.foxinmy.weixin4j.http.factory.HttpClientFactory;
import com.foxinmy.weixin4j.http.support.apache4.HttpComponent4Factory;
import com.foxinmy.weixin4j.model.Token;
import com.foxinmy.weixin4j.model.WeixinAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信代理（WeixinProxy）工厂。对微信代理（WeixinProxy）进行缓存，避免重复创建。
 * <p>
 * Created by PONY on 2017/8/7.
 */
public class WeixinProxyFactory {
    private Map<String, WeixinProxy> weixinProxyMap = new HashMap<>();

    /**
     * 获取WeixinProxy。缓存中不存在或者secret有变动，则创建并缓存；否则从缓存中获取，不重复创建。
     *
     * @param weixinAppid  微信公众号appid
     * @param weixinSecret 微信公众号secret
     * @return 微信代理对象
     */
    public WeixinProxy get(String weixinAppid, String weixinSecret) {
        if (StringUtils.isBlank(weixinAppid) || StringUtils.isBlank(weixinSecret)) {
            throw new IllegalArgumentException("weixinAppid or weixinSecret cannot be empty!");
        }
        WeixinProxy weixinProxy = weixinProxyMap.get(weixinAppid);
        if (weixinProxy == null || !weixinProxy.getWeixinAccount().getSecret().equals(weixinSecret)) {
            HttpClientFactory.setDefaultFactory(new HttpComponent4Factory());
            weixinProxy = new WeixinProxy(new WeixinAccount(weixinAppid, weixinSecret),
                    new FileCacheStorager<Token>());
            weixinProxyMap.put(weixinAppid, weixinProxy);
        }
        return weixinProxy;
    }
}

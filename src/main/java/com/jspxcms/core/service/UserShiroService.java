package com.jspxcms.core.service;

import com.jspxcms.core.domain.User;

/**
 * UserRealmService 用户登录权限专用Service
 * 
 * @author liufang
 * 
 */
public interface UserShiroService {
	public User findByUsername(String username);

	public User findByWeixinOpenid(String weixinOpenid);

	public User findByQqOpenid(String qqOpenid);

	public User findByWeiboUid(String weiboUid);

	public User get(Integer id);

	public User getAnonymous();

	public Integer getAnonymousId();

	public User updateLoginSuccess(Integer userId, String loginIp);

	public User updateLoginFailure(String username);
}

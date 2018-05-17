package com.jspxcms.core.security;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspxcms.common.security.CredentialsDigest;
import com.jspxcms.common.security.CredentialsMatcherAdapter;
import com.jspxcms.common.security.UnactivatedAccountException;
import com.jspxcms.core.domain.Site;
import com.jspxcms.core.domain.User;
import com.jspxcms.core.service.UserShiroService;
import com.jspxcms.core.support.Context;

/**
 * ShiroDbRealm
 * 
 * @author liufang
 * 
 */
public class ShiroDbRealm extends AuthorizingRealm implements InitializingBean {
	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userShiroService.findByUsername(token.getUsername());
		// 前后台登录共用，非管理员也可登录。
		if (user != null) {
			if (user.isNormal()) {
				byte[] salt = user.getSaltBytes();
				return new SimpleAuthenticationInfo(new ShiroUser(user.getId(),
						user.getUsername()), user.getPassword(),
						ByteSource.Util.bytes(salt), getName());
			} else if (user.isLocked()) {
				throw new LockedAccountException();
			} else if (user.isUnactivated()) {
				throw new UnactivatedAccountException();
			}
		}
		return null;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = userShiroService.get(shiroUser.id);
		Site site = Context.getCurrentSite();
		if (site != null && user != null && user.isNormal()) {
			Set<String> perms = user.getPerms(site.getId());
			if (!CollectionUtils.isEmpty(perms)) {
				auth.setStringPermissions(perms);
			}
			if (user.isSuper()) {
				auth.addRole("super");
			}
		}
		return auth;
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	public void afterPropertiesSet() throws Exception {
		CredentialsMatcher matcher = new CredentialsMatcherAdapter(
				credentialsDigest);
		setCredentialsMatcher(matcher);
	}

	protected UserShiroService userShiroService;
	private CredentialsDigest credentialsDigest;

	@Autowired
	public void setUserShiroService(UserShiroService userShiroService) {
		this.userShiroService = userShiroService;
	}

	@Autowired
	public void setCredentialsDigest(CredentialsDigest credentialsDigest) {
		this.credentialsDigest = credentialsDigest;
	}

}

package com.jspxcms.core.security.oauth;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.type.FaceSize;
import com.foxinmy.weixin4j.mp.type.Lang;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;

/**
 * OAuthToken
 * 
 * @author liufang
 * 
 */
public class OAuthToken implements AuthenticationToken {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(OAuthToken.class);

	public static final String PROVIDER_WEIXIN = "weixin";
	public static final String PROVIDER_QQ = "qq";
	public static final String PROVIDER_WEIBO = "weibo";

	public OAuthToken(String provider, String ticket, String openid) {
		this.provider = provider;
		this.ticket = ticket;
		this.openid = openid;
	}

	public OAuthToken(String provider, String ticket, String openid, Integer userId, String username) {
		this.provider = provider;
		this.ticket = ticket;
		this.openid = openid;
		this.userId = userId;
		this.username = username;
	}

	private void fetchUserInfo() {
		try {
			if (isQq()) {
				UserInfoBean userBean = new UserInfo(ticket, openid).getUserInfo();
				nickname = userBean.getNickname();
				avatarLarge = userBean.getAvatar().getAvatarURL100();
				avatarSmall = userBean.getAvatar().getAvatarURL50();
			} else if (isWeibo()) {
				Users um = new Users(ticket);
				User user = um.showUserById(openid);
				nickname = user.getScreenName();
				avatarLarge = user.getAvatarLarge();
				avatarSmall = user.getProfileImageUrl();
			} else if (isWeixin()) {
				com.foxinmy.weixin4j.mp.model.User user = new OauthApi().getAuthorizationUser(ticket, openid,
						Lang.zh_CN);
				nickname = user.getNickName();
				avatarLarge = user.getHeadimgurl(FaceSize.big);
				avatarSmall = user.getHeadimgurl(FaceSize.small);
			}
			nicknameRandom = (provider + "_" + RandomStringUtils.randomAlphanumeric(8)).toUpperCase();
		} catch (QQConnectException e) {
			logger.error("get QQ UserInfo exception.", e);
		} catch (WeiboException e) {
			logger.error("get WEIBO User exception.", e);
		} catch (WeixinException e) {
			logger.error("get WEIXIN User exception.", e);
		}
	}

	public String getNickname() {
		if (StringUtils.isBlank(nickname)) {
			fetchUserInfo();
		}
		return nickname;
	}

	public String getNicknameRandom() {
		if (StringUtils.isBlank(nicknameRandom)) {
			fetchUserInfo();
		}
		return nicknameRandom;
	}

	public String getAvatarLarge() {
		if (StringUtils.isBlank(avatarLarge)) {
			fetchUserInfo();
		}
		return avatarLarge;
	}

	public String getAvatarSmall() {
		if (StringUtils.isBlank(avatarSmall)) {
			fetchUserInfo();
		}
		return avatarSmall;
	}

	public boolean isWeixin() {
		return PROVIDER_WEIXIN.equals(this.provider);
	}

	public boolean isQq() {
		return PROVIDER_QQ.equals(this.provider);
	}

	public boolean isWeibo() {
		return PROVIDER_WEIBO.equals(this.provider);
	}

	public Object getPrincipal() {
		return this.openid;
	}

	public Object getCredentials() {
		return this.ticket;
	}

	private String provider;
	private String openid;
	private String ticket;
	private Integer userId;
	private String username;
	private String nickname;
	private String nicknameRandom;
	private String avatarLarge;
	private String avatarSmall;

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setNicknameRandom(String nicknameRandom) {
		this.nicknameRandom = nicknameRandom;
	}

	public void setAvatarLarge(String avatarLarge) {
		this.avatarLarge = avatarLarge;
	}

	public void setAvatarSmall(String avatarSmall) {
		this.avatarSmall = avatarSmall;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "provider=" + this.provider + ";ticket=" + this.ticket + ";openid=" + this.openid + ";userId="
				+ this.userId + ";username=" + this.username;
	}

}

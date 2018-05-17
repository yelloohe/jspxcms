package com.jspxcms.core.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jspxcms.core.support.Configurable;

public class SiteComment implements Configurable {
	public static final String PREFIX = "sys_comment_";
	/**
	 * 评论关闭
	 */
	public static final int STATUS_OFF = 0;
	/**
	 * 评论无权限
	 */
	public static final int STATUS_DENIED = 1;
	/**
	 * 评论需登录
	 */
	public static final int STATUS_LOGIN = 2;
	/**
	 * 评论允许
	 */
	public static final int STATUS_ALLOWED = 3;

	/**
	 * 关闭评论
	 */
	public static final int MODE_OFF = 0;
	/**
	 * 开放会员评论
	 */
	public static final int MODE_USER = 1;
	/**
	 * 开放游客评论
	 */
	public static final int MODE_GUEST = 2;
	/**
	 * 关闭审核
	 */
	public static final int AUDIT_MODE_OFF = 0;
	/**
	 * 游客需审核
	 */
	public static final int AUDIT_MODE_GUEST = 1;
	/**
	 * 游客和会员需审核
	 */
	public static final int AUDIT_MODE_USER = 2;
	/**
	 * 游客、会员、管理员都需审核
	 */
	public static final int AUDIT_MODE_ALL = 3;
	/**
	 * 关闭验证码
	 */
	public static final int CAPTCHA_MODE_OFF = 0;
	/**
	 * 游客需验证码
	 */
	public static final int CAPTCHA_MODE_GUEST = 1;
	/**
	 * 游客和会员需验证码
	 */
	public static final int CAPTCHA_MODE_USER = 2;
	/**
	 * 游客、会员、管理员都需验证码
	 */
	public static final int CAPTCHA_MODE_ALL = 3;

	/**
	 * 评论模式。0：关闭，1：开放会员评论，2：开放游客评论
	 */
	public static final String MODE = PREFIX + "mode";
	/**
	 * 审核模式。0：关闭审核，1：游客需审核，2：游客和会员需审核，3：游客、会员、管理员都需审核
	 */
	public static final String AUDIT_MODE = PREFIX + "auditMode";
	/**
	 * 验证码模式。0：关闭验证码，1：游客需验证码，2：游客和会员需验证码，3：游客、会员、管理员都需验证码
	 */
	public static final String CAPTCHA_MODE = PREFIX + "captchaMode";
	/**
	 * 评论最大长度
	 */
	public static final String MAX_LENGTH = PREFIX + "maxLength";

	private Map<String, String> customs;

	public SiteComment() {
	}

	public SiteComment(Map<String, String> customs) {
		this.customs = customs;
	}

	public SiteComment(Site site) {
		this.customs = site.getCustoms();
	}

	public boolean isNeedCaptcha(User user) {
		int captchaMode = getCaptchaMode();
		return !((captchaMode == CAPTCHA_MODE_OFF)
				|| (captchaMode == CAPTCHA_MODE_GUEST && user != null) || (captchaMode == CAPTCHA_MODE_USER
				&& user != null && user.isAdmin()));
	}

	public boolean isAudit(User user) {
		int auditMode = getAuditMode();
		return (auditMode == AUDIT_MODE_OFF)
				|| (auditMode == AUDIT_MODE_GUEST && user != null)
				|| (auditMode == AUDIT_MODE_USER && user != null && user
						.isAdmin());
	}

	public int getMode() {
		String mode = getCustoms().get(MODE);
		if (StringUtils.isNotBlank(mode)) {
			return Integer.parseInt(mode);
		} else {
			return MODE_GUEST;
		}
	}

	public void setMode(Integer mode) {
		if (mode != null) {
			getCustoms().put(MODE, mode.toString());
		} else {
			getCustoms().remove(MODE);
		}
	}

	public Integer getAuditMode() {
		String auditMode = getCustoms().get(AUDIT_MODE);
		if (StringUtils.isNotBlank(auditMode)) {
			return Integer.parseInt(auditMode);
		} else {
			return AUDIT_MODE_OFF;
		}
	}

	public void setAuditMode(Integer auditMode) {
		if (auditMode != null) {
			getCustoms().put(AUDIT_MODE, auditMode.toString());
		} else {
			getCustoms().remove(AUDIT_MODE);
		}
	}

	public Integer getCaptchaMode() {
		String auditMode = getCustoms().get(CAPTCHA_MODE);
		if (StringUtils.isNotBlank(auditMode)) {
			return Integer.parseInt(auditMode);
		} else {
			return CAPTCHA_MODE_GUEST;
		}
	}

	public void setCaptchaMode(Integer captchaMode) {
		if (captchaMode != null) {
			getCustoms().put(CAPTCHA_MODE, captchaMode.toString());
		} else {
			getCustoms().remove(CAPTCHA_MODE);
		}
	}

	public int getMaxLength() {
		String maxLength = getCustoms().get(MAX_LENGTH);
		if (StringUtils.isNotBlank(maxLength)) {
			return Integer.parseInt(maxLength);
		} else {
			return Integer.MAX_VALUE;
		}
	}

	public void setMaxLength(Integer maxLength) {
		if (maxLength != null) {
			getCustoms().put(MAX_LENGTH, maxLength.toString());
		} else {
			getCustoms().remove(MAX_LENGTH);
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
}

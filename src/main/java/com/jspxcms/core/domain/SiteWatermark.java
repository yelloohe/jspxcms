package com.jspxcms.core.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jspxcms.common.image.WatermarkParam;
import com.jspxcms.core.support.Configurable;

public class SiteWatermark implements Configurable {
	public static final String PREFIX = "sys_watermark_";
	/**
	 * 模式：关闭注册
	 */
	public static final int MODE_OFF = 0;
	/**
	 * 模式：开放注册
	 */
	public static final int MODE_ON = 1;
	/**
	 * 模式
	 */
	public static final String MODE = PREFIX + "mode";
	/**
	 * 图片URL
	 */
	public static final String IMAGE = PREFIX + "image";
	/**
	 * 透明度
	 */
	public static final String ALPHA = PREFIX + "alpha";
	/**
	 * 水印位置。
	 * <ul>
	 * <li>1,2,3
	 * <li>4,5,6
	 * <li>7,8,9
	 * </ul>
	 */
	public static final String POSITION = PREFIX + "position";
	/**
	 * 水平内边距
	 */
	public static final String PADDING_X = PREFIX + "padding_x";
	/**
	 * 垂直内边距
	 */
	public static final String PADDING_Y = PREFIX + "padding_y";
	/**
	 * 最小图片宽度
	 */
	public static final String MIN_WIDTH = PREFIX + "min_width";
	/**
	 * 最小图片高度
	 */
	public static final String MIN_HEIGHT = PREFIX + "min_height";

	private Map<String, String> customs;
	private Site site;

	public SiteWatermark() {
	}

	public SiteWatermark(Map<String, String> customs) {
		this.customs = customs;
	}

	public SiteWatermark(Site site) {
		this.site = site;
		this.customs = site.getCustoms();
	}

	public WatermarkParam getWatermarkParam(Boolean watermark) {
		if (isOff()) {
			watermark = false;
		} else if (watermark == null) {
			watermark = true;
		}
		return new WatermarkParam(watermark, getImagePath(), getAlpha(),
				getPosition(), getPaddingX(), getPaddingY(), getMinWidth(),
				getMinHeight());
	}

	/**
	 * 不含context path
	 * 
	 * @return
	 */
	public String getImagePath() {
		return site.getFilesPath(getImage());
	}

	/**
	 * 含context path
	 * 
	 * @return
	 */
	public String getImageUrl() {
		return site.getFilesUrl(getImage());
	}

	public boolean isOff() {
		return MODE_OFF == getMode() || StringUtils.isBlank(getImage());
	}

	public Integer getMode() {
		String mode = getCustoms().get(MODE);
		if (StringUtils.isNotBlank(mode)) {
			return Integer.parseInt(mode);
		} else {
			return MODE_OFF;
		}
	}

	public void setMode(Integer mode) {
		if (mode != null) {
			getCustoms().put(MODE, mode.toString());
		} else {
			getCustoms().remove(MODE);
		}
	}

	public String getImage() {
		return getCustoms().get(IMAGE);
	}

	public void setImage(String image) {
		if (StringUtils.isNotBlank(image)) {
			getCustoms().put(IMAGE, image);
		} else {
			getCustoms().remove(IMAGE);
		}
	}

	public float getAlphaFloat() {
		float alpha = (float) getAlpha();
		return alpha / (float) 100;
	}

	public Integer getAlpha() {
		String alpha = getCustoms().get(ALPHA);
		if (StringUtils.isNotBlank(alpha)) {
			return Integer.parseInt(alpha);
		} else {
			return 50;
		}
	}

	public void setAlpha(Integer alpha) {
		if (alpha != null) {
			getCustoms().put(ALPHA, alpha.toString());
		} else {
			getCustoms().remove(ALPHA);
		}
	}

	public Integer getPosition() {
		String position = getCustoms().get(POSITION);
		if (StringUtils.isNotBlank(position)) {
			return Integer.parseInt(position);
		} else {
			return 9;
		}
	}

	public void setPosition(Integer position) {
		if (position != null) {
			getCustoms().put(POSITION, position.toString());
		} else {
			getCustoms().remove(POSITION);
		}
	}

	public Integer getPaddingX() {
		String paddingX = getCustoms().get(PADDING_X);
		if (StringUtils.isNotBlank(paddingX)) {
			return Integer.parseInt(paddingX);
		} else {
			return 20;
		}
	}

	public void setPaddingX(Integer paddingX) {
		if (paddingX != null) {
			getCustoms().put(PADDING_X, paddingX.toString());
		} else {
			getCustoms().remove(PADDING_X);
		}
	}

	public Integer getPaddingY() {
		String paddingY = getCustoms().get(PADDING_Y);
		if (StringUtils.isNotBlank(paddingY)) {
			return Integer.parseInt(paddingY);
		} else {
			return 20;
		}
	}

	public void setPaddingY(Integer paddingY) {
		if (paddingY != null) {
			getCustoms().put(PADDING_Y, paddingY.toString());
		} else {
			getCustoms().remove(PADDING_Y);
		}
	}

	public Integer getMinWidth() {
		String minWidth = getCustoms().get(MIN_WIDTH);
		if (StringUtils.isNotBlank(minWidth)) {
			return Integer.parseInt(minWidth);
		} else {
			return 300;
		}
	}

	public void setMinWidth(Integer minWidth) {
		if (minWidth != null) {
			getCustoms().put(MIN_WIDTH, minWidth.toString());
		} else {
			getCustoms().remove(MIN_WIDTH);
		}
	}

	public Integer getMinHeight() {
		String minHeight = getCustoms().get(MIN_HEIGHT);
		if (StringUtils.isNotBlank(minHeight)) {
			return Integer.parseInt(minHeight);
		} else {
			return 300;
		}
	}

	public void setMinHeight(Integer minHeight) {
		if (minHeight != null) {
			getCustoms().put(MIN_HEIGHT, minHeight.toString());
		} else {
			getCustoms().remove(MIN_HEIGHT);
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

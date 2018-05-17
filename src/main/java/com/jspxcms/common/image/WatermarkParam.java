package com.jspxcms.common.image;

import org.apache.commons.lang3.StringUtils;

public class WatermarkParam {
	public WatermarkParam(Boolean watermark) {
		this(watermark, null);
	}

	public WatermarkParam(Boolean watermark, String imagePath) {
		this(watermark, imagePath, null, null, null, null, null, null);
	}

	public WatermarkParam(Boolean watermark, String imagePath, Integer alpha, Integer position, Integer paddingX,
			Integer paddingY, Integer minWidth, Integer minHeight) {
		this.watermark = watermark;
		this.imagePath = imagePath;
		this.alpha = alpha;
		this.position = position;
		this.paddingX = paddingX;
		this.paddingY = paddingY;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
	}

	public float getAlphaFloat() {
		float alpha = (float) getAlpha();
		return alpha / (float) 100;
	}

	private Boolean watermark;
	private String imagePath;
	private Integer alpha;
	private Integer position;
	private Integer paddingX;
	private Integer paddingY;
	private Integer minWidth;
	private Integer minHeight;

	public boolean isWatermark() {
		return watermark != null && watermark && StringUtils.isNotBlank(getImagePath());
	}

	public void setWatermark(Boolean watermark) {
		this.watermark = watermark;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getAlpha() {
		return alpha != null ? alpha : 100;
	}

	public void setAlpha(Integer alpha) {
		this.alpha = alpha;
	}

	/**
	 * 水印位置。默认1（左上角）。1:NorthWest, 2:North, 3:NorthEast, 4:West, 5:Center, 6:East, 7:SouthWest, 8:South, 9:SouthEast.
	 * 
	 * @return
	 */
	public int getPosition() {
		return position == null || position < 1 || position > 9 ? 1 : position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public int getPaddingX() {
		return paddingX != null ? paddingX : 0;
	}

	public void setPaddingX(Integer paddingX) {
		this.paddingX = paddingX;
	}

	public int getPaddingY() {
		return paddingY != null ? paddingY : 0;
	}

	public void setPaddingY(Integer paddingY) {
		this.paddingY = paddingY;
	}

	public int getMinWidth() {
		return minWidth != null ? minWidth : 0;
	}

	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
	}

	public int getMinHeight() {
		return minHeight != null ? minHeight : 0;
	}

	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
	}

}

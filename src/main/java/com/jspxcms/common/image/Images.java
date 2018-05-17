package com.jspxcms.common.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片工具类
 * 
 * @author liufang
 * 
 */
public class Images {
	private static final Logger logger = LoggerFactory.getLogger(Images.class);
	// 目前浏览器支持的图片有：jpg(image/jpeg),gif(image/gif),png(image/png),bmp(image/png),svg(image/svg+xml),webp(image/webp),ico(image/x-icon)
	// 以后有可能会支持谷歌超微型WebP图像格式，目前IE、Edge、火狐都不支持。
	// JDK支持的读取格式 ImageIO.getReaderFormatNames();
	// JDK支持的写入格式 ImageIO.getWriterFormatNames();
	/**
	 * 图片扩展名
	 */
	public static final String[] IMAGE_EXTENSIONS = new String[] { "jpeg", "jpg", "png", "gif", "bmp" };

	/**
	 * 是否是图片扩展名
	 * 
	 * @param extension
	 * @return
	 */
	public static final boolean isImageExtension(String extension) {
		if (StringUtils.isBlank(extension)) {
			return false;
		}
		for (String imageExtension : IMAGE_EXTENSIONS) {
			if (StringUtils.equalsIgnoreCase(imageExtension, extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the underlying input stream contains an image.
	 * 
	 * @param in
	 *            input stream of an image
	 * @return 图片类型。如果为null，代表不是图片。
	 * @throws IOException
	 */
	// URL获取的InputStream不支持reset，远程的url就先存为File吧。
	// png和gif都不是好惹的货，一个是支持透明图片、一个是多桢动画。
	// png按png格式写入，如果以jpg格式写入，将失去透明效果。
	// 纯java不支持多桢的gif，不考虑jdk1.5不支持gif写入问题。
	// 保存为File再判断格式，或者无法判断时，按后缀格式处理。
	// 这个方法的目的是判断是否是可以操作的图片格式，以及真实的图片格式。
	public static String getFormatName(InputStream in) throws IOException {
		ImageInfo ii = new ImageInfo();
		ii.setInput(in);
		if (ii.check()) {
			String formatName = ii.getFormatName().toLowerCase();
			if (ArrayUtils.contains(IMAGE_EXTENSIONS, formatName)) {
				return formatName;
			}
		}
		return null;
	}

	/**
	 * 获取图片格式。
	 * 
	 * @param file
	 *            待识别的图片文件
	 * @return 只返回WEB支持的格式："jpeg", "jpg", "png", "gif", "bmp"。
	 */
	public static String getFormatName(File file) {
		try {
			InputStream input = null;
			try {
				input = FileUtils.openInputStream(file);
				return Images.getFormatName(input);
			} finally {
				IOUtils.closeQuietly(input);
			}
		} catch (IOException e) {
			logger.error(null, e);
			return null;
		}
	}

	public static BufferedImage crop(BufferedImage buff, int x, int y, int width, int height) {
		int origWidth = buff.getWidth();
		int origHeight = buff.getHeight();
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (x + width > origWidth) {
			if (width > origWidth) {
				width = origWidth;
				x = 0;
			} else {
				x = origWidth - width;
			}
		}
		if (y + height > origHeight) {
			if (height > origHeight) {
				height = origHeight;
				y = 0;
			} else {
				y = origHeight - height;
			}
		}
		// 宽高与原图一致，不做处理
		if (width == origWidth && height == origHeight) {
			return buff;
		}
		return Scalr.crop(buff, x, y, width, height);
	}

	public static BufferedImage resize(BufferedImage buff, ScaleParam scaleParam) {
		if (buff == null || !scaleParam.isScale()) {
			return buff;
		}
		Boolean exact = scaleParam.isExact();
		Integer scaleWidth = scaleParam.getWidth();
		Integer scaleHeight = scaleParam.getHeight();
		int width = buff.getWidth();
		int height = buff.getHeight();
		if ((scaleHeight == null && scaleWidth >= width) || (scaleWidth == null && scaleHeight >= height)
				|| (scaleHeight != null && scaleWidth != null && scaleHeight >= height && scaleWidth >= width)) {
			return buff;
		}
		if (scaleHeight == null) {
			buff = Scalr.resize(buff, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, scaleWidth, scaleWidth);
		} else if (scaleWidth == null) {
			buff = Scalr.resize(buff, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, scaleHeight, scaleHeight);
		} else {
			buff = Scalr.resize(buff, Scalr.Method.QUALITY, exact ? Scalr.Mode.FIT_EXACT : Scalr.Mode.AUTOMATIC,
					scaleWidth, scaleHeight);
		}
		return buff;
	}

	public static void watermark(BufferedImage buff, BufferedImage watermark, int x, int y, float alpha) {
		Graphics2D g = buff.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		g.drawImage(watermark, x, y, null);
		g.dispose();
	}

	public static void watermark(BufferedImage buff, BufferedImage watermark, WatermarkParam watermarkInfo) {
		watermark(buff, watermark, watermarkInfo.getPosition(), watermarkInfo.getPaddingX(),
				watermarkInfo.getPaddingY(), watermarkInfo.getAlphaFloat(), watermarkInfo.getMinWidth(),
				watermarkInfo.getMinHeight());
	}

	public static void watermark(BufferedImage buff, BufferedImage watermark, int postion, int paddingX, int paddingY,
			float alpha, int minWidth, int minHeight) {
		int width = buff.getWidth();
		int height = buff.getHeight();
		int watermarkWidth = watermark.getWidth();
		int watermarkHeight = watermark.getHeight();
		if (width < minWidth || height < minHeight || watermarkWidth + paddingX > width
				|| watermarkHeight + paddingY > height) {
			return;
		}
		int x, y;
		switch (postion) {
		// NorthWest
		case 1: {
			x = paddingX;
			y = paddingY;
			break;
		}
		// North
		case 2: {
			x = width / 2 - watermarkWidth / 2;
			y = paddingY;
			break;
		}
		// NorthEast
		case 3: {
			x = width - watermarkWidth - paddingX;
			y = paddingY;
			break;
		}
		// West
		case 4: {
			x = paddingX;
			y = height / 2 - watermarkHeight / 2;
			break;
		}
		// Center
		case 5: {
			x = width / 2 - watermarkWidth / 2;
			y = height / 2 - watermarkHeight / 2;
			break;
		}
		// East
		case 6: {
			x = width - watermarkWidth - paddingX;
			y = height / 2 - watermarkHeight / 2;
			break;
		}
		// SouthWest
		case 7: {
			x = paddingX;
			y = height - watermarkHeight - paddingY;
			break;
		}
		// South
		case 8: {
			x = width / 2 - watermarkWidth / 2;
			y = height - watermarkHeight - paddingY;
			break;
		}
		// SouthEast
		case 9: {
			x = width - watermarkWidth - paddingX;
			y = height - watermarkHeight - paddingY;
			break;
		}
		default: {
			throw new IllegalArgumentException("postion must be 1..9");
		}
		}
		watermark(buff, watermark, x, y, alpha);
	}
}

package com.jspxcms.common.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;

import com.jspxcms.common.file.FilesEx;

public class ImageScalrHandler implements ImageHandler {

	@Override
	public boolean crop(String src, String dest, int x, int y, int width, int height) {
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (width <= 0) {
			throw new IllegalArgumentException("width must be > 0");
		}
		if (height <= 0) {
			throw new IllegalArgumentException("height must be > 0");
		}
		File srcFile = new File(src);
		File destFile = new File(dest);
		String formatName = Images.getFormatName(srcFile);
		// 不支持的图片格式
		if (formatName == null) {
			return false;
		}
		try {
			BufferedImage srcBuff = ImageIO.read(new File(src));
			int srcWidth = srcBuff.getWidth();
			int srcHeight = srcBuff.getHeight();
			if (x + width > srcWidth) {
				if (width > srcWidth) {
					width = srcWidth;
					x = 0;
				} else {
					x = srcWidth - width;
				}
			}
			if (y + height > srcHeight) {
				if (height > srcHeight) {
					height = srcHeight;
					y = 0;
				} else {
					y = srcHeight - height;
				}
			}
			// 宽高与原图一致，不做处理
			if (width == srcWidth && height == srcHeight) {
				return false;
			}
			BufferedImage destBuff = Scalr.crop(srcBuff, x, y, width, height);
			FilesEx.makeParentDir(destFile);
			ImageIO.write(destBuff, formatName, destFile);
			return true;
		} catch (IOException e) {
			FileUtils.deleteQuietly(destFile);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean resize(String src, String dest, ScaleParam scaleParam) {
		return resize(src, dest, scaleParam.getWidth(), scaleParam.getHeight(), scaleParam.isExact());
	}

	@Override
	public boolean resize(String src, String dest, Integer width, Integer height, boolean exact) {
		// 宽高都为null，不做处理
		if (width == null && height == null) {
			return false;
		}
		if (width != null && width <= 0) {
			throw new IllegalArgumentException("width must be > 0");
		}
		if (height != null && height <= 0) {
			throw new IllegalArgumentException("height must be > 0");
		}
		File srcFile = new File(src);
		File destFile = new File(dest);
		String formatName = Images.getFormatName(srcFile);
		// 不支持的图片格式
		if (formatName == null) {
			return false;
		}
		try {
			BufferedImage srcBuff = ImageIO.read(srcFile);
			int srcWidth = srcBuff.getWidth();
			int srcHeight = srcBuff.getHeight();
			if ((height == null && width >= srcWidth) || (width == null && height >= srcHeight)
					|| (height != null && width != null && height >= srcHeight && width >= srcWidth)) {
				return false;
			}
			BufferedImage destBuff;
			if (height == null) {
				destBuff = Scalr.resize(srcBuff, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, width, width);
			} else if (width == null) {
				destBuff = Scalr.resize(srcBuff, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, height, height);
			} else {
				destBuff = Scalr.resize(srcBuff, Scalr.Method.QUALITY, exact ? Scalr.Mode.FIT_EXACT
						: Scalr.Mode.AUTOMATIC, width, height);
			}
			FilesEx.makeParentDir(destFile);
			ImageIO.write(destBuff, formatName, destFile);
			return true;
		} catch (IOException e) {
			FileUtils.deleteQuietly(destFile);
			throw new RuntimeException(e);
		}
	}

	public void composite(BufferedImage overlayBuff, BufferedImage srcBuff, int x, int y, float alpha) {
		Graphics2D g = srcBuff.createGraphics();
		// 设置水印透明度
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
		// 水印不透明
		// g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g.drawImage(overlayBuff, x, y, null);
		g.dispose();
	}

	@Override
	public boolean composite(String overlay, String src, String dest, Integer x, Integer y, Integer dissolve) {
		if (x == null) {
			x = 0;
		}
		if (y == null) {
			y = 0;
		}
		if (dissolve == null) {
			dissolve = 100;
		}
		float alpha = (float) dissolve / (float) 100;

		File overlayFile = new File(overlay);
		File srcFile = new File(src);
		File destFile = new File(dest);
		String formatName = Images.getFormatName(srcFile);
		// 不支持的图片格式
		if (formatName == null) {
			return false;
		}
		try {
			BufferedImage overlayBuff = ImageIO.read(overlayFile);
			BufferedImage srcBuff = ImageIO.read(srcFile);
			composite(overlayBuff, srcBuff, x, y, alpha);
			FilesEx.makeParentDir(destFile);
			ImageIO.write(srcBuff, formatName, destFile);
			return true;
		} catch (IOException e) {
			FileUtils.deleteQuietly(destFile);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean composite(String overlay, String src, String dest, WatermarkParam watermarkParam) {
		// 水印关闭、水印文件未设置或水印文件不存在，不处理
		File overlayFile = new File(overlay);
		if (!overlayFile.exists()) {
			return false;
		}
		File srcFile = new File(src);
		File destFile = new File(dest);
		String formatName = Images.getFormatName(srcFile);
		// 不支持的图片格式
		if (formatName == null) {
			return false;
		}
		try {
			BufferedImage overlayBuff = ImageIO.read(overlayFile);
			BufferedImage srcBuff = ImageIO.read(srcFile);
			int overlayWidth = overlayBuff.getWidth();
			int overlayHeight = overlayBuff.getHeight();
			int width = srcBuff.getWidth();
			int height = srcBuff.getHeight();
			int minWidth = watermarkParam.getMinWidth();
			int minHeight = watermarkParam.getMinHeight();
			int paddingX = watermarkParam.getPaddingX();
			int paddingY = watermarkParam.getPaddingY();
			int position = watermarkParam.getPosition();
			float alpha = watermarkParam.getAlphaFloat();
			// 水印超出原图范围
			if (width < minWidth || height < minHeight || overlayWidth + paddingX > width
					|| overlayHeight + paddingY > height) {
				return false;
			}
			int x, y;
			switch (position) {
			// NorthWest
			case 1: {
				x = paddingX;
				y = paddingY;
				break;
			}
			// North
			case 2: {
				x = width / 2 - overlayWidth / 2;
				y = paddingY;
				break;
			}
			// NorthEast
			case 3: {
				x = width - overlayWidth - paddingX;
				y = paddingY;
				break;
			}
			// West
			case 4: {
				x = paddingX;
				y = height / 2 - overlayHeight / 2;
				break;
			}
			// Center
			case 5: {
				x = width / 2 - overlayWidth / 2;
				y = height / 2 - overlayHeight / 2;
				break;
			}
			// East
			case 6: {
				x = width - overlayWidth - paddingX;
				y = height / 2 - overlayHeight / 2;
				break;
			}
			// SouthWest
			case 7: {
				x = paddingX;
				y = height - overlayHeight - paddingY;
				break;
			}
			// South
			case 8: {
				x = width / 2 - overlayWidth / 2;
				y = height - overlayHeight - paddingY;
				break;
			}
			// SouthEast
			case 9: {
				x = width - overlayWidth - paddingX;
				y = height - overlayHeight - paddingY;
				break;
			}
			default: {
				throw new IllegalArgumentException("position must be 1..9");
			}
			}
			composite(overlayBuff, srcBuff, x, y, alpha);
			FilesEx.makeParentDir(destFile);
			ImageIO.write(srcBuff, formatName, destFile);
			return true;
		} catch (IOException e) {
			FileUtils.deleteQuietly(destFile);
			throw new RuntimeException(e);
		}
	}

}

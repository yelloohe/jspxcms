package com.jspxcms.common.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;

import com.jspxcms.common.file.FilesEx;

public class ImageMagickHandler implements ImageHandler {

	public ImageMagickHandler() {
	}

	public ImageMagickHandler(boolean userGM) {
		this.useGM = userGM;
	}

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
		try {
			ImImageInfo ii = getImageInfo(src);
			int origWidth = ii.getWidth();
			int origHeight = ii.getHeight();
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
				return false;
			}
			File destFile = new File(dest);
			FilesEx.makeParentDir(destFile);
			IMOperation op = new IMOperation();
			op.addImage(src);
			// 去除Exif信息
			// op.profile("*");
			op.crop(width, height, x, y);
			op.addImage(dest);
			ConvertCmd convertCmd = getConvertCmd();
			convertCmd.run(op);
			return true;
		} catch (Exception e) {
			FileUtils.deleteQuietly(new File(dest));
			throw new RuntimeException(e);
		}
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
		try {
			File destFile = new File(dest);
			FilesEx.makeParentDir(destFile);
			IMOperation op = new IMOperation();
			op.addImage(src);
			// 去除Exif信息
			// op.profile("*");
			if (exact) {
				// 按宽高生成图片（不按比例）
				op.resize(width, height, '!');
			} else {
				// 按比例只缩小不放大
				op.resize(width, height, '>');
			}
			op.addImage(dest);
			ConvertCmd convertCmd = getConvertCmd();
			convertCmd.run(op);
			return true;
		} catch (Exception e) {
			FileUtils.deleteQuietly(new File(dest));
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean resize(String src, String dest, ScaleParam scaleParam) {
		return resize(src, dest, scaleParam.getWidth(), scaleParam.getHeight(), scaleParam.isExact());
	}

	public boolean composite(String overlay, String src, String dest, Gravity gravity, Integer x, Integer y,
			Integer dissolve) {
		try {
			File destFile = new File(dest);
			FilesEx.makeParentDir(destFile);
			IMOperation op = new IMOperation();
			// 水印位置。NorthWest, North, NorthEast, West, Center, East, SouthWest, South, SouthEast. 默认为左上角：NorthWest。
			if (gravity != null) {
				op.gravity(gravity.name());
			}
			if (x != null || y != null) {
				op.geometry(null, null, x, y);
			}
			// 0-100透明度。0：完全透明，100：完全不透明。默认100。
			if (dissolve != null) {
				op.dissolve(dissolve);
			}
			op.addImage(overlay);
			op.addImage(src);
			op.addImage(dest);
			CompositeCmd compositeCmd = getCompositeCmd();
			compositeCmd.run(op);
			return true;
		} catch (Exception e) {
			FileUtils.deleteQuietly(new File(dest));
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean composite(String overlay, String src, String dest, Integer x, Integer y, Integer dissolve) {
		return composite(overlay, src, dest, null, x, y, dissolve);
	}

	@Override
	public boolean composite(String overlay, String src, String dest, WatermarkParam watermarkParam) {
		// 水印文件不存在，不处理
		if (!new File(overlay).exists()) {
			return false;
		}
		try {
			ImImageInfo overlayInfo = getImageInfo(overlay);
			ImImageInfo srcInfo = getImageInfo(src);
			int width = srcInfo.getWidth();
			int height = srcInfo.getHeight();
			int watermarkWidth = overlayInfo.getWidth();
			int watermarkHeight = overlayInfo.getHeight();
			if (width < watermarkParam.getMinWidth() || height < watermarkParam.getMinHeight()
					|| watermarkWidth + watermarkParam.getPaddingX() > width
					|| watermarkHeight + watermarkParam.getPaddingY() > height) {
				return false;
			}
			Gravity gravity = Gravity.values()[watermarkParam.getPosition() - 1];
			return composite(overlay, src, dest, gravity, watermarkParam.getPaddingX(), watermarkParam.getPaddingY(),
					watermarkParam.getAlpha());
		} catch (Exception e) {
			FileUtils.deleteQuietly(new File(dest));
			throw new RuntimeException(e);
		}
	}

	public boolean rotate(String src, String dest, Double degree) {
		// 度数为null，不处理
		if (degree == null) {
			return false;
		}
		// 一周是360度，不超过这个范围
		degree = degree % 360;
		if (degree < 0) {
			degree = 360 + degree;
		}
		// 度数0，不旋转，不处理
		if (degree == 0) {
			return false;
		}
		try {
			File destFile = new File(dest);
			FilesEx.makeParentDir(destFile);
			IMOperation op = new IMOperation();
			op.addImage(src);
			op.rotate(degree);
			op.addImage(dest);
			ConvertCmd convertCmd = getConvertCmd();
			convertCmd.run(op);
			return true;
		} catch (Exception e) {
			FileUtils.deleteQuietly(new File(dest));
			throw new RuntimeException(e);
		}
	}

	/**
	 * 上下（垂直）翻转图片
	 * 
	 * @param src
	 * @param dest
	 */
	public void flip(String src, String dest) {
		try {
			File destFile = new File(dest);
			FilesEx.makeParentDir(destFile);
			IMOperation op = new IMOperation();
			op.addImage(src);
			// 上下翻转
			op.flip();
			op.addImage(dest);
			ConvertCmd convertCmd = getConvertCmd();
			convertCmd.run(op);
		} catch (Exception e) {
			FileUtils.deleteQuietly(new File(dest));
			throw new RuntimeException(e);
		}

	}

	/**
	 * 左右（水平）翻转图片
	 * 
	 * @param src
	 * @param dest
	 */
	public void flop(String src, String dest) {
		try {
			File destFile = new File(dest);
			FilesEx.makeParentDir(destFile);
			IMOperation op = new IMOperation();
			op.addImage(src);
			// 左右翻转
			op.flop();
			op.addImage(dest);
			ConvertCmd convertCmd = getConvertCmd();
			convertCmd.run(op);
		} catch (Exception e) {
			FileUtils.deleteQuietly(new File(dest));
			throw new RuntimeException(e);
		}
	}

	public ImImageInfo getImageInfo(String path) throws IOException, InterruptedException, IM4JavaException {
		IMOperation op = new IMOperation();
		// 主要是宽高，其他的参数没有太大的作用，反而有可能因为ImageMagick升级，接口规则变化导致错误
		// op.format("%w,%h,%d,%f");
		op.format("%w,%h");
		op.addImage(path);
		IdentifyCmd identifyCmd = getIdentifyCmd();
		ArrayListOutputConsumer output = new ArrayListOutputConsumer();
		identifyCmd.setOutputConsumer(output);
		identifyCmd.run(op);
		ArrayList<String> cmdOutput = output.getOutput();
		if (cmdOutput.size() != 1) {
			return null;
		}
		String line = cmdOutput.get(0);
		String[] arr = line.split(",");
		return new ImImageInfo(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
	}

	public ConvertCmd getConvertCmd() {
		ConvertCmd cmd = new ConvertCmd(useGM);
		if (StringUtils.isNotBlank(searchPath)) {
			cmd.setSearchPath(searchPath);
		}
		return cmd;
	}

	public CompositeCmd getCompositeCmd() {
		CompositeCmd cmd = new CompositeCmd(useGM);
		if (StringUtils.isNotBlank(searchPath)) {
			cmd.setSearchPath(searchPath);
		}
		return cmd;
	}

	public IdentifyCmd getIdentifyCmd() {
		IdentifyCmd cmd = new IdentifyCmd(useGM);
		if (StringUtils.isNotBlank(searchPath)) {
			cmd.setSearchPath(searchPath);
		}
		return cmd;
	}

	private boolean useGM = false;
	private String searchPath = null;

	public boolean isUseGM() {
		return useGM;
	}

	public void setUseGM(boolean useGM) {
		this.useGM = useGM;
	}

	public String getSearchPath() {
		return searchPath;
	}

	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}

	public static enum Gravity {
		NorthWest, North, NorthEast, West, Center, East, SouthWest, South, SouthEast
	};

	public static class ImImageInfo {
		public ImImageInfo() {
		}

		public ImImageInfo(int width, int height) {
			this.width = width;
			this.height = height;
		}

		private int width;
		private int height;

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}
}

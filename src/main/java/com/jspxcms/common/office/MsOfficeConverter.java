package com.jspxcms.common.office;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 微软OFFICE转换工具
 * 
 * 需要将`jacob-1.18-x64.dll`放到`C:\Windows\System32`、`JDK的bin目录`以及`JRE的bin目录`（
 * 也许只要放在其中一个就可以了）。
 * 
 * @author liufang
 *
 */
public class MsOfficeConverter {
	private static Logger logger = LoggerFactory.getLogger(MsOfficeConverter.class);

	/**
	 * Word转PDF
	 */
	public static final int wdFormatPDF = 17;
	/**
	 * Excel转PDF
	 */
	public static final int xlTypePDF = 0;
	/**
	 * PPT转PDF
	 */
	public static final int ppSaveAsPDF = 32;

	/**
	 * Word转HTML
	 */
	public static final int wdFormatHTML = 8;
	/**
	 * Word转FilteredHTML
	 */
	public static final int wdFormatFilteredHTML = 10;
	/**
	 * Excel转HTML
	 */
	public static final int xlHtml = 44;
	/**
	 * PPT转HTML。Office2010及以前版本可用，Office2013及以后版本不可用
	 */
	public static final int ppSaveAsHTML = 12;

	public static boolean wordSaveAsFilteredHTML(String inputFilename, String outputFilename) {
		return wordSaveAs(inputFilename, outputFilename, wdFormatFilteredHTML);
	}

	public static boolean wordSaveAsPDF(String inputFilename, String outputFilename) {
		return wordSaveAs(inputFilename, outputFilename, wdFormatPDF);
	}

	public static boolean wordSaveAs(String inputFilename, String outputFilename, int saveAsFormat) {
		boolean successed = false;
		String inputName = FilenameUtils.getName(inputFilename);
		String outputExtension = FilenameUtils.getExtension(outputFilename);
		ComThread.InitSTA();
		try {
			ActiveXComponent axc = null;
			Dispatch dispatch = null;
			try {
				long startTime = System.currentTimeMillis();
				axc = new ActiveXComponent("Word.Application");
				axc.setProperty("Visible", new Variant(false));
				axc.setProperty("AutomationSecurity", new Variant(3));
				Dispatch office = axc.getProperty("Documents").toDispatch();
				dispatch = Dispatch.call(office, "Open", inputFilename, false, true).toDispatch();
				Dispatch.call(dispatch, "SaveAs", outputFilename, saveAsFormat);
				long conversionTime = System.currentTimeMillis() - startTime;
				logger.info(String.format("successful conversion: %s to %s in %dms", inputName, outputExtension,
						conversionTime));
				successed = true;
			} finally {
				Dispatch.call(dispatch, "Close", false);
				if (axc != null) {
					axc.invoke("Quit", new Variant[] {});
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
		}
		ComThread.Release();
		ComThread.quitMainSTA();
		return successed;
	}

	public static boolean excelSaveAsHTML(String inputFilename, String outputFilename) {
		return excelSaveAs(inputFilename, outputFilename, xlHtml);
	}

	public static boolean excelSaveAsPDF(String inputFilename, String outputFilename) {
		return excelSaveAs(inputFilename, outputFilename, xlTypePDF);
	}

	public static boolean excelSaveAs(String inputFilename, String outputFilename, int saveAsFormat) {
		boolean successed = false;
		String inputName = FilenameUtils.getName(inputFilename);
		String outputExtension = FilenameUtils.getExtension(outputFilename);
		ComThread.InitSTA();
		try {
			ActiveXComponent axc = null;
			Dispatch dispatch = null;
			try {
				long startTime = System.currentTimeMillis();
				axc = new ActiveXComponent("Excel.Application");
				axc.setProperty("Visible", new Variant(false));
				// axc.setProperty("AutomationSecurity", new Variant(3));

				Dispatch office = axc.getProperty("Workbooks").toDispatch();
				dispatch = Dispatch.call(office, "Open", inputFilename, false, true).toDispatch();
				// Excel 不能调用SaveAs方法
				Dispatch.call(dispatch, "ExportAsFixedFormat", saveAsFormat, outputFilename);
				long conversionTime = System.currentTimeMillis() - startTime;
				logger.info(String.format("successful conversion: %s to %s in %dms", inputName, outputExtension,
						conversionTime));
				successed = true;
			} finally {
				Dispatch.call(dispatch, "Close", false);
				if (axc != null) {
					axc.invoke("Quit", new Variant[] {});
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
		}
		ComThread.Release();
		ComThread.quitMainSTA();
		return successed;
	}

	/**
	 * PPT转HTML。Office2010及以前版本可用，Office2013及以后版本不可用
	 * 
	 * @param inputFilename
	 * @param outputFilename
	 * @return
	 */
	public static boolean pptSaveAsHTML(String inputFilename, String outputFilename) {
		return pptSaveAs(inputFilename, outputFilename, ppSaveAsHTML);
	}

	public static boolean pptSaveAsPDF(String inputFilename, String outputFilename) {
		return pptSaveAs(inputFilename, outputFilename, ppSaveAsPDF);
	}

	public static boolean pptSaveAs(String inputFilename, String outputFilename, int saveAsFormat) {
		boolean successed = false;
		String inputName = FilenameUtils.getName(inputFilename);
		String outputExtension = FilenameUtils.getExtension(outputFilename);
		ComThread.InitSTA();
		try {
			ActiveXComponent axc = null;
			Dispatch dispatch = null;
			try {
				long startTime = System.currentTimeMillis();
				axc = new ActiveXComponent("PowerPoint.Application");
				// axc.setProperty("Visible", new Variant(false));
				// axc.setProperty("AutomationSecurity", new Variant(3));

				Dispatch office = axc.getProperty("Presentations").toDispatch();
				// true ReadOnly
				// true Untitled指定文件是否有标题
				// false WithWindow指定文件是否可见
				dispatch = Dispatch.call(office, "Open", inputFilename, true, true, false).toDispatch();
				Dispatch.call(dispatch, "SaveAs", inputFilename, saveAsFormat);
				long conversionTime = System.currentTimeMillis() - startTime;
				logger.info(String.format("successful conversion: %s to %s in %dms", inputName, outputExtension,
						conversionTime));
				successed = true;
			} finally {
				Dispatch.call(dispatch, "Close", false);
				if (axc != null) {
					axc.invoke("Quit", new Variant[] {});
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
		}
		ComThread.Release();
		ComThread.quitMainSTA();
		return successed;
	}

	public static void main(String[] args) throws Exception {
		String src1 = "D:\\test\\word2html\\Jspxcms.docx";
		String dest1 = "D:\\test\\word2html\\Jspxcms_msoffice.pdf";
		wordSaveAsPDF(src1, dest1);
	}

}

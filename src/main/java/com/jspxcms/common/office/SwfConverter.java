package com.jspxcms.common.office;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// D:\\programcommon\\swftools\\pdf2swf.exe Paper.pdf -o Paper%.swf -f -T 9 -t -s storeallcharacters
public class SwfConverter {
	private static final Logger logger = LoggerFactory
			.getLogger(SwfConverter.class);

	public static void pdf2swf(File from, File to, String exe,
			String languagedir) throws Exception {
		// Runtime r = Runtime.getRuntime();
		// ProcessBuilder b;
		// Process p = r.exec(exe + " " + from.getPath() + " -o " + to.getPath()
		// + " -f -T 9 -t -v -G -s storeallcharacters -s poly2bitmap");
		// // -G -s poly2bitmap
		// logger.debug(IOUtils.toString(p.getInputStream()));
		// logger.debug(IOUtils.toString(p.getErrorStream()));

		String[] command = { exe, from.getPath(), "-o", to.getPath(), "-f",
				"-T", "9", "-t", "-v", "-s", "storeallcharacters", "-s",
				"poly2bitmap" };
		if (StringUtils.isNotBlank(languagedir)) {
			command = ArrayUtils.addAll(command, "-s", "languagedir="
					+ languagedir);
		}
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(command);
		Process process = processBuilder.start();

		// dealWith(process);//改用下面的方式来处理：
		InputStreamWathThread inputWathThread = new InputStreamWathThread(
				process);
		inputWathThread.start();
		ErrorInputStreamWathThread errorInputWathThread = new ErrorInputStreamWathThread(
				process);
		errorInputWathThread.start();

		try {
			process.waitFor();// 等待子进程的结束，子进程就是系统调用文件转换这个新进程
			inputWathThread.setOver(true);// 转换完，停止流的处理
			errorInputWathThread.setOver(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.debug("convert complete");
	}

	// private static boolean isWindows() {
	// String osName = System.getProperty("os.name");
	// return osName.toLowerCase().indexOf("windows") != -1;
	// }

	public static void main(String[] args) throws Exception {
		File pdfFile = new File("d:\\jspxcms.pdf");
		File swfFile = new File("d:\\jspxcms.swf");
		String exe = "D:\\p_work\\swftools\\pdf2swf.exe";
		String languagedir = "D:\\p_work\\xpdf\\chinese-simplified";
		pdf2swf(pdfFile, swfFile, exe, languagedir);
	}
}

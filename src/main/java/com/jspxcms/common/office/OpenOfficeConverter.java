package com.jspxcms.common.office;

import java.io.File;
import java.net.ConnectException;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.ExternalOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

// cd C:\Program Files (x86)\OpenOffice 4\program\
// soffice -headless -accept="socket,host=127.0.0.1,port=2002;urp;" -nofirststartwizard
// /opt/openoffice4/program/soffice -headless -accept="socket,host=127.0.0.1,port=2002;urp;" -nofirststartwizard &
// ./soffice "-accept=socket,host=localhost,port=2002;urp;StarOffice.ServiceManager" -nologo -headless -nofirststartwizard
public class OpenOfficeConverter {
	// private static final Logger logger = LoggerFactory
	// .getLogger(OpenOfficeConverter.class);

	/**
	 * 转换文件，OpenOffice必须已经启动
	 * 
	 * @param from
	 * @param to
	 * @param port
	 * @throws ConnectException
	 */
	public static void convert(File from, File to, int port)
			throws ConnectException {
		ExternalOfficeManagerConfiguration conf = new ExternalOfficeManagerConfiguration();
		conf.setPortNumber(port);
		OfficeManager officeManager = conf.buildOfficeManager();
		// OfficeManager可纳入spring管理，通过officeManager.isRunning()判断是否正在运行。
		try {
			officeManager.start();
			OfficeDocumentConverter converter = new OfficeDocumentConverter(
					officeManager);
			converter.convert(from, to);
		} finally {
			if (officeManager.isRunning()) {
				officeManager.stop();
			}
		}
	}

	/**
	 * 转换文件，并自动启动、关闭OpenOffice，端口默认2002
	 * 
	 * @param from
	 * @param to
	 * @param home
	 */
	public static void convert(File from, File to, String home) {
		DefaultOfficeManagerConfiguration conf = new DefaultOfficeManagerConfiguration();
		conf.setOfficeHome(home);
		OfficeManager officeManager = conf.buildOfficeManager();
		officeManager.start();
		OfficeDocumentConverter converter = new OfficeDocumentConverter(
				officeManager);
		converter.convert(from, to);
		officeManager.stop();
	}

	public static void main(String[] args) throws Exception {
		File src1 = new File("D:\\test\\word2html\\Jspxcms.docx");
		File dest1 = new File("D:\\test\\word2html\\Jspxcms_openoffice.pdf");
		int port = 2002;
		convert(src1, dest1, port);
		// String home = "C:\\Program Files (x86)\\OpenOffice 4";
		// convert(src1, dest1, home);
	}
}

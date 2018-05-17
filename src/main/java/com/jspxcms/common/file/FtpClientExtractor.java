package com.jspxcms.common.file;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public interface FtpClientExtractor {
	public void doInFtp(FTPClient client) throws IOException;
}

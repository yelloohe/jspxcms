package com.jspxcms.common.file;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpTemplate {
	private static final Logger logger = LoggerFactory
			.getLogger(FtpTemplate.class);

	private String hostname;
	private Integer port;
	private String username;
	private String password;
	private String encoding;

	public FtpTemplate(String hostname, Integer port, String username,
			String password) {
		this(hostname, port, username, password, "UTF-8");
	}

	public FtpTemplate(String hostname, Integer port, String username,
			String password, String encoding) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.encoding = encoding;
	}

	public void execute(FtpClientExtractor fce) {
		FTPClient client = new FTPClient();
		if (StringUtils.isNotBlank(encoding)) {
			client.setControlEncoding(encoding);
		}
		FTPClientConfig config = new FTPClientConfig();
		client.configure(config);
		try {
			try {
				if (port != null) {
					client.connect(hostname, port);
				} else {
					client.connect(hostname);
				}
				client.login(username, password);
				int reply = client.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					client.disconnect();
					throw new RuntimeException("FTP server refused connection.");
				}
				client.setFileType(FTPClient.BINARY_FILE_TYPE);
				fce.doInFtp(client);
			} finally {
				if (client.isConnected()) {
					client.disconnect();
				}
			}
		} catch (IOException e) {
			logger.error("ftp error!", e);
		}
	}
}

package com.jspxcms.common.office;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorInputStreamWathThread extends Thread {
	private Process process = null;
	private boolean over = false;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public ErrorInputStreamWathThread(Process p) {
		process = p;
		over = false;
	}

	public void run() {
		try {
			if (process == null) {
				logger.info("process cannot be null");
				return;
			}

			// 对出错流的处理
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));

			while (true) {
				if (process == null || over) {
					logger.info("process complete");
					break;
				}
				String temp;
				while ((temp = br.readLine()) != null) {
					logger.info(temp);
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
		}
	}

	public void setOver(boolean over) {
		this.over = over;
	}
}

package com.jspxcms.common.office;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputStreamWathThread extends Thread {
	private Process process = null;
	private boolean over = false;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public InputStreamWathThread(Process p) {
		process = p;
		over = false;
	}

	public void run() {
		try {
			if (process == null) {
				logger.info("process cannot be null");
				return;
			}

			// 对输入流，可能是一个回车之类的输入
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			while (true) {
				if (process == null || over) {
					logger.info("process complete");
					break;
				}
				String temp;
				while ((temp = br.readLine()) != null) {
					// 如这些信息:NOTICE processing PDF page 10
					logger.debug(temp);
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
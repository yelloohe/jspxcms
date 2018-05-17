package com.jspxcms.plug.dbbackup;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DbBackupExcutorImpl implements DbBackupExcutor {
	private static final Logger logger = LoggerFactory
			.getLogger(DbBackupExcutorImpl.class);

	public void backup(File dir, boolean dataOnly) {
		new DbBackupThread(dir, dataOnly).start();
	}

	public void restore(File file) {
		try {
			service.restore(file);
		} catch (IOException e) {
			logger.error("database backup error!", e);
		}
		// new DbRestoreThread(file).start();
	}

	public class DbBackupThread extends Thread {
		private File dir;
		private boolean dataOnly;

		public DbBackupThread(File dir, boolean dataOnly) {
			this.dir = dir;
			this.dataOnly = dataOnly;
		}

		@Override
		public void run() {
			try {
				service.backup(dir, dataOnly);
			} catch (IOException e) {
				logger.error("database backup error!", e);
			}
		}
	}

	public class DbRestoreThread extends Thread {
		private File file;

		public DbRestoreThread(File file) {
			this.file = file;
		}

		@Override
		public void run() {
			try {
				service.restore(file);
			} catch (IOException e) {
				logger.error("database backup error!", e);
			}
		}
	}

	private DbBackupService service;

	@Autowired
	public void setDbBackupService(DbBackupService service) {
		this.service = service;
	}
}

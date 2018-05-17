package com.jspxcms.plug.dbbackup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据库备份ServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional
public class DbBackupServiceImpl implements DbBackupService {
	private static final String NEWLINE = "\r\n";

	public String getSchema() {
		return dao.getSchema();
	}

	public List<String> getTables(String schema) {
		return dao.getTables(schema);
	}

	public String getTableDDL(String schema, String table) {
		return dao.getTableDDL(schema, table);
	}

	public void backup(File dir, boolean dataOnly) throws IOException {
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String schema = getSchema();
		List<String> tables = getTables(schema);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String filename = "jspxcms-" + df.format(new Date())
				+ (dataOnly ? "-data" : "") + ".sql";
		File file = new File(dir, filename);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF-8"));
		writer.write("/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;"
				+ NEWLINE);
		writer.write("/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;"
				+ NEWLINE);
		writer.write("/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;"
				+ NEWLINE);
		writer.write("/*!40101 SET NAMES utf8 */;" + NEWLINE);
		writer.write("SET FOREIGN_KEY_CHECKS=0;" + NEWLINE);
		if (!dataOnly) {
			for (String table : tables) {
				writer.write(getTableDDL(schema, table));
			}
		}
		for (String table : tables) {
			dao.writeData(schema, table, writer);
		}
		writer.write("/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;"
				+ NEWLINE);
		writer.write("/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;"
				+ NEWLINE);
		writer.write("/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;"
				+ NEWLINE);
		writer.flush();
		writer.close();
	}

	public void restore(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
		dao.restore(reader);
	}

	public String getTableData(String schema, String table) {
		BufferedWriter writer = new BufferedWriter(new StringWriter());
		dao.writeData(schema, table, writer);
		return writer.toString();
	}

	private DbBackupDao dao;

	@Autowired
	public void setDao(DbBackupDao dao) {
		this.dao = dao;
	}
}

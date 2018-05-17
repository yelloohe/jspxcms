package com.jspxcms.plug.dbbackup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * 数据库备份Dao
 * 
 * @author liufang
 * 
 */
public interface DbBackupDao {
	public String getSchema();

	public List<String> getTables(String schema);

	public String getTableDDL(String schema, String table);

	public void writeData(String schema, final String table,
			final BufferedWriter writer);

	public void restore(BufferedReader reader) throws DataAccessException,
			IOException;
}

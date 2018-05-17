package com.jspxcms.plug.dbbackup;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 数据库备份Service
 * 
 * @author liufang
 * 
 */
public interface DbBackupService {
	public String getSchema();

	public List<String> getTables(String schema);

	public String getTableDDL(String schema, String table);

	public void backup(File dir, boolean dataOnly) throws IOException;

	public void restore(File file) throws IOException;

	public String getTableData(String schema, String table);
}

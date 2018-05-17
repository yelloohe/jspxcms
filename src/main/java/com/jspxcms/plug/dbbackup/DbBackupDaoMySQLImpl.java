package com.jspxcms.plug.dbbackup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * 数据库备份DaoImpl
 * 
 * @author liufang
 * 
 */
@Repository
public class DbBackupDaoMySQLImpl implements DbBackupDao {
	// private static final Logger logger = LoggerFactory
	// .getLogger(DbBackupDaoMySQLImpl.class);
	private static final String NEWLINE = "\r\n";
	private static final SimpleDateFormat DF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public String getSchema() {
		String sql = "select DATABASE()";
		String schema = jdbc.queryForObject(sql, String.class);
		return schema;
	}

	public List<String> getTables(String schema) {
		String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = ?";
		List<String> tables = jdbc.queryForList(sql, new Object[] { schema },
				String.class);
		return tables;
	}

	public String getTableDDL(String schema, String table) {
		String sql = "show create table `" + schema + "`.`" + table + "`";
		String ddl = "DROP TABLE IF EXISTS `" + table + "`;" + NEWLINE
				+ jdbc.query(sql, new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						if (rs.next()) {
							return rs.getString(2);
						} else {
							return null;
						}
					}
				}) + ";" + NEWLINE;
		return ddl;
	}

	public void writeData(String schema, final String table,
			final BufferedWriter writer) {
		String sql = "select * from `" + schema + "`.`" + table + "`";
		jdbc.query(sql, new ResultSetExtractor<String>() {
			public String extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				try {
					ResultSetMetaData md = rs.getMetaData();
					int count = md.getColumnCount();
					Object obj;
					while (rs.next()) {
						writer.write("INSERT INTO `" + table + "` (");
						for (int i = 1; i <= count; i++) {
							writer.write("`");
							writer.write(md.getColumnName(i));
							writer.write("`");
							if (i < count) {
								writer.write(',');
							}
						}
						writer.write(") VALUES (");
						for (int i = 1; i <= count; i++) {
							obj = rs.getObject(i);
							if (obj == null) {
								writer.write("NULL");
							} else if (obj instanceof Number) {
								writer.write(obj.toString());
							} else if (obj instanceof Date) {
								writer.write("'");
								writer.write(DF.format((Date) obj));
								writer.write("'");
							} else {
								writer.write("'");
								String s = obj.toString();
								s = StringUtils.replace(s, "\r", "\\r");
								s = StringUtils.replace(s, "\n", "\\n");
								s = StringUtils.replace(s, "'", "''");
								writer.write(s);
								writer.write("'");
							}
							if (i < count) {
								writer.write(',');
							}
						}
						writer.write(");");
						writer.write(NEWLINE);
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
	}

	public void restore(BufferedReader reader) throws DataAccessException,
			IOException {
		String line = null;
		StringBuilder sqlBuilder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			if (StringUtils.isBlank(line) || line.startsWith("--")) {
				continue;
			}
			sqlBuilder.append(line);
			if (line.endsWith(";")) {
				sqlBuilder.setLength(sqlBuilder.length() - 1);
				String sql = sqlBuilder.toString();
				jdbc.execute(sql);
				sqlBuilder.setLength(0);
			}
		}
	}

	private JdbcTemplate jdbc;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
}

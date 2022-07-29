package ddc.support.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ddc.support.jdbc.SqlRowHandler;
import ddc.support.util.Chronometer;
import ddc.support.util.LogListener;

public class EntitySql {
	private static LogListener logger = null; // new LogConsole(SqlUtils.class);
	private final static int RS_TYPE = ResultSet.TYPE_FORWARD_ONLY;// .TYPE_SCROLL_INSENSITIVE;
	private final static int RS_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;
	private final static int FETCH_SIZE = 100000;
	private final static int VERBOSE_COUNT = 10;

	public static void setLogListener(LogListener logger) {
		EntitySql.logger = logger;
	}

	private static void log(String message) {
		if (logger != null) {
			log(message);
		}
	}

	public static String sqlString(String source) {
		if (source == null)
			source = "";
		source = source.replaceAll("'", "''");
		return "'" + source + "'";
	}

	public static void select(Connection connection, String sql, SqlRowHandler handler) throws Exception {
		log("Executing... sql:[" + sql + "]");
		Statement statement = null;
		ResultSet rs = null;
		try {
			Chronometer chron = new Chronometer();
			statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.FETCH_FORWARD);
//			statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			statement.setFetchSize(FETCH_SIZE);
			rs = statement.executeQuery(sql);
			// ResultSetMetaData meta = rs.getMetaData();
			long counter = 0;
			while (rs.next()) {
				counter++;
				handler.handle(counter, rs);
//				if (counter <= VERBOSE_COUNT) {
//					printHandler(counter, rs, 40);
//					if (counter == VERBOSE_COUNT)
//						log("more rows....");
//				}
			}
			log("Executed - sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		}
	}

//    public DataSet readTable(String filename) {
//        DataTable t = new DataTable();
//        t.ReadXml(filename);
//        return t;
//    }

//    public DataTable updateTable(String connectionString, String filename) {
//        DataTable t = new DataTable();
//        t.ReadXml(filename);
//        return t;
//    }

//    public DataTable selectTable(String connectionString, String table) {
//        String sql = "SELECT * FROM " + table;
//        return select(connectionString, sql);
//    }

	public void selectTable(Connection connection, String table, String orderByColumn, SqlRowHandler handler)
			throws Exception {
		String sql = "SELECT * FROM " + table + " ORDER BY " + orderByColumn;
		select(connection, sql, handler);
	}

	public void selectDetailsRows(Connection connection, String table, String fkName, String fkValue,
			SqlRowHandler handler) throws Exception {
		String sql = "SELECT * FROM " + table;
		sql += " WHERE " + fkName + "=" + sqlString(fkValue);
		select(connection, sql, handler);
	}

	public void selectDetailsRows(Connection connection, String table, String fkName, long fkValue,
			SqlRowHandler handler) throws Exception {
		String sql = "SELECT * FROM " + table;
		sql += " WHERE " + fkName + "=" + fkValue;
		select(connection, sql, handler);
	}

//	public void select(String connectionString, String sql) {
//		Chronometer chron = new Chronometer();
//		SqlConnection connection = new SqlConnection(connectionString);
//		SqlCommand command = new SqlCommand(sql, connection);
//		SqlDataAdapter sqlDataAdapter = new SqlDataAdapter(command);
//		DataTable dataTable = new DataTable();
//		try {
//			sqlDataAdapter.Fill(dataTable);
//		} catch (Exception e) {
//			logger.error("select sql:[" + sql + "] elapsed:[" + chron.ElapsedTimeMillis + "]", e);
//		} finally {
//			if (connection != null)
//				connection.Close();
//		}
//		logger.debug("select sql:[" + sql + "] elapsed:[" + chron.ElapsedTimeMillis + "]");
//		return dataTable;
//	}

	public int deleteRows(Connection connection, String table, String fieldName, String fieldValue) throws SQLException {
		Chronometer chron = new Chronometer();
		String sql = "DELETE " + table + " WHERE " + fieldName + "=" + sqlString(fieldValue);
		logger.debug("delete sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
		return update(connection, sql);
	}

	public int deleteRows(Connection connection, String table, String fieldName, long fieldValue) throws SQLException {
		Chronometer chron = new Chronometer();
		String sql = "DELETE " + table + " WHERE " + fieldName + "=" + fieldValue;
		logger.debug("delete sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
		return update(connection, sql);
	}

//	public void writeTableToFile(String connectionString, String table, String filename) {
//		String sql = "SELECT * FROM " + table;
//		DataTable t = select(connectionString, sql);
//		t.TableName = table;
//		t.WriteXml(filename, XmlWriteMode.WriteSchema);
//	}

//	public void importTableFromFile(String connectionString, String table, String filename) {
//		Chronometer chron = new Chronometer();
//		SqlConnection connection = new SqlConnection(connectionString);
//		SqlBulkCopy bulkCopy = new SqlBulkCopy(connection);
//		bulkCopy.DestinationTableName = table;
//		DataTable newTable = readTable(filename);
//		DataTableReader reader = newTable.CreateDataReader();
//		try {
//			connection.Open();
//			bulkCopy.WriteToServer(reader);
//		} catch (Exception e) {
//			logger.error("importTableFromFile table:[" + table + "] elapsed:[" + chron.ElapsedTimeMillis + "]", e);
//		} finally {
//			if (reader != null)
//				reader.Close();
//			if (connection != null)
//				connection.Close();
//		}
//		logger.debug("importTableFromFile table:[" + table + "] elapsed:[" + chron.ElapsedTimeMillis + "]");
//	}

//	public void select(Connection connection, String tableName, String pkName, Object value) {
//		Chronometer chron = new Chronometer();
//		String sql = "SELECT * FROM " + tableName + " WHERE " + pkName + "=@" + pkName;
//		
//		SqlCommand command = new SqlCommand(sql, connection);
//		command.Parameters.Add(new SqlParameter("@" + pkName, value));
//		SqlDataAdapter sqlDataAdapter = new SqlDataAdapter(command);
//		DataTable dataTable = new DataTable();
//		try {
//			sqlDataAdapter.Fill(dataTable);
//		} catch (Exception e) {
//			logger.error("select sql:[" + sql + "] elapsed:[" + chron.ElapsedTimeMillis + "]", e);
//		} finally {
//			if (connection != null)
//				connection.Close();
//		}
//		logger.debug("select sql:[" + sql + "] elapsed:[" + chron.ElapsedTimeMillis + "]");
//		return dataTable;
//	}

	public long selectCount(Connection connection, String table, String field, String value) throws SQLException {
		String where = field + "=" + sqlString(value);
		return selectCount(connection, table, where);
	}

	public long selectCount(Connection connection, String table, String field, long value) throws SQLException {
		String where = field + "=" + value;
		return selectCount(connection, table, where);
	}

	public long selectCount(Connection connection, String table, String whereCondition) throws SQLException {
		String sql = "SELECT COUNT(*) FROM " + table;
		if (whereCondition != null && whereCondition.length() > 0) {
			sql += " WHERE " + whereCondition;
		}
		return selectScalar(connection, sql);
	}

//    String empid="";
//    int sequence=0;
//    try {
//        Statement st=conn.createStatement();
//        ResultSet rs=st.executeQuery("select empid_seq.NEXTVAL from dual;");
//        if (rs.next() ) {  
//          sequence = rs.getInt(1);  
//        }
//        empid="EMP"+Integer.toString(sequence);
//    } catch (SQLException ex) {
//        Logger.getLogger(Global.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    return empid;
//}

	public long selectScalar(Connection connection, String sql) throws SQLException {
		Statement statement = null;
		ResultSet rs = null;
		long result = 0;
		try {
			Chronometer chron = new Chronometer();
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.setFetchSize(FETCH_SIZE);
			rs = statement.executeQuery(sql);
			// ResultSetMetaData meta = rs.getMetaData();
			if (rs.next()) {
				result = rs.getLong(1);
			}
			log("Executed - sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
			return result;
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		}
	}

	public int update(Connection connection, String sql) throws SQLException {
		Statement statement = null;
		int affected = 0;
		try {
			Chronometer chron = new Chronometer();
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.setFetchSize(FETCH_SIZE);
			affected = statement.executeUpdate(sql);
			log("update affected:[" + affected + "] sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
			return affected;
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
		}
	}

//	public void tryConnection(Connection connection) {
//        Chronometer chron = new Chronometer();
//        SqlConnection connection = new SqlConnection(connectionString);
//        using (SqlConnection conn = new SqlConnection(connectionString)) {
//            try {
//                conn.Open(); // throws if invalid
//                logger.debug("TestDbConnection() connectionString:[" + connectionString + "] elapsed:[" + chron.ElapsedTimeMillis + "]");
//            } catch (Exception e) {
//                logger.error("TestDbConnection() connectionString:[" + connectionString + "] exception:[" + e.Message + "] elapsed:[" + chron.ElapsedTimeMillis + "]", e);
//                throw e;
//            }
//        }
//    }

	public static String buildSqlInsert(String table, String autoValue, Map<String, Object> datas, boolean avoidNull) {
		StringBuilder names = new StringBuilder();
		StringBuilder values = new StringBuilder();
		String separator = "";
		for (String name : datas.keySet()) {
			if (avoidNull && datas.get(name) == null)
				continue;
			if (!name.equals(autoValue)) {
				names.append(separator);
				values.append(separator);
				//
				separator = ",";
				names.append(name);
				values.append("@").append(name);
			}
		}
		return "INSERT INTO " + table + "(" + names.toString() + ") VALUES ( " + values.toString() + ")";
	}

	public static String buildSqlUpdate(String table, String pkName, Map<String, Object> datas, boolean avoidNull) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(table).append(" SET ");
		String separator = "";
		for (String name : datas.keySet()) {
			if (avoidNull && datas.get(name) == null)
				continue;
			if (name != pkName) {
				sql.append(separator);
				separator = ",";
				sql.append(name).append("=?");
			}
		}
		if (StringUtils.isNotBlank(pkName)) {
			sql.append(" WHERE ").append(pkName).append("=?");
		}
		return sql.toString();
	}

	public static String buildOrClause(String fieldName, String[] values) {
		String clause = " ";
		String separator = "";
		for (String v : values) {
			clause += separator + fieldName + "='" + v + "'";
			separator = " OR ";
		}
		return "(" + clause + ")";
	}

	public static String buildSqlUpdate(String table, Map<String, Object> datas, Map<String, Object> whereAndClause,
			boolean avoidNull) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(table).append(" SET ");
		String separator = "";
		for (String name : datas.keySet()) {
			if (datas.get(name) == null && avoidNull)
				continue;
			sql.append(separator);
			sql.append(name).append("=?");
			separator = ",";
		}

		sql.append(" WHERE ");
		separator = "";
		for (String name : whereAndClause.keySet()) {
			sql.append(separator);
			sql.append(name).append("=?");
			separator = " AND ";
		}
		return sql.toString();
	}

	public int update(Connection connection, String table, String pkName, Map<String, Object> datas,
			boolean avoidNull) throws SQLException {
		Chronometer chron = new Chronometer();		
		PreparedStatement prepared = null;
		int affected = 0;
		int counter = 1;
		String sql = null;
		try {
			sql = buildSqlUpdate(table, pkName, datas, avoidNull);
			prepared = connection.prepareStatement(sql);
			for (String name : datas.keySet()) {
				if (datas.get(name) == null && avoidNull)
					continue;
				prepared.setObject(counter, datas.get(name));
				counter++;
			}
			affected = prepared.executeUpdate();
			connection.commit();
			logger.debug(
					"update affected:[" + affected + "] sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
		} catch (Exception e) {
			logger.error("update sql:[" + sql + "] elapsed:[" + chron.toString() + "]", e);
		} finally {
			if (connection != null)
				connection.close();
		}
		return affected;
	}
//
//	public int update(Connection connection, String table, Dictionary<String, Object> datas, Dictionary<String, Object> whereAndClauses, bool avoidNull) {
//        Chronometer chron = new Chronometer();
//        SqlConnection connection = new SqlConnection(connectionString);
//        int affected = 0;
//        String sql = null;
//        try {
//            sql = buildSqlUpdate(table, datas, whereAndClauses, avoidNull);
//            SqlCommand command = new SqlCommand(sql, connection);
//            foreach (String name in datas.Keys) {
//                if (datas[name] == null && avoidNull) continue;
//                command.Parameters.Add(new SqlParameter("@" + name, datas[name]));
//            }
//            connection.Open();
//            affected = command.ExecuteNonQuery();
//            logger.debug("update affected:[" + affected + "] sql:[" + sql + "] elapsed:[" + chron.ElapsedTimeMillis + "]");
//        } catch (Exception e) {
//            logger.error("update sql:[" + sql + "] elapsed:[" + chron.ElapsedTimeMillis + "]", e);
//        } finally {
//            if (connection != null)
//                connection.Close();
//        }
//        return affected;
//    }
//
//	public int delete(Connection connection, String tableName, String pkName, Object value) {
//		Chronometer chron = new Chronometer();
//		SqlConnection connection = new SqlConnection(connectionString);
//		int affected = 0;
//		String sql = "DELETE FROM " + tableName + " WHERE " + pkName + "=@" + pkName;
//		try {
//			SqlCommand command = new SqlCommand(sql, connection);
//			command.Parameters.Add(new SqlParameter("@" + pkName, value));
//			connection.Open();
//			affected = command.ExecuteNonQuery();
//			logger.debug(
//					"delete affected:[" + affected + "] sql:[" + sql + "] elapsed:[" + chron.ElapsedTimeMillis + "]");
//		} catch (Exception e) {
//			logger.error("delete sql:[" + sql + "] elapsed:[" + chron.ElapsedTimeMillis + "]", e);
//		} finally {
//			if (connection != null)
//				connection.Close();
//		}
//		return affected;
//	}
//
//	public static void logTable(String tableName, DataTable table, int logLevel) {
//        if (!Logger.isLevelEnabled(logLevel)) return;
//        logger.log(logLevel, "------------------------------------------- Start");
//        if (table == null) return;
//        logger.log(logLevel, "Table:[" + tableName + "] rows:[" + table.Rows.Count + "]");
//        String cs = "";
//        foreach (DataColumn c in table.Columns) {
//            int size = 30;
//            if (c.MaxLength > 0) size = c.MaxLength;
//            cs += c.ColumnName.PadRight(size) + " |";
//        }
//        logger.log(logLevel, cs);
//        foreach (DataRow dr in table.Rows) {
//            String rs = "";
//            foreach (DataColumn c in table.Columns) {
//                int size = 30;
//                if (c.MaxLength > 0) size = c.MaxLength;
//                String v = dr[c.ColumnName].ToString().PadRight(size);
//                rs += v + " |";
//            }
//            logger.log(logLevel, rs);
//        }
//        logger.log(logLevel, "------------------------------------------- End");
//    }
}

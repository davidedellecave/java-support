package ddc.support.jdbc;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ddc.support.util.Chronometer;
import ddc.support.util.LogListener;

public class SqlUtils {
//	private static Logger logger = Logger.getLogger(SqlUtils.class);

	// public static boolean testConnection(Connection connection) throws
	// SQLException {
	// String sql = "SELECT 1";
	// select(connection, sql);
	// return true;
	// }
	//
	// <property name="queryLimitTemplate" value="SELECT $COLUMNS FROM $TABLE
	// LIMIT $MAXROWS"/>
	// <property name="queryLimitTemplate" value="SELECT $COLUMNS FROM $TABLE
	// WHERE WHERE RowNum <= $MAXROWS"/>
	// <property name="queryLimitTemplate" value="SELECT TOP $MAXROWS $COLUMNS
	// FROM $TABLE"/>

	private final static int RS_TYPE = ResultSet.TYPE_FORWARD_ONLY;// .TYPE_SCROLL_INSENSITIVE;
	private final static int RS_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;
	private final static int FETCH_SIZE = 100000;
	private final static int VERBOSE_COUNT = 10;
	private static LogListener logger = null; // new LogConsole(SqlUtils.class);

	public static void setLogListener(LogListener logger) {
		SqlUtils.logger = logger;
	}

	private static void log(String message) {
		if (logger != null) {
			logger.info(message);
		}
	}

	public static Connection createConnection(JdbcConnectionFactory jdbcConnectionFactory)
			throws SQLException, ClassNotFoundException {
		Chronometer chron = new Chronometer();
		String logInfo = "Getting sql connection:[" + jdbcConnectionFactory.getUrl() + "] user:["
				+ jdbcConnectionFactory.getUser() + "] ...";
		log(logInfo);
		Connection c = jdbcConnectionFactory.createConnection();
		String info = JdbcConnectionFactory.getConnectionInfo(c);
		logInfo = "Sql connection created:[" + jdbcConnectionFactory.getUrl() + "] user:["
				+ jdbcConnectionFactory.getUser() + "] elapsed:[" + chron.toString() + "] " + info;
		log(logInfo);
		return c;
	}

	public static void close(Connection connection) throws SQLException {
		if (connection == null) {
			log("Sql connection already closed");
			return;
		}
		String info = JdbcConnectionFactory.getConnectionInfo(connection);
		log("Sql connection closing" + info + " ...");
		JdbcConnectionFactory.close(connection);
	}

	public static void close(Statement statement) throws SQLException {
		if (statement == null) {
			log("Sql statement already closed");
			return;
		}
		String info = JdbcConnectionFactory.getConnectionInfo(statement.getConnection());
		log("Sql statement closing[" + info + "] ...");
		JdbcConnectionFactory.close(statement);
	}

	public static void dropTable(Connection connection, String table) {
		try {
			String sql = "DROP TABLE " + table;
			execute(connection, sql);
		} catch (Exception e) {
		}
	}

	public static void select(Connection connection, String sql, SqlRowHandler handler) throws SQLException {
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
				if (counter <= VERBOSE_COUNT) {
					printHandler(counter, rs, 40);
					if (counter == VERBOSE_COUNT)
						log("more rows....");
				}
			}
			log("Executed - sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		}
	}

	public static String selectOneField(Connection connection, String sql) throws Exception {
		log("Executing... sql:[" + sql + "]");
		Statement statement = null;
		ResultSet rs = null;
		try {
			Chronometer chron = new Chronometer();
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.setFetchSize(FETCH_SIZE);
			rs = statement.executeQuery(sql);
			// ResultSetMetaData meta = rs.getMetaData();
			String result = null;
			if (rs.next()) {
				result = rs.getString(1);
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

	public static void execute(Connection connection, String sql) throws SQLException {
		log("Executing... catalog:[" + connection.getCatalog() + "] sql:[" + sql + "]");
		Statement statement = null;
		try {
			Chronometer chron = new Chronometer();
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.execute(sql);
			log("Executed - catalog:[" + connection.getCatalog() + "] sql:[" + sql + "] elapsed:[" + chron.toString()
					+ "]");
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
		}
	}

	static public void printHandler(long counter, ResultSet rs, int colSize) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		String message = "";
		if (counter == 1) {
			meta = rs.getMetaData();
			String sep = "";
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				String typeName = JDBCType.valueOf(meta.getColumnType(i)).getName();
				String col = StringUtils.rightPad(meta.getColumnName(i) + "(" + typeName + ")", colSize);
				message += col + " |";
				sep += StringUtils.repeat("=", colSize) + " |";
			}
			log(message);
			log(sep);
		}
		message = "";
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			String v = rs.getString(i);
			if (v == null)
				v = "NULL";
			String col = StringUtils.rightPad(v, colSize);
			message += col + " |";
		}
		log(message);
	}

	public static String getRowInfo(ResultSetMetaData meta) throws SQLException {
		StringBuilder b = new StringBuilder();
		if (StringUtils.isNotBlank(meta.getTableName(1))) {
			b.append("Sql schema - table:[" + meta.getTableName(1) + "]\n");
		} else {
			b.append("Sql schema\n");
		}
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			b.append("\t#:[" + i + "] name:[" + meta.getColumnName(i) + "] type:["
					+ JDBCType.valueOf(meta.getColumnType(i)).getName() + "]");
			b.append('\n');
		}
		return b.toString();
	}

	public static String getRowInfo(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		StringBuilder b = new StringBuilder();
		if (StringUtils.isNotBlank(meta.getTableName(1))) {
			b.append("Sql row - table:[" + meta.getTableName(1) + "]\n");
		} else {
			b.append("Sql row\n");
		}
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			b.append("\t#:[" + i + "] name:[" + meta.getColumnName(i) + "] type:["
					+ JDBCType.valueOf(meta.getColumnType(i)).getName() + "]");
			b.append(" value:[" + String.valueOf(rs.getObject(i)) + "]");
			b.append('\n');
		}
		return b.toString();
	}

	public static void printSqlSelect(Connection connection, String sql, final OutputStream ostream, final int colSize)
			throws Exception {
		printSqlSelect(connection, sql, new PrintStream(ostream), colSize);
	}

	public static void printSqlSelect(Connection connection, String sql, final PrintStream ps, final int colSize)
			throws Exception {
		select(connection, sql, new SqlRowHandler() {
			ResultSetMetaData meta = null;

			@Override
			public void handle(long counter, ResultSet rs) throws SQLException {
				if (counter == 1) {
					meta = rs.getMetaData();
					String sep = "";
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						String typeName = JDBCType.valueOf(meta.getColumnType(i)).getName();
						String col = StringUtils.rightPad(meta.getColumnName(i) + "(" + typeName + ")", colSize);
						ps.print(col + " |");
						sep += StringUtils.repeat("=", colSize) + " |";
					}
					ps.println();
					ps.println(sep);
				}
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String v = rs.getString(i);
					if (v == null)
						v = "NULL";
					String col = StringUtils.rightPad(v, colSize);
					ps.print(col + " |");
				}
				ps.println();
			}
		});
	}

	public static String buildSqlInsert(String table, String autoValue, Map<String, Object> datas, boolean avoidNull) {
		StringBuilder names = new StringBuilder();
		StringBuilder values = new StringBuilder();
		for (Map.Entry<String, Object> e : datas.entrySet()) {
			if (avoidNull && e.getValue() == null)
				continue;
			if (!e.getKey().equals(autoValue)) {
				names.append(e.getKey()).append(",");
				values.append("?").append(",");
			}
		}
		var sNames = StringUtils.removeEnd(names.toString(), ",");
		var sValues = StringUtils.removeEnd(values.toString(), ",");
		return "INSERT INTO " + table + "(" + sNames + ") VALUES ( " + sValues + ")";
	}

	public long insert(Connection connection, String table, String autoValue, Map<String, Object> datas,
			boolean avoidNull) throws SQLException {
		String sql = buildSqlInsert(table, autoValue, datas, avoidNull);
		log("Sql:[" + sql + "]");
		Chronometer chron = new Chronometer();
		int affected = 0;
		int idx = 0;
		try {
			PreparedStatement pstat = connection.prepareStatement(sql);
			for (Map.Entry<String, Object> e : datas.entrySet()) {
				if (avoidNull && e.getValue() == null)
					continue;
				if (!e.getKey().equals(autoValue)) {
					idx++;
					pstat.setObject(idx, e.getValue());
				}
			}
			affected = pstat.executeUpdate();
		} finally {
			close(connection);
		}
		log("insert affected:[" + affected + "] elapsed:[" + chron.toString() + "] sql:[" + sql + "] entity:[" + datas
				+ "]");
		return affected;
	}

	public static String buildSqlUpdate(String table, String pkName, String autoValue, Map<String, Object> datas,
			boolean avoidNull) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(table).append(" SET ");
		for (Map.Entry<String, Object> e : datas.entrySet()) {
			if (avoidNull && e.getValue() == null)
				continue;
			if (e.getKey().equals(autoValue))
				continue;
			if (e.getKey().equals(pkName))
				continue;
			sql.append(e.getKey()).append("=?,");
		}
		var sSql = StringUtils.removeEnd(sql.toString(), ",");
		sSql += " WHERE " + pkName + "=?";
		return sSql;
	}

//    public static String buildOrClause(String fieldName, String[] values) {
//        String clause = "";
//        foreach (String v in values) {
//            clause += " " + fieldName + "='" + v + "' OR";
//        }
//        if (clause.EndsWith("OR"))
//            clause = clause.Substring(0, clause.Length - 2);
//        return "(" + clause + ")";
//    }

//    public static String buildSqlUpdate(String table, Dictionary<String, Object> datas, Dictionary<String, Object> whereAndClause, bool avoidNull) {
//        StringBuilder sql = new StringBuilder();
//        sql.Append("UPDATE ").Append(table).Append(" SET ");
//        foreach (String name in datas.Keys) {
//            if (datas[name] == null && avoidNull) continue;
//            sql.Append(" ").Append(name).Append("=@").Append(name).Append(",");
//        }
//        sql = sql.Remove(sql.Length - 1, 1);
//        sql.Append(" WHERE ");
//        foreach (String name in whereAndClause.Keys) {
//            sql.Append(name).Append("=@").Append(name).Append(" AND ");
//        }
//        return StringUtils.removeLast(sql, " AND ").ToString();
//    }

	public int update(Connection connection, String table, String pkName, String autoValue, Map<String, Object> datas,
			boolean avoidNull) throws SQLException {
		Chronometer chron = new Chronometer();
		int affected = 0;
		int idx = 0;
		String sql = null;
		try {
			sql = buildSqlUpdate(table, pkName, autoValue, datas, avoidNull);
			PreparedStatement pstat = connection.prepareStatement(sql);
			for (Map.Entry<String, Object> e : datas.entrySet()) {
				if (avoidNull && e.getValue() == null)
					continue;
				if (e.getKey().equals(autoValue))
					continue;
				if (e.getKey().equals(pkName))
					continue;
				idx++;
				pstat.setObject(idx, e.getValue());
			}

			idx++;
			pstat.setObject(idx, datas.get(pkName));
			affected = pstat.executeUpdate();
		} finally {
			close(connection);
		}
		log("update affected:[" + affected + "] elapsed:[" + chron.toString() + "] sql:[" + sql + "] entity:[" + datas
				+ "]");
		return affected;
	}

//	public int update(String connectionString, String table, Dictionary<String, Object> datas, Dictionary<String, Object> whereAndClauses, bool avoidNull) {
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

	public static int delete(Connection connection, String tableName, String pkName, Object value) throws SQLException {
		Chronometer chron = new Chronometer();
		int affected = 0;
		String sql = "DELETE FROM " + tableName + " WHERE " + pkName + "=?";
		try {
			PreparedStatement pstat = connection.prepareStatement(sql);
			pstat.setObject(1, value);
			affected = pstat.executeUpdate();
		} finally {
			close(connection);
		}
		log("delete affected:[" + affected + "] elapsed:[" + chron.toString() + "] sql:[" + sql + "] pkName:[" + pkName
				+ "] value:[" + value + "]");
		return affected;

	}
}

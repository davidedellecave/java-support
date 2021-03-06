package ddc.support.jdbc;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

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
	private static LogListener logger = null; //new LogConsole(SqlUtils.class);

	public static void setLogListener(LogListener logger) {
		SqlUtils.logger = logger;
	}
	
	private static void log(String message) {
		if (logger!=null) {
			log(message);
		}
	}
	
	public static Connection createConnection(JdbcConnectionFactory jdbcConnectionFactory) throws SQLException, ClassNotFoundException {
		Chronometer chron = new Chronometer();
		String logInfo = "Getting sql connection:[" + jdbcConnectionFactory.getUrl() + "] user:[" + jdbcConnectionFactory.getUser() + "] ..."; 
		log(logInfo);
		Connection c = jdbcConnectionFactory.createConnection();	
		String info=JdbcConnectionFactory.getConnectionInfo(c);		
		logInfo = "Sql connection created:[" + jdbcConnectionFactory.getUrl() + "] user:[" + jdbcConnectionFactory.getUser() + "] elapsed:[" + chron.toString() + "] " + info;		
		log(logInfo);
		return c;
	}
	
	public static void close(Connection connection) throws SQLException {
		if (connection==null) {
			log("Sql connection already closed");
			return;
		}
		String info=JdbcConnectionFactory.getConnectionInfo(connection);
		log("Sql connection closing" + info + " ..."); 
		JdbcConnectionFactory.close(connection);
	}

	public static void close(Statement statement) throws SQLException {
		if (statement==null) {
			log("Sql statement already closed");
			return;
		}
		String info=JdbcConnectionFactory.getConnectionInfo(statement.getConnection());
		log("Sql statement closing[" + info + "] ..."); 
		JdbcConnectionFactory.close(statement);
	}

	public static void select(Connection connection, String sql, SqlRowHandler handler) throws Exception {
		log("Executing... sql:[" + sql + "]");
		Statement statement = null;
		ResultSet rs = null;
		try {
			Chronometer chron = new Chronometer();
//			statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.FETCH_FORWARD);
			statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
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
			log("Executed - catalog:[" + connection.getCatalog() + "] sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
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
		if (StringUtils.isNotBlank( meta.getTableName(1))) {
			b.append("Sql schema - table:[" + meta.getTableName(1) + "]\n");
		} else {
			b.append("Sql schema\n");
		}
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			b.append("\t#:[" + i + "] name:[" + meta.getColumnName(i) + "] type:[" + JDBCType.valueOf(meta.getColumnType(i)).getName() + "]");
			b.append('\n');
		}
		return b.toString();
	}

	public static String getRowInfo(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		StringBuilder b = new StringBuilder();
		if (StringUtils.isNotBlank( meta.getTableName(1))) {
			b.append("Sql row - table:[" + meta.getTableName(1) + "]\n");
		} else {
			b.append("Sql row\n");
		}
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			b.append("\t#:[" + i + "] name:[" + meta.getColumnName(i) + "] type:[" + JDBCType.valueOf(meta.getColumnType(i)).getName() + "]");
			b.append(" value:[" + String.valueOf(rs.getObject(i)) + "]");
			b.append('\n');
		}
		return b.toString();
	}

	public static void printSqlSelect(Connection connection, String sql, final OutputStream ostream, final int colSize) throws Exception {
		printSqlSelect(connection, sql, new PrintStream(ostream), colSize);
	}

	public static void printSqlSelect(Connection connection, String sql, final PrintStream ps, final int colSize) throws Exception {
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
}

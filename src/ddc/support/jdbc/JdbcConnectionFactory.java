package ddc.support.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ddc.support.jdbc.db.SqlTypeMap;
import ddc.support.jdbc.schema.LiteDbSchemaBuilder;
import ddc.support.util.Chronometer;

public abstract class JdbcConnectionFactory {
	private final static int DEFAULT_CONNECTION_RETRY = 1;
	private final static int DEFAULT_CONNECTION_WAIT = 30 * 1000;
	private boolean driverLoaded = false;
	private JdbcConfig conf;

	public JdbcConnectionFactory(JdbcConfig conf) {
		this.conf = conf;
	}

	public abstract String getUrl();

	public abstract String getDriver();

	public abstract int getDefaultPort();

	public abstract String getSqlLimitTemplate();

	public abstract SqlTypeMap getSqlTypeMap();
	
	public abstract LiteDbSchemaBuilder getSchemaBuilder();

	public String getHost() {
		return conf.getHost();
	}

	public int getPort() {
		return conf.getPort() > 0 ? conf.getPort() : getDefaultPort();
	}

	public String getDatabase() {
		return conf.getDatabase();
	}

	public String getUser() {
		return conf.getUser();
	}

	public String getPassword() {
		return conf.getPassword();
	}

	public String getSqlLimit(String table, String columns, int limit) {
		String s = getSqlLimitTemplate();
		s = s.replace("$TABLE", table);
		s = s.replace("$COLUMNS", columns);
		s = s.replace("$MAXROWS", String.valueOf(limit));
		return s;
	}

	public String getSqlSubqueryLimitTemplate(String table, String subquery, int limit) {
		return getSqlLimit("(" + subquery + ") T", "*", limit);
	}

	public Connection createConnection() throws SQLException, ClassNotFoundException {
		loadDriver();
		Connection c = DriverManager.getConnection(getUrl(), conf.getUser(), conf.getPassword());
		return c;
	}

	public Connection createConnection(int retry) throws SQLException, ClassNotFoundException {
		return createConnection(retry, DEFAULT_CONNECTION_WAIT);
	}

	public Connection createConnection(int retry, long waitMillis) throws SQLException, ClassNotFoundException {
		Chronometer chron = new Chronometer();
		retry = retry > 0 ? retry : DEFAULT_CONNECTION_RETRY;
		waitMillis = waitMillis > 0 ? waitMillis : DEFAULT_CONNECTION_WAIT;
		int counter = 0;
		Throwable exception = null;
		while (counter < retry) {
			try {
				if (counter > 0)
					Chronometer.sleep(waitMillis);
				chron.start();
				Connection conn = createConnection();
				return conn;
			} catch (Throwable e) {
				counter++;
				System.err.println("Connection failed - connection:[" + this.toString() + "] \n\t elapsed:[" + chron.toString() + "] \n\t exception:[" + e.getMessage() + "] \n\t retry... " + counter + "/" + retry);
				exception = e;
			}
		}
		throw new SQLException("Cannection cannot be established after " + retry + " retries - connection:[" + this.toString() + "]", exception);
	}

	public void loadDriver() throws ClassNotFoundException {
		if (driverLoaded)
			return;
		Class.forName(getDriver());
		driverLoaded = true;
	}

	public static String getConnectionInfo(Connection connection) throws SQLException {
		String info = "- Connection ";
		if (connection == null) {
			info += "is null";
			return info;
		}
		try {
			if (connection.getMetaData() != null) {
				if (connection.getMetaData().getDatabaseProductName() != null && connection.getMetaData().getDatabaseProductVersion() != null) {
					info += " Product:[" + connection.getMetaData().getDatabaseProductName() + " " + connection.getMetaData().getDatabaseProductVersion() + "]";
					info += " Driver:[" + connection.getMetaData().getDriverName() + " " + connection.getMetaData().getDriverVersion() + "]";
				}
			}
		} catch(Throwable t) { }
		try {
			if (connection.getCatalog() != null) {
				info += " Catalog:[" + connection.getCatalog() + "]";
			}
		} catch(Throwable t) { }
		try {
			if (connection.getSchema() != null) {
				info += " Schema:[" + connection.getSchema() + "]";
			}
		} catch(Throwable t) { }
		return info;
	}

	public static void close(PreparedStatement statement) {
		if (statement != null) {
			try {
				if (!statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				if (!statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					if (!connection.getAutoCommit()) 
						connection.commit();
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		if (conf != null)
			return conf.toString();
		return super.toString();
	}
}

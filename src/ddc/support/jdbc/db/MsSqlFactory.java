package ddc.support.jdbc.db;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.JdbcConnectionFactory;

public class MsSqlFactory extends JdbcConnectionFactory {

	public MsSqlFactory(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
//		jdbc:sqlserver://[serverName[\instanceName][:portNumber]][;property=value[;property=value]]
		return "jdbc:sqlserver://" + getHost() + ":" + getPort() + ";databaseName=" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	@Override
	public int getDefaultPort() {
		return 1433;
	}

	@Override
	public String getSqlLimitTemplate() {
		// TODO Auto-generated method stub
		return "SELECT TOP $MAXROWS $COLUMNS FROM $TABLE";
	}


	@Override
	public SqlTypeMap getSqlTypeMap() {
		return null;
	}

//	@Override
//	public void testConnection(Connection connection) throws SQLException {
//		String sql = "SELECT 1";
//		ConnectionUtils.select(connection, sql);
//		
//	}

}

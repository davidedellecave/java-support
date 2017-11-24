package ddc.support.jdbc.db;

import java.sql.JDBCType;
import java.util.Map;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.JdbcConnectionFactory;

public class MsSqlFactory extends JdbcConnectionFactory {

	public MsSqlFactory(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
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
		return null;
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

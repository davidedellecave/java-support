package ddc.support.jdbc.db;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.JdbcConnectionFactory;
import ddc.support.jdbc.schema.LiteDbSchemaBuilder;

public class MsSqlFactory extends JdbcConnectionFactory {

	public MsSqlFactory(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
		String url ="jdbc:sqlserver://" + getHost() + ":" + getPort() +  ";databaseName=" + getDatabase();
		url += ";encrypt=true;trustServerCertificate=true";
		url += ";integratedSecurity=false";
//		url += ";CryptoProtocolVersion=TLSv1.2";
//		TLS, TLSv1, TLSv1.1, and TLSv1.2
		return url;
	}

//	jdbc:sqlserver://5.134.124.246:1433;databaseName=davidedc_gottardo;;encrypt=true;trustServerCertificate=true;
//	jdbc:sqlserver://5.134.124.246:1433;databaseName=davidedc_gottardo;encrypt=true;trustServerCertificate=true;integratedSecurity=false		
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
	
	@Override
	public LiteDbSchemaBuilder getSchemaBuilder() {
		return null;
	}

//	@Override
//	public void testConnection(Connection connection) throws SQLException {
//		String sql = "SELECT 1";
//		ConnectionUtils.select(connection, sql);
//		
//	}

}

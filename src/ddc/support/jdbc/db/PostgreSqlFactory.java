package ddc.support.jdbc.db;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.JdbcConnectionFactory;

public class PostgreSqlFactory extends JdbcConnectionFactory {

	public PostgreSqlFactory(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
		return "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "org.postgresql.Driver";
	}

	@Override
	public int getDefaultPort() {
		return 5432;
	}

	@Override
	public String getSqlLimitTemplate() {
		return "SELECT $COLUMNS FROM $TABLE LIMIT $MAXROWS";
	}


	private static SqlTypeMap map = new PostreSqlTypeMap();

	@Override
	public SqlTypeMap getSqlTypeMap() {
		return map;
	}
}

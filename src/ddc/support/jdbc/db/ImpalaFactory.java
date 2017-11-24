package ddc.support.jdbc.db;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.JdbcConnectionFactory;

public class ImpalaFactory extends JdbcConnectionFactory {

	public ImpalaFactory(JdbcConfig conf) {
		super(conf);
	}
	
	@Override
	public String getUrl() {
		return "jdbc:impala://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "com.cloudera.impala.jdbc4.Driver";
	}

	@Override
	public int getDefaultPort() {
		return 21050;
	}

	@Override
	public String getSqlLimitTemplate() {
		return "SELECT $COLUMNS FROM $TABLE LIMIT $MAXROWS";
	}

	private static ImpalaSqlTypeMap map = new ImpalaSqlTypeMap();

	@Override
	public SqlTypeMap getSqlTypeMap() {
		return map;
	}
	

}

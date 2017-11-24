package ddc.support.jdbc.db;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.JdbcConnectionFactory;

public class MySqlFactory  extends JdbcConnectionFactory {

	public MySqlFactory(JdbcConfig conf) {
		super(conf);
	}
		    
	@Override
	public String getUrl() {
		return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public int getDefaultPort() {
		return 3306;
	}

	@Override
	public String getSqlLimitTemplate() {
		return  "SELECT $COLUMNS FROM $TABLE LIMIT $MAXROWS";
	}


	private static SqlTypeMap map = new MySqlTypeMap();

	@Override
	public SqlTypeMap getSqlTypeMap() {
		return map;
	}

}

package ddc.support.jdbc.db;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.JdbcConnectionFactory;
import ddc.support.jdbc.schema.LiteDbSchemaBuilder;

public class VerticaDbFactory extends JdbcConnectionFactory {

	public VerticaDbFactory(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
		return "jdbc:vertica://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "com.vertica.jdbc.Driver";
	}

	@Override
	public int getDefaultPort() {
		return 5433;
	}

	@Override
	public String getSqlLimitTemplate() {
		return "SELECT $COLUMNS FROM $TABLE LIMIT $MAXROWS";
	}

	public static void main(String[] args) {
		JdbcConfig c = new JdbcConfig();
		c.setHost("");
		c.setPort(5433);
		c.setDatabase("");
		c.setUser("");
		c.setPassword("");
		VerticaDbFactory f = new VerticaDbFactory(c);
		System.out.println(f.getUrl());

		// {
		// "type": "jdbc",
		// "driver": "com.mysql.jdbc.Driver",
		// "url": "jdbc:mysql://localhost:3306",
		// "username": "root",
		// "password": "mypassword",
		// "enabled": true
		// }
	}

	private static VerticaSqlTypeMap map = new VerticaSqlTypeMap();

	@Override
	public SqlTypeMap getSqlTypeMap() {
		return map;
	}
	
	@Override
	public LiteDbSchemaBuilder getSchemaBuilder() {
		return null;
	}
}

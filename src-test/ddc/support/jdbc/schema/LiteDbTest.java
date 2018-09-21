package ddc.support.jdbc.schema;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.db.PostgreSqlFactory;

public class LiteDbTest {

	@Test
	public void testBuild() throws ClassNotFoundException, SQLException {		
		JdbcConfig conf = new JdbcConfig();
		conf.setHost("localhost");
		conf.setDatabase("davide");
		conf.setUser("davide");
		conf.setPassword("davidedc");	
		
//		conf.setPort(1414);
		PostgreSqlFactory f = new PostgreSqlFactory(conf);
		Connection conn = f.createConnection();
		LiteDb db = LiteDb.build(f);
		
		System.out.println(db);
	}

}

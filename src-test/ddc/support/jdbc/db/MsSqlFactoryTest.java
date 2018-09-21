package ddc.support.jdbc.db;

import java.sql.Connection;
import java.sql.ResultSet;

import org.junit.Test;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.SqlRowHandler;
import ddc.support.jdbc.SqlUtils;

public class MsSqlFactoryTest {

	@Test
	public void testCreateConnection() throws Exception {
		JdbcConfig conf = new JdbcConfig();
		conf.setHost("");
		conf.setDatabase("");
		conf.setUser("");
		conf.setPassword("");
		
//		conf.setPort(1414);
		MsSqlFactory f = new MsSqlFactory(conf);
		Connection conn = f.createConnection();
	
		
//		SqlUtils.printSqlSelect(conn, "SELECT * FROM RET_movimenti LIMIT 1000000", System.out, 40);
		String sql = "SELECT * FROM CONF LIMIT 1000";
//		String sql = "SELECT * FROM RET_movimenti";
		conn.setAutoCommit(false);
		SqlUtils.select(conn, sql, new SqlRowHandler() {			
			@Override
			public void handle(long counter, ResultSet rs) throws Exception {
				// TODO Auto-generated method stub
				
			}
		});
	}

}

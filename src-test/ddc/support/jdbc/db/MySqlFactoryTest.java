package ddc.support.jdbc.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

import ddc.support.jdbc.SqlRowHandler;
import ddc.support.util.Chronometer;
import ddc.support.util.LogConsole;
import ddc.support.util.LogListener;

public class MySqlFactoryTest {
	private final static LogListener logger = new LogConsole(MySqlFactoryTest.class);
	@Test
	public void testGetSqlLimitTemplate() throws Exception {
//		JdbcConfig conf = new JdbcConfig();
//		conf.setDatabase("dataquality");
//		conf.setHost("192.168.76.224");
//		conf.setUser("root");
//		conf.setPassword("Davidedc3");
//		MySqlFactory f = new MySqlFactory(conf);
//		Connection conn = f.createConnection();
//	
//		
////		SqlUtils.printSqlSelect(conn, "SELECT * FROM RET_movimenti LIMIT 1000000", System.out, 40);
//		String sql = "SELECT * FROM RET_movimenti LIMIT 5000000";
////		String sql = "SELECT * FROM RET_movimenti";
//		conn.setAutoCommit(false);
//		select(conn, sql, new SqlRowHandler() {			
//			@Override
//			public void handle(long counter, ResultSet rs) throws Exception {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}

	private static int FETCH_SIZE = 10;
	private static int RS_TYPE = ResultSet.TYPE_FORWARD_ONLY;
	private static int RS_CONCURRENCY = ResultSet.CONCUR_READ_ONLY;
	
	
	public static void select(Connection connection, String sql, SqlRowHandler handler) throws Exception {
		logger.debug("Executing... sql:[" + sql + "]");
		Statement statement = null;
		ResultSet rs = null;
		try {
			Chronometer chron = new Chronometer();
			
			statement = connection.createStatement(RS_TYPE, RS_CONCURRENCY);
			statement.setFetchSize(FETCH_SIZE);
			rs = statement.executeQuery(sql);
			long counter = 0;
			while (rs.next()) {
				counter++;
				handler.handle(counter, rs);
			}
			logger.info("Executed - sql:[" + sql + "] elapsed:[" + chron.toString() + "]");
		} finally {
			if (statement != null && !statement.isClosed())
				statement.close();
			if (rs != null && !rs.isClosed())
				rs.close();
		}
	}
}

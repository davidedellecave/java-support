package ddc.support.jdbc.db;

import ddc.support.jdbc.JdbcConfig;
import ddc.support.jdbc.JdbcConnectionFactory;
import ddc.support.jdbc.schema.LiteDbSchemaBuilder;

//IBM DB2 Universal Driver Type 4 
//
//--------------------------------------------------------------------------------
//
//DRIVER CLASS: com.ibm.db2.jcc.DB2Driver 
//
//DRIVER LOCATION: db2jcc.jar and db2jcc_license_cu.jar 
//(Both of these jars must be included) 
//
//JDBC URL FORMAT: jdbc:db2://<host>[:<port>]/<database_name> 
//
//JDBC URL Examples: 
//
//jdbc:db2://127.0.0.1:50000/SAMPLE 

public class Db2Factory extends JdbcConnectionFactory {

	public Db2Factory(JdbcConfig conf) {
		super(conf);
	}

	@Override
	public String getUrl() {
		return "jdbc:db2://" + getHost() + ":" + getPort() + "/" + getDatabase();
	}

	@Override
	public String getDriver() {
		return "com.ibm.db2.jcc.DB2Driver";
	}

	@Override
	public int getDefaultPort() {
		return 3003;
	}

	@Override
	public String getSqlLimitTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SqlTypeMap getSqlTypeMap() {
		return new Db2SqlTypeMap();
	}

	@Override
	public LiteDbSchemaBuilder getSchemaBuilder() {
		return new Db2SchemaBuilder();
	}
	
//	Java data type 	Database data type
//	short, java.lang.Short 									SMALLINT
//	boolean1, byte1, java.lang.Boolean, java.lang.Byte 		SMALLINT
//	int, java.lang.Integer 									INTEGER
//	long, java.lang.Long 									BIGINT12
//	java.math.BigInteger 									BIGINT11
//	java.math.BigInteger 									CHAR(n)11,5
//	float, java.lang.Float 									REAL
//	double, java.lang.Double 								DOUBLE
//	java.math.BigDecimal 									DECIMAL(p,s)2
//	java.math.BigDecimal 									DECFLOAT(n)3,4
//	java.lang.String 										CHAR(n)5
//	java.lang.String 										GRAPHIC(m)6
//	java.lang.String 										VARCHAR(n)7
//	java.lang.String 										VARGRAPHIC(m)8
//	java.lang.String 										CLOB9
//	java.lang.String 										XML10
//	byte[] 	CHAR(n) FOR BIT 								DATA5
//	byte[] 	VARCHAR(n) FOR BIT 								DATA7
//	byte[] 	BINARY(n)5, 									13
//	byte[] 	VARBINARY(n)7, 									13
//	byte[] 													BLOB9
//	byte[] 													ROWID
//	byte[] 													XML10
//	java.sql.Blob 											BLOB
//	java.sql.Blob 											XML10
//	java.sql.Clob 											CLOB
//	java.sql.Clob 											DBCLOB9
//	java.sql.Clob 											XML10
//	java.sql.Date 											DATE
//	java.sql.Time 											TIME
//	java.sql.Timestamp 										TIMESTAMP, TIMESTAMP(p), TIMESTAMP WITH TIME ZONE, TIMESTAMP(p) WITH TIME ZONE14,15
//	java.io.ByteArrayInputStream 							BLOB
//	java.io.StringReader 									CLOB
//	java.io.ByteArrayInputStream 							CLOB
//	java.io.InputStream 									XML10
//	com.ibm.db2.jcc.DB2RowID (deprecated) 					ROWID
//	java.sql.RowId 											ROWID
//	com.ibm.db2.jcc.DB2Xml (deprecated) 					XML10
//	java.sql.SQLXML 										XML10
//	java.util.Date 											CHAR(n)11,5
//	java.util.Date 											VARCHAR(n)11,5
//	java.util.Date 											DATE11
//	java.util.Date 											TIME11
//	java.util.Date 											TIMESTAMP, TIMESTAMP(p), TIMESTAMP WITH TIME ZONE, TIMESTAMP(p) WITH TIME ZONE11,14,15
//	java.util.Calendar 										CHAR(n)11,5
//	java.util.Calendar 										VARCHAR(n)11,5
//	java.util.Calendar 										DATE11
//	java.util.Calendar 										TIME11
//	java.util.Calendar										TIMESTAMP, TIMESTAMP(p), TIMESTAMP WITH TIME ZONE, TIMESTAMP(p) WITH TIME ZONE11,14,15

//	@Override
//	public void checkConnection(Connection connection) throws SQLException {
//		String sql = "SELECT * FROM SYSIBM.SYSTABLES fetch first 5 rows only" ;
//		ResultSet table = SqlConnectionUtils.select(connection, sql);
//		SqlConnectionUtils.printTable(table);
//	}
}

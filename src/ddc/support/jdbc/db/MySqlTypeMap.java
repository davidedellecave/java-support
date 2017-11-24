package ddc.support.jdbc.db;

import java.sql.JDBCType;

public class MySqlTypeMap  extends SqlTypeMap {
	private static final long serialVersionUID = -7066765127367971041L;

	public MySqlTypeMap() {
		put(JDBCType.ARRAY, "_ARRAY_");
		put(JDBCType.BIGINT, "BIGINT");
		put(JDBCType.BINARY, "BINARY");
		put(JDBCType.BIT, "BOOLEAN");
		put(JDBCType.BLOB, "VARBINARY");
		put(JDBCType.BOOLEAN, "BOOLEAN");
		put(JDBCType.CHAR, "CHAR");
		put(JDBCType.CLOB, "LONG VARCHAR");
		put(JDBCType.DATALINK, "_DATALINK_");
		put(JDBCType.DATE, "DATE");
		put(JDBCType.DECIMAL, "DECIMAL");
		put(JDBCType.DISTINCT, "_DISTINCT_");
		put(JDBCType.DOUBLE,  "DOUBLE PRECISION");
		put(JDBCType.FLOAT,  "FLOAT");
		put(JDBCType.INTEGER, "INTEGER");
		put(JDBCType.JAVA_OBJECT,  "VARBINARY");
		put(JDBCType.LONGNVARCHAR,  "LONG VARCHAR");
		put(JDBCType.LONGVARBINARY,  "VARBINARY");
		put(JDBCType.NCHAR,  "CHAR");
		put(JDBCType.NCLOB,  "LONG VARCHAR");
		put(JDBCType.NULL,  "_NULL_");
		put(JDBCType.NUMERIC,  "NUMERIC");
		put(JDBCType.NVARCHAR,  "VARCHAR");
		put(JDBCType.OTHER,  "_OTHER_");
		put(JDBCType.REAL,  "REAL");
		put(JDBCType.REF,  "_REF_");
		put(JDBCType.REF_CURSOR, "_REF_CURSOR_");
		put(JDBCType.ROWID, "_ROWID_");
		put(JDBCType.SMALLINT,"SMALLINT");
		put(JDBCType.SQLXML, "VARCHAR");
		put(JDBCType.STRUCT, "_STRUCT_");
		put(JDBCType.TIME, "TIMESTAMP");
		put(JDBCType.TIMESTAMP, "TIMESTAMP");
		put(JDBCType.TIME_WITH_TIMEZONE, "TIMESTAMPTZ");
		put(JDBCType.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMPTZ");
		put(JDBCType.TINYINT,"TINYINT");
		put(JDBCType.VARBINARY, "VARBINARY");
		put(JDBCType.VARCHAR, "VARCHAR");
	}
}

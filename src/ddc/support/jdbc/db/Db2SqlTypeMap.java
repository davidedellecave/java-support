package ddc.support.jdbc.db;

import java.sql.JDBCType;

public class Db2SqlTypeMap extends SqlTypeMap {
	private static final long serialVersionUID = -4276667222075611327L;

	public Db2SqlTypeMap() {
		put(JDBCType.ARRAY, "_ARRAY_");
		put(JDBCType.BIGINT, "BIGINT");
		put(JDBCType.BINARY, "CHAR");
		put(JDBCType.BIT, "_BIT_");
		put(JDBCType.BLOB, "BLOB");
		put(JDBCType.BOOLEAN, "_BOOLEAN_");
		put(JDBCType.CHAR, "CHAR");
		put(JDBCType.CLOB, "CLOB");
		put(JDBCType.DATALINK, "_DATALINK_");
		put(JDBCType.DATE, "DATE");
		put(JDBCType.DECIMAL, "DECIMAL");
		put(JDBCType.DISTINCT, "_DISTINCT_");
		put(JDBCType.DOUBLE, "DOUBLE");
		put(JDBCType.FLOAT, "FLOAT");
		put(JDBCType.INTEGER, "INTEGER");
		put(JDBCType.JAVA_OBJECT, "_JAVA_OBJECT_");
		put(JDBCType.LONGNVARCHAR, "LONG VARCHAR");
		put(JDBCType.LONGVARBINARY, "LONG VARCHAR");
		put(JDBCType.NCHAR, "CHAR");
		put(JDBCType.NCLOB, "CLOB");
		put(JDBCType.NULL, "_NULL_");
		put(JDBCType.NUMERIC, "NUMERIC");
		put(JDBCType.NVARCHAR, "VARCHAR");
		put(JDBCType.OTHER, "_OTHER_");
		put(JDBCType.REAL, "REAL");
		put(JDBCType.REF, "_REF_");
		put(JDBCType.REF_CURSOR, "_REF_CURSOR_");
		put(JDBCType.ROWID, "_ROWID_");
		put(JDBCType.SMALLINT, "SMALLINT");
		put(JDBCType.SQLXML, "VARCHAR");
		put(JDBCType.STRUCT, "_STRUCT_");
		put(JDBCType.TIME, "TIME");
		put(JDBCType.TIMESTAMP, "TIMESTMP"); //TIMESTMP without 'A' is right!!
		put(JDBCType.TIME_WITH_TIMEZONE, "TIMESTAMPTZ");
		put(JDBCType.TIMESTAMP_WITH_TIMEZONE, "_TIMESTAMP_WITH_TIMEZONE_");
		put(JDBCType.TINYINT, "_TINYINT_");
		put(JDBCType.VARBINARY, "VARBINARY");
		put(JDBCType.VARCHAR, "VARCHAR");
	}

}

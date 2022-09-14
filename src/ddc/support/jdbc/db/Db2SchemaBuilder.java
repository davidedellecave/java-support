package ddc.support.jdbc.db;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;

import ddc.support.jdbc.SqlRowHandler;
import ddc.support.jdbc.SqlUtils;
import ddc.support.jdbc.schema.LiteDb;
import ddc.support.jdbc.schema.LiteDbCatalog;
import ddc.support.jdbc.schema.LiteDbColumn;
import ddc.support.jdbc.schema.LiteDbSchema;
import ddc.support.jdbc.schema.LiteDbSchemaBuilder;
import ddc.support.jdbc.schema.LiteDbTable;

public class Db2SchemaBuilder implements LiteDbSchemaBuilder {
	private LiteDb db = null;

	@Override
	public void build(LiteDb db, Connection connection, String schema, String table) throws Exception {
		this.db = db;
		doBuild2(connection, schema, table);
	}

	private String buildSql2(String schema, String table) {
		// String sql = "SELECT * FROM SYSIBM.SYSCOLUMNS WHERE TBNAME='$TABLE' AND
		// TBCREATOR='$SCHEMA'".replace("$TABLE", tableName).replace("$SCHEMA",
		// schemaName);
		// SELECT * FROM SYSIBM.SYSCOLUMNS";
		String sql = "SELECT * FROM SYSIBM.SYSCOLUMNS";
		if (schema != null || table != null) {
			sql += " WHERE ";
		}
		if (schema != null) {
			sql += " TBCREATOR='$SCHEMA'".replace("$SCHEMA", schema);
		}
		if (table != null) {
			if (schema != null) {
				sql += " AND ";
			}
			sql += " TBNAME='$TABLE'".replace("$TABLE", table);
		}
		return sql;
	}

	private void doBuild2(Connection connection, String schema, String table) throws SQLException {
		// SELECT * FROM SYSIBM.SYSTABLES WHERE DBNAME='LIFEUNIV';
		String sql = buildSql2(schema, table);
		SqlUtils.select(connection, sql, new SqlRowHandler() {
			@Override
			public void handle(long counter, ResultSet rs) throws SQLException {
				String catalogName = rs.getString("TBCREATOR");
				String schemaName = rs.getString("TBCREATOR");
				String tableName = rs.getString("TBNAME");

				LiteDbCatalog c = db.findCatalog(catalogName);
				LiteDbSchema s = db.findSchema(catalogName, schemaName);
				LiteDbTable t = db.findTable(catalogName, schemaName, tableName);
				if (c == null) {
					c = new LiteDbCatalog(catalogName, db);
					s = new LiteDbSchema(schemaName, c);
					t = new LiteDbTable(tableName, s);
					db.getCatalogs().add(c);
					c.getSchemas().add(s);
					s.getTables().add(t);
				} else if (s == null) {
					s = new LiteDbSchema(schemaName, c);
					t = new LiteDbTable(tableName, s);
					c.getSchemas().add(s);
					s.getTables().add(t);
				} else if (t == null) {
					t = new LiteDbTable(tableName, s);
					s.getTables().add(t);
				}
				t.getColumns().add(builCol(rs));
			}
		});
	}

//	private String buildSql(String schema, String table) {
//		// SELECT * FROM SYSIBM.SYSTABLES WHERE DBNAME='LIFEUNIV';
//		String sql = "SELECT * FROM SYSIBM.SYSTABLES";
//		if (schema != null || table != null) {
//			sql += " WHERE ";
//		}
//		if (schema != null) {
//			sql += " CREATOR='$SCHEMA'".replace("$SCHEMA", schema);
//		}
//		if (table != null) {
//			if (schema != null) {
//				sql += " AND ";
//			}
//			sql += " NAME='$NAME'".replace("$NAME", table);
//		}
//		return sql;
//	}
//
//	private void doBuild(Connection connection, String schema, String table) throws Exception {
//		// SELECT * FROM SYSIBM.SYSTABLES WHERE DBNAME='LIFEUNIV';
//		String sql = buildSql(schema, table);
//		SqlUtils.select(connection, sql, new SqlRowHandler() {
//			@Override
//			public void handle(long counter, ResultSet rs) throws Exception {
//				String catalogName = rs.getString("CREATOR");
//				String schemaName = rs.getString("DBNAME");
//				String tableName = rs.getString("NAME");
//
//				LiteDbCatalog c = db.findCatalog(catalogName);
//				LiteDbSchema s = db.findSchema(catalogName, schemaName);
//				LiteDbTable t = db.findTable(catalogName, schemaName, tableName);
//				if (c == null) {
//					c = new LiteDbCatalog(catalogName, db);
//					s = new LiteDbSchema(schemaName, c);
//					t = new LiteDbTable(tableName, s);
//					db.getCatalogs().add(c);
//					c.getSchemas().add(s);
//					s.getTables().add(t);
//				} else if (s == null) {
//					s = new LiteDbSchema(schemaName, c);
//					t = new LiteDbTable(tableName, s);
//					c.getSchemas().add(s);
//					s.getTables().add(t);
//				} else if (t == null) {
//					t = new LiteDbTable(tableName, s);
//					s.getTables().add(t);
//				}
//			}
//		});
//		for (LiteDbTable t : db.getAllTables()) {
//			t.setTableType("T");
//			buildTable(connection, t);
//		}
//	}

//	private LiteDbTable buildTable(Connection connection, LiteDbTable table) throws Exception {
//		String tableName = table.getTableName();
//		String schemaName = table.getSchema().getSchemaName();
//		String sql = "SELECT * FROM SYSIBM.SYSCOLUMNS WHERE TBNAME='$TABLE' AND TBCREATOR='$SCHEMA'".replace("$TABLE", tableName).replace("$SCHEMA", schemaName);
//		LiteDbColumns cols = new LiteDbColumns();
//		table.setColumns(cols);
//		SqlUtils.select(connection, sql, new SqlRowHandler() {
//			@Override
//			public void handle(long counter, ResultSet rs) throws Exception {
//				cols.add(builCol(rs));
//			}
//		});
//		return table;
//	}

	private LiteDbColumn builCol(ResultSet rs) throws SQLException {
		String columnName = rs.getString("NAME").trim();
		String dataType = rs.getString("COLTYPE").trim();
		JDBCType jdbcType = (new Db2SqlTypeMap()).getJDBCType(dataType);
		boolean isNullable = !rs.getString("NULLS").trim().equalsIgnoreCase("N");
		int columnSize = rs.getInt("LENGTH");
		int colCounter = rs.getInt("COLNO");
		LiteDbColumn col = new LiteDbColumn(columnName, jdbcType, isNullable, columnSize, colCounter);
		col.setScale(rs.getInt("SCALE"));
		return col;
	}

}

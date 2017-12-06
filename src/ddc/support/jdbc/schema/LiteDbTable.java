package ddc.support.jdbc.schema;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class LiteDbTable {
	private LiteDbSchema schema = new LiteDbSchema();
	private String tableName = "";
	private String tableType = "";
	private LiteDbColumns columns = new LiteDbColumns();

	public LiteDbTable(String tableName, LiteDbSchema schema) {
		this.tableName = tableName;
		this.schema = schema;
	}

	public LiteDbTable() {
	}

	public static LiteDbTable build(Connection sqlConnection, String table, String sql) throws SQLException {
		try (Statement sqlStatement = sqlConnection.createStatement();) {
			ResultSet rs = sqlStatement.executeQuery(sql);
			return build(table, rs.getMetaData());
		}
	}

	public static LiteDbTable build(String table, ResultSetMetaData meta) throws SQLException {
		LiteDbTable s = new LiteDbTable();
		s.setTableName(table);
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			LiteDbColumn c = new LiteDbColumn();
			c.setName(meta.getColumnName(i));
			c.setIndex(i);
			c.setNullable(meta.isNullable(i) == ResultSetMetaData.columnNullable);
			c.setSize(meta.getPrecision(i));
			c.setScale(meta.getScale(i));
			c.setType(JDBCType.valueOf(meta.getColumnType(i)));
			s.getColumns().add(c);
		}
		return s;
	}

	public static Map<JDBCType, String> getDefaultTypeMap() {
		return defaultTypeMap;
	}

	public static void setDefaultTypeMap(Map<JDBCType, String> defaultTypeMap) {
		LiteDbTable.defaultTypeMap = defaultTypeMap;
	}

	public LiteDbSchema getSchema() {
		return schema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public LiteDbColumns getColumns() {
		return columns;
	}

	public void setColumns(LiteDbColumns columns) {
		this.columns = columns;
	}

	public LiteDbColumn getColumn(String name) {
		for (LiteDbColumn c : columns) {
			if (c.getName().equals(name))
				return c;
		}
		return null;
	}

	public boolean addColumn(LiteDbColumn column) {
		return columns.add(column);
	}

	public boolean addColumn(String name, JDBCType type, boolean isNullable) {
		return columns.add(new LiteDbColumn(name, type, isNullable));
	}

	public LiteDbColumn getColumn(int index) {
		for (LiteDbColumn c : columns) {
			if (c.getIndex() == index)
				return c;
		}
		return null;
	}

	private static Map<JDBCType, String> defaultTypeMap = new TreeMap<>();
	static {
		for (JDBCType t : JDBCType.values()) {
			defaultTypeMap.put(t, t.getName());
		}
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Database:[" + schema.getCatalog().getDatabase().getDatabaseName() + "] ");
		b.append("Catalog:[" + schema.getCatalog().getCatalogName() + "] ");
		b.append("Schema:[" + schema.getSchemaName() + "] ");
		b.append("Table:[" + tableName + "] ");
		b.append("Type:[" + tableType + "] ");
		b.append("\n");
		for (LiteDbColumn c : columns) {
			b.append("\t" + c.getIndex() + ") " + c.getName() + " " + c.getType().toString() + "(" + c.getSize() + "," + c.getScale() + ")");
			if (c.isNullable()) {
				b.append(" NULLABLE");
			}
			b.append("\n");
		}
		return b.toString();
	}

	public String buildSelect() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT " + StringUtils.join(columns, ','));
		sql.append(" FROM " + tableName);
		return sql.toString();
	}

	public String buildSelect(String appendClause) {
		return buildSelect() + " " + appendClause;
	}

	public String buildCreateTable() throws SQLException {
		return buildCreateTable(defaultTypeMap);
	}

	public String buildCreateTable(Map<JDBCType, String> typeMap) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + tableName + " (\n");
		for (LiteDbColumn c : getColumns()) {
			sql.append("\t\"" + c.getName() + "\"");
			String typeName = typeMap.get(c.getType());
			if (typeName == null) {
				throw new SQLException("Sql type is not mapped - type:[" + c.getType() + "]");
			}
			sql.append(" " + typeName);
			if (c.getType().equals(JDBCType.CHAR) || c.getType().equals(JDBCType.LONGNVARCHAR) || c.getType().equals(JDBCType.LONGVARCHAR) || c.getType().equals(JDBCType.NCHAR) || c.getType().equals(JDBCType.NVARCHAR) || c.getType().equals(JDBCType.VARCHAR)) {
				sql.append(" (" + c.getSize() + ")");
			} else if (c.getType().equals(JDBCType.DECIMAL) || c.getType().equals(JDBCType.BIGINT) || c.getType().equals(JDBCType.NUMERIC)) {
				sql.append(" (" + c.getSize() + "," + c.getScale() + ")");
			}
			if (!c.isNullable()) {
				sql.append(" NOT NULL");
			}
			sql.append(",\n");
		}
		// remove \n
		sql = sql.deleteCharAt(sql.length() - 1);
		// remove last comma
		sql = sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		return sql.toString();
	}

	public String buildInsertInto() {
		String sql = "INSERT INTO " + getTableName() + " (";
		sql += StringUtils.join(getColumns(), ",");
		sql += ")";
		sql += " VALUES (";
		sql += StringUtils.repeat("?,", getColumns().size());
		sql = StringUtils.removeEnd(sql, ",");
		sql += ")";
		return sql;
	}
	
	public String buildInsertInto(String colLeftQuote, String colRightQuote) {
		String sql = "INSERT INTO " + getTableName() + " (";
		sql += colLeftQuote + StringUtils.join(getColumns(), colLeftQuote + "," + colRightQuote) + colRightQuote;
		sql += ")";
		sql += " VALUES (";
		sql += StringUtils.repeat("?,", getColumns().size());
		sql = StringUtils.removeEnd(sql, ",");
		sql += ")";
		return sql;
	}

}

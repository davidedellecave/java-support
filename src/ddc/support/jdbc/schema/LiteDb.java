package ddc.support.jdbc.schema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddc.support.jdbc.JdbcConnectionFactory;

public class LiteDb {
	// TABLE_TYPE String => table type. Typical types are "TABLE",
	// "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
	// "SYNONYM"
	private final static String[] TABLE_TYPES = { "TABLE", "VIEW" };
	private String databaseName = "";
	private List<LiteDbCatalog> catalogs = new ArrayList<LiteDbCatalog>();

	private LiteDb(String databaseName) {
		this.databaseName = databaseName;
	}

	public static LiteDb build(JdbcConnectionFactory connectionFactory) throws SQLException, ClassNotFoundException {
		try (Connection conn = connectionFactory.createConnection()) {
			return build(connectionFactory.getDatabase(), conn);
		}
	}

	private static LiteDb build(String databaseName, Connection connection) throws SQLException {
		LiteDb liteDb = new LiteDb(databaseName);
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rsCatalogs = meta.getCatalogs();
		while (rsCatalogs.next()) {
			String catalogName = rsCatalogs.getString("TABLE_CAT");
			LiteDbCatalog liteCatalog = new LiteDbCatalog(catalogName, liteDb);
			liteDb.getCatalogs().add(liteCatalog);
			//
			ResultSet rsSchema = meta.getSchemas();
			while (rsSchema.next()) {
				String schemaName = rsSchema.getString("TABLE_SCHEM");
				LiteDbSchema liteSchema = new LiteDbSchema(schemaName, liteCatalog);
				liteCatalog.getSchemas().add(liteSchema);
				//
				ResultSet rsTable = meta.getTables(catalogName, schemaName, null, TABLE_TYPES);
				while (rsTable.next()) {
					String tableName = rsTable.getString("TABLE_NAME");
					String tableType = rsTable.getString("TABLE_TYPE");
					LiteDbTable liteTable = new LiteDbTable(tableName, liteSchema);
					liteTable.setTableType(tableType);
					liteSchema.getTables().add(liteTable);
					//
					ResultSet rsColumn = meta.getColumns(catalogName, schemaName, tableName, null);
					int colCounter = 0;
					while (rsColumn.next()) {
						colCounter++;
						String columnName = rsColumn.getString("COLUMN_NAME");
						int dataType = rsColumn.getShort("DATA_TYPE");
						JDBCType jdbcType = JDBCType.valueOf(dataType);
						boolean isNullable = rsColumn.getInt("NULLABLE") == 1;
						int columnSize = rsColumn.getInt("COLUMN_SIZE");
						LiteDbColumn c = new LiteDbColumn(columnName, jdbcType, isNullable, columnSize, colCounter);
						c.setScale(rsColumn.getInt("DECIMAL_DIGITS"));
						liteTable.getColumns().add(c);
					}

					ResultSet rsPK = meta.getPrimaryKeys(catalogName, schemaName, tableName);
					while (rsPK.next()) {
						String colPKName = rsPK.getString("COLUMN_NAME");
						liteTable.addPKColumn(colPKName);
					}
				}
			}
		}
		return liteDb;
	}

	public List<LiteDbTable> findByCol(String name) {
		name=name.toLowerCase();
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				for (LiteDbTable t : s.getTables()) {
					for (LiteDbColumn cl : t.getColumns()) {
						if (cl.getName().toLowerCase().contains(name)) {
							list.add(t);
							break;
						}
					}
				}
			}
		}
		return list;
	}

	public List<LiteDbTable> findByTable(String name) {
		name=name.toLowerCase();
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				for (LiteDbTable t : s.getTables()) {
					if (t.getTableName().toLowerCase().contains(name))
						list.add(t);
				}
			}
		}
		return list;
	}

	public List<LiteDbTable> findBySchema(String name) {
		name=name.toLowerCase();
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				if (s.getSchemaName().toLowerCase().contains(name)) {
					for (LiteDbTable t : s.getTables()) {
						list.add(t);
					}
				}
			}
		}
		return list;
	}

	public List<LiteDbTable> findByCatalog(String name) {
		name=name.toLowerCase();
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			if (c.getCatalogName().toLowerCase().contains(name)) {
				for (LiteDbSchema s : c.getSchemas()) {
					for (LiteDbTable t : s.getTables()) {
						list.add(t);
					}
				}
			}
		}
		return list;
	}

	public List<LiteDbTable> getAllTables() {
		List<LiteDbTable> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				for (LiteDbTable t : s.getTables()) {
					list.add(t);
				}
			}
		}
		return list;
	}

	public List<LiteDbSchema> getAllSchemas() {
		List<LiteDbSchema> list = new ArrayList<>();
		for (LiteDbCatalog c : catalogs) {
			for (LiteDbSchema s : c.getSchemas()) {
				list.add(s);
			}
		}
		return list;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public List<LiteDbCatalog> getCatalogs() {
		return catalogs;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Database:[" + databaseName + "] ");
		b.append("\n");
		for (LiteDbCatalog c : catalogs) {
			b.append(c.toString());
		}
		return b.toString();
	}

}

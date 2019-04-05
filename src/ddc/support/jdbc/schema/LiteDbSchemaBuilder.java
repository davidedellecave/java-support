package ddc.support.jdbc.schema;

import java.sql.Connection;

public interface LiteDbSchemaBuilder {
	public void build(LiteDb db, Connection connection, String schema, String table) throws Exception;
}

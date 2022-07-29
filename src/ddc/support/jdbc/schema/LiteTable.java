package ddc.support.jdbc.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author davidedc
 */
public class LiteTable {
	private LiteDbTable schema = null;
	private List<LiteRow> rows = new ArrayList<LiteRow>();
	
	public LiteDbTable getSchema() {
		return schema;
	}
	public void setSchema(LiteDbTable schema) {
		this.schema = schema;
	}
	public List<LiteRow> getRows() {
		return rows;
	}
	public void setRows(List<LiteRow> rows) {
		this.rows = rows;
	}
	
//	public static LiteTable build_(ResultSet rs) throws SQLException {		
//		LiteTable t = new LiteTable();
//		t.setSchema(new LiteDbTable());
//		t.setSchema(LiteDbTable.build(rs));
//		while (rs.next()) {
//			t.rows.add(LiteRow.build(rs));
//		}
//		return t;
//	}
}

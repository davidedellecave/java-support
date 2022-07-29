package ddc.support.data;

public class TableInfo {

	public TableInfo(String table, String pkName, String fkName, String identityName, String orderbyCol) {
		this.tableName = table;
		this.pkCol = pkName;
		this.fkCol = fkName;
		this.identityCol = identityName;
		this.orderbyCol = orderbyCol;
	}

	public String tableName;
	public String pkCol;
	public String fkCol;
	public String identityCol;
	public String orderbyCol;

	@Override
	public String toString() {
        return tableName;
    }
}
package ddc.support.data;

public abstract class EntityTable<T> extends BaseEntityTable
{

//	public abstract T createEntity(DataRow row);
//
//	public abstract int updateEntity(String connectionString, T entity);
//
//	public abstract int insertEntity(String connectionString, T entity);
//
//	public abstract int deleteEntity(String connectionString, T entity);
//
//	public String getTableName() {
//            return tableInfo().tableName;
//        }
//
//	public virtual DataTable
//
//	getTable(String connectionString) {
//            TableInfo info = tableInfo();
//            if (info == null)
//                throw new Exception("getTable() TableInfo is null");
//            DataTable table = null;
//            SqlUtils u = new SqlUtils();
//            if (info.orderbyCol != null) {
//                table = u.selectTable(connectionString, info.tableName, info.orderbyCol);
//            } else {
//                table = u.selectTable(connectionString, info.tableName);
//            }
//            SqlUtils.logTable(info.tableName, table, Logger.TRACE);
//            return table;
//        }
//
//	public virtual List<T> getList(String connectionString) {
//            DataTable table = getTable(connectionString);
//            List<T> list = new List<T>();
//            foreach (DataRow row in table.Rows) {
//                T o = createEntity(row);
//                list.Add(o);
//            }
//            return list;
//        }
//
//	public List<T> getList(DataTable table) {
//            List<T> list = new List<T>();
//            foreach (DataRow row in table.Rows) {
//                T o = createEntity(row);
//                list.Add(o);
//            }
//            return list;
//        }
//
//	public virtual int update(String connectionString, T entity) {
//            int affected = updateEntity(connectionString, entity);
//            return affected;
//        }
//
//	public virtual int delete(String connectionString, T entity) {
//            int affected = deleteEntity(connectionString, entity);
//            return affected;
//
//            //TableInfo info = getTableInfo();
//            //SqlUtils u = new SqlUtils();
//            //int affected = u.deleteRows(connectionString, info.tableName, info.pkCol, value.ToString());
//            //return affected;
//        }
//
//	public virtual int deleteAll(String connectionString) {
//            SqlUtils u = new SqlUtils();
//            TableInfo info = tableInfo();
//            int affected = u.executeNonQuery(connectionString, "DELETE FROM " + info.tableName);
//            return affected;
//        }
//
//	public virtual int insert(String connectionString, T entity) {
//            int affected = insertEntity(connectionString, entity);
//            return affected;
//        }
}

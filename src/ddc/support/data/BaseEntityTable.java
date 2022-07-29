package ddc.support.data;

public abstract class BaseEntityTable {
//	private static final org.apache.logging.log4j.Logger logger = getLogger(BaseEntityTable.class);
//	public abstract TableInfo getTableInfo();
//    public abstract void createTable(String connectionString);
//    
//    public void exportTable(String connectionString, String dataFolder) {
//        TableInfo table = getTableInfo();            
//        String file = getDataFilename(dataFolder, table.tableName);
//        String infoHeader = "Exporting Table - " + table.tableName + "] file:[" + file + "]";
//        logger.debug(infoHeader);
//        try {
//            SqlUtils u = new SqlUtils();
//            u.writeTableToFile(connectionString, table.tableName, file);
//        } catch (Exception e) {
//            logger.error(infoHeader, e);
//            throw e;
//        }
//    }
//
//    public long countTable(String connectionString) {
//        Connection connection = null;
//        SqlCommand command = null;
//        String sql = null;
//        try {
//            connection = new SqlConnection(connectionString);
//            sql = "SELECT COUNT(*) FROM " + tableInfo().tableName;
//            logger.debug("sql:[" + sql + "]");
//            command = new SqlCommand(sql, connection);
//            connection.Open();
//            Object o = command.ExecuteScalar();
//            long n = long.Parse(o.ToString());
//            logger.debug("countTable scalar:[" + n + "] sql:[" + sql + "]");
//            return n;
//        } finally {
//            if (connection != null)
//                connection.Close();
//        }
//    }
//
//    public void importTable(String connectionString, String dataFolder) {
//        TableInfo table = tableInfo();            
//        String file = getDataFilename(dataFolder, table.tableName);
//        String infoHeader = "Importing Table - " + table.tableName + "] file:[" + file + "]";
//        logger.debug(infoHeader);
//        try {
//            SqlUtils u = new SqlUtils();
//            u.importTableFromFile(connectionString, table.tableName, file);
//        } catch (Exception e) {
//            logger.error(infoHeader, e);
//            throw e;
//        }
//    }
//
//    private String getDataFilename(String dataFolder, String table) {
//        return dataFolder + "\\" + table + ".xml";
//    }

}

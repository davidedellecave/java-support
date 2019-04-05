package ddc.support.jdbc.db;

import java.sql.JDBCType;
import java.util.Map;
import java.util.TreeMap;

public class SqlTypeMap extends TreeMap<JDBCType,String> {
	private static final long serialVersionUID = -2326922497298663983L;
	
	public JDBCType getJDBCType(String targetType) {
		String search=targetType.trim();
		for (Map.Entry<JDBCType, String> s : this.entrySet()) {
			if (s.getValue().equalsIgnoreCase(search)) return s.getKey();
		}
		return null;
	}
}

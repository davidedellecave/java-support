package ddc.support.jdbc;

import java.io.IOException;

import ddc.support.jack.JackUtil;

public class JdbcConfig {
	private String driver=null;
	private String host = "";
	private int port = 0;
	private String database = "";
	private String user = "";
	private String password = "";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Override
	public String toString() {
		try {
			return JackUtil.toString(this);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
//		return user + "@" + host + ":" + port + "/" + database;
	}

}

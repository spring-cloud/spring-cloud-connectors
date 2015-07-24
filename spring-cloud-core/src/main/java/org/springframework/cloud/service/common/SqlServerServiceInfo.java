package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo;

@ServiceInfo.ServiceLabel("sqlserver")
public class SqlServerServiceInfo extends RelationalServiceInfo {
	private static final String JDBC_URL_TYPE = "sqlserver";

	public static final String SQLSERVER_SCHEME = JDBC_URL_TYPE;

	public SqlServerServiceInfo(String id, String url) {
		super(id, url, JDBC_URL_TYPE);
	}
	
	@Override  
	 public String getJdbcUrl()   
	 {  
		return String.format("jdbc:%s://%s:%d;database=%s;user=%s;password=%s;",
				jdbcUrlDatabaseType, 
				getHost(), getPort(), getPath(), getUserName(), getPassword());
	 }  
}

package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo;

@ServiceInfo.ServiceLabel("db2")
public class DB2ServiceInfo extends RelationalServiceInfo {

    private static final String JDBC_URL_TYPE = "db2";

    public static final String DB2_SCHEME = JDBC_URL_TYPE;

    public DB2ServiceInfo(String id, String url) {
		super(id, url, JDBC_URL_TYPE);
	}

	@ServiceProperty(category = "connection")
	@Override
	public String getJdbcUrl() {
		if (getUriInfo().getUriString().startsWith(JDBC_PREFIX)) {
			return getUriInfo().getUriString();
		}
		                    
		return String.format("jdbc:%s://%s:%d/%s:user=%s;password=%s;",
				jdbcUrlDatabaseType, 
				getHost(), getPort(), getPath(), getUserName(), getPassword());
	}
	
}

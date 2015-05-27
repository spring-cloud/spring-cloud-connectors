package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo;

@ServiceInfo.ServiceLabel("oracle")
public class OracleServiceInfo extends RelationalServiceInfo {

	private static final String JDBC_URL_TYPE = "oracle";

	public static final String ORACLE_SCHEME = JDBC_URL_TYPE;

	public OracleServiceInfo(String id, String url) {
		super(id, url, JDBC_URL_TYPE);
	}

	@Override
	public String getJdbcUrl() {
		if (getUriInfo().getUriString().startsWith(JDBC_PREFIX)) {
			return getUriInfo().getUriString();
		}

		return String.format("jdbc:%s:thin:%s/%s@%s:%d/%s",
				jdbcUrlDatabaseType, getUserName(), getPassword(),
				getHost(), getPort(), getPath());
	}

}

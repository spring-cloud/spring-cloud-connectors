package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo;

@ServiceInfo.ServiceLabel("oracle")
public class OracleServiceInfo extends RelationalServiceInfo {
	public static final String ORACLE_SCHEME = "oracle";

	public OracleServiceInfo(String id, String url) {
		this(id, url, null);
	}

	public OracleServiceInfo(String id, String url, String jdbcUrl) {
		super(id, url, jdbcUrl, ORACLE_SCHEME);
	}

	@Override
	protected String buildJdbcUrl() {
		return String.format("jdbc:%s:thin:%s/%s@%s:%d/%s",
				jdbcUrlDatabaseType, getUserName(), getPassword(),
				getHost(), getPort(), getPath());
	}
}

package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo;

@ServiceInfo.ServiceLabel("db2")
public class DB2ServiceInfo extends RelationalServiceInfo {
	public static final String DB2_SCHEME = "db2";

	public DB2ServiceInfo(String id, String url) {
		this(id, url, null);
	}

	public DB2ServiceInfo(String id, String url, String jdbcUrl) {
		super(id, url, jdbcUrl, DB2_SCHEME);
	}

	@Override
	protected String buildJdbcUrl() {
		return String.format("jdbc:%s://%s:%d/%s:user=%s;password=%s;",
				jdbcUrlDatabaseType,
				getHost(), getPort(), getPath(), getUserName(), getPassword());
	}
}

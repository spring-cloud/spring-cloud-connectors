package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 *
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("postgresql")
public class PostgresqlServiceInfo extends RelationalServiceInfo {
	public static final String POSTGRES_SCHEME = "postgres";
	public static final String POSTGRES_JDBC_SCHEME = "postgresql";

	public PostgresqlServiceInfo(String id, String url) {
		this(id, url, null);
	}

	public PostgresqlServiceInfo(String id, String url, String jdbcUrl) {
		super(id, url, jdbcUrl, POSTGRES_JDBC_SCHEME);
	}
}

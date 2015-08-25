package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 *
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("mysql")
public class MysqlServiceInfo extends RelationalServiceInfo {
	public static final String MYSQL_SCHEME = "mysql";

	public MysqlServiceInfo(String id, String url) {
		this(id, url, null);
	}

	public MysqlServiceInfo(String id, String url, String jdbcUrl) {
		super(id, url, jdbcUrl, MYSQL_SCHEME);
	}
}

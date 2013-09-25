package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;


/**
 * 
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("postgresql")
public class PostgresqlServiceInfo extends RelationalServiceInfo {
	public PostgresqlServiceInfo(String id, String url) {
		super(id, url, "postgresql");
	}
}

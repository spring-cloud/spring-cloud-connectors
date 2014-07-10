package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 *
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("mysql")
public class MysqlServiceInfo extends RelationalServiceInfo {

    public static final String URI_SCHEME = "mysql";

	public MysqlServiceInfo(String id, String url) {
		super(id, url, URI_SCHEME);
	}
}

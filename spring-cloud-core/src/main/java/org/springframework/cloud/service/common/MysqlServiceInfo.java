package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 *
 * @author Ramnivas Laddad
 *
 */
@ServiceLabel("mysql")
public class MysqlServiceInfo extends RelationalServiceInfo {

    public static final String JDBC_URL_TYPE = "mysql";

    public static final String URI_SCHEME = JDBC_URL_TYPE;

    public MysqlServiceInfo(String id, String url) {
        super(id, url, URI_SCHEME);
    }
}

package org.springframework.cloud.service.common;

import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * @author GJA
 * @version $Revision:$ $Date$
 */
@ServiceLabel("sqldb")
public class DB2ServiceInfo extends RelationalServiceInfo {

    public DB2ServiceInfo(String id, String uriString) {
        super(id, uriString, "db2");
    }

    @Override
    public String getJdbcUrl() {
        return String.format("jdbc:%s://%s:%s/%s:user=%s;password=%s;",
              jdbcUrlDatabaseType, getHost(), getPort(),
              getPath(), getUserName(), getPassword());
    }
}
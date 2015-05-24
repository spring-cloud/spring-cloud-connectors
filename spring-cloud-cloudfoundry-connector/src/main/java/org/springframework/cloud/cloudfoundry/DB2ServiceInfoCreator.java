package org.springframework.cloud.cloudfoundry;

import org.springframework.cloud.service.common.DB2ServiceInfo;

/**
 * @author GJA
 * @version $Revision:$ $Date$
 */
public class DB2ServiceInfoCreator extends RelationalServiceInfoCreator<DB2ServiceInfo> {

    public DB2ServiceInfoCreator() {
        super(new Tags("sqldb"), "db2");
    }

    @Override
    public DB2ServiceInfo createServiceInfo(String id, String uri) {
        return new DB2ServiceInfo(id, uri);
    }
}

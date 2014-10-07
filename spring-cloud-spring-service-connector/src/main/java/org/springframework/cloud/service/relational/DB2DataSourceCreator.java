package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.common.DB2ServiceInfo;

/**
 * @author GJA
 * @version $Revision:$ $Date$
 */
public class DB2DataSourceCreator extends DataSourceCreator<DB2ServiceInfo> {

    private static final String[] DRIVERS = new String[] { "com.ibm.db2.jcc.DB2Driver" };
    private static final String VALIDATION_QUERY = "VALUES 1";
    private static final String DRIVER_PROPERTY_KEY = "spring-cloud.db2.driver";

    public DB2DataSourceCreator() {
        super(DRIVER_PROPERTY_KEY, DRIVERS, VALIDATION_QUERY);
    }
}

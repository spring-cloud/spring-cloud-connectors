package org.springframework.cloud.service.relational;

import static org.springframework.cloud.service.Util.hasClass;

import javax.sql.DataSource;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 * @param <SI> the {@link RelationalServiceInfo} type for the underlying database service
 */
public class BasicDbcpPooledDataSourceCreator<SI extends RelationalServiceInfo> extends DbcpLikePooledDataSourceCreator<SI> {

    @Override
    public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
                             String driverClassName, String validationQuery) {
        if (hasClass("org.apache.commons.dbcp2.BasicDataSource")) {
            logger.info("Found DBCP2 on the classpath. Using it for DataSource connection pooling.");
            org.apache.commons.dbcp2.BasicDataSource ds = new org.apache.commons.dbcp2.BasicDataSource();
            setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
            return ds;
        } else if (hasClass("org.apache.commons.dbcp.BasicDataSource")) {
            logger.info("Found DBCP on the classpath. Using it for DataSource connection pooling.");
            org.apache.commons.dbcp.BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
            setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
            return ds;
        } else {
            return null;
        }
    }
}

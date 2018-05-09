package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 * @param <SI> the {@link RelationalServiceInfo} type for the underlying database service
 */
public class BasicDbcpPooledDataSourceCreator<SI extends RelationalServiceInfo> extends DbcpLikePooledDataSourceCreator<SI> {
	static final String DBCP2_BASIC_DATASOURCE = "org.apache.commons.dbcp2.BasicDataSource";

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
							 String driverClassName, String validationQuery) {
		logger.info("Found DBCP2 on the classpath. Using it for DataSource connection pooling.");
		org.apache.commons.dbcp2.BasicDataSource ds = new org.apache.commons.dbcp2.BasicDataSource();
		setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
		return ds;
	}
}

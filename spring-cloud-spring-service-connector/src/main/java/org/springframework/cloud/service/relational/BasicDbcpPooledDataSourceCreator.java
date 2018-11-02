package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

import static org.springframework.cloud.service.Util.hasClass;

/**
 *
 * @author Ramnivas Laddad
 *
 * @param <SI> the {@link RelationalServiceInfo} type for the underlying database service
 */
public class BasicDbcpPooledDataSourceCreator<SI extends RelationalServiceInfo> extends DbcpLikePooledDataSourceCreator<SI> {
	public static final String DBCP2_BASIC_DATASOURCE = "org.apache.commons.dbcp2.BasicDataSource";
	public static final String DBCP_BASIC_DATASOURCE = "org.apache.commons.dbcp.BasicDataSource";

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
							 String driverClassName, String validationQuery) {
		if (hasClass(DBCP2_BASIC_DATASOURCE)) {
			logger.info("Found DBCP2 on the classpath. Using it for DataSource connection pooling.");
			org.apache.commons.dbcp2.BasicDataSource ds = new org.apache.commons.dbcp2.BasicDataSource();
			setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
			return new UrlDecodingDataSource(ds, "url");
		} else if (hasClass(DBCP_BASIC_DATASOURCE)) {
			logger.info("Found DBCP on the classpath. Using it for DataSource connection pooling.");
			org.apache.commons.dbcp.BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
			setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
			return new UrlDecodingDataSource(ds, "url");
		} else {
			return null;
		}
	}
}

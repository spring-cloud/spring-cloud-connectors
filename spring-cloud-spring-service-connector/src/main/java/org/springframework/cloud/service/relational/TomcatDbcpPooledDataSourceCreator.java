package org.springframework.cloud.service.relational;

import static org.springframework.cloud.service.Util.hasClass;

import javax.sql.DataSource;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 *
 * @author Ramnivas Laddad
 *
 * @param <SI> the {@link RelationalServiceInfo} type for the underlying database service
 */
public class TomcatDbcpPooledDataSourceCreator<SI extends RelationalServiceInfo> extends DbcpLikePooledDataSourceCreator<SI> {

	public static final String TOMCAT_7_DBCP = "org.apache.tomcat.dbcp.dbcp.BasicDataSource";
	public static final String TOMCAT_8_DBCP = "org.apache.tomcat.dbcp.dbcp2.BasicDataSource";

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
                             String driverClassName, String validationQuery) {
		if (hasClass(TOMCAT_7_DBCP)) {
			logger.info("Found Tomcat 7 DBCP connection pool on the classpath. Using it for DataSource connection pooling.");
			return createDataSource(TOMCAT_7_DBCP, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
		} else if (hasClass(TOMCAT_8_DBCP)) {
			logger.info("Found Tomcat 8 DBCP connection pool on the classpath. Using it for DataSource connection pooling.");
			return createDataSource(TOMCAT_8_DBCP, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
		} else {
			return null;
		}
	}

	private DataSource createDataSource(String className, RelationalServiceInfo serviceInfo,
										ServiceConnectorConfig serviceConnectorConfig,
										String driverClassName, String validationQuery) {
		try {
			DataSource dataSource = (DataSource) Class.forName(className).newInstance();
			setBasicDataSourceProperties(dataSource, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
			return dataSource;
		} catch (Throwable e) {
			throw new ServiceConnectorCreationException("Error instantiating Tomcat DBCP connection pool", e);
		}
	}
}

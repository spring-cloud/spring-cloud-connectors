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
public class TomcatDbcpPooledDataSourceCreator<SI extends RelationalServiceInfo> extends DbcpLikePooledDataSourceCreator<SI> {

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
                             String driverClassName, String validationQuery) {
		if (hasClass("org.apache.tomcat.dbcp.dbcp.BasicDataSource")) {
			logger.info("Found Tomcat dbcp connection pool on the classpath. Using it for DataSource connection pooling.");
			org.apache.tomcat.dbcp.dbcp.BasicDataSource ds = new org.apache.tomcat.dbcp.dbcp.BasicDataSource();
			setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
			return ds;
		} else {
			return null;
		}
	}

}

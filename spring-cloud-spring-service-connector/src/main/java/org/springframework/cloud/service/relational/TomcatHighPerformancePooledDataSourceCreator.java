package org.springframework.cloud.service.relational;

import static org.springframework.cloud.service.Util.hasClass;

import javax.sql.DataSource;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 * @param <SI>
 */
public class TomcatHighPerformancePooledDataSourceCreator<SI extends RelationalServiceInfo> 
	extends DbcpLikePooledDataSourceCreator<SI> {

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
                             String driverClassName, String validationQuery) {
		if (hasClass("org.apache.tomcat.jdbc.pool.DataSource")) {
			logger.info("Found Tomcat connection pool on the classpath. Using it for DataSource connection pooling.");
			org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
			setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
			return ds;
		} else {
			return null;
		}
	}

}

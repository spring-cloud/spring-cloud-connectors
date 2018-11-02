package org.springframework.cloud.service.relational;

import javax.sql.DataSource;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

import static org.springframework.cloud.service.Util.hasClass;

/**
 *
 * @author Ramnivas Laddad
 */
public class TomcatJdbcPooledDataSourceCreator<SI extends RelationalServiceInfo>
	extends DbcpLikePooledDataSourceCreator<SI> {

	public static final String TOMCAT_JDBC_DATASOURCE = "org.apache.tomcat.jdbc.pool.DataSource";

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
                             String driverClassName, String validationQuery) {
		if (hasClass(TOMCAT_JDBC_DATASOURCE)) {
			logger.info("Found Tomcat JDBC connection pool on the classpath. Using it for DataSource connection pooling.");
			org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
			setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
			return new UrlDecodingDataSource(ds, "url");
		} else {
			return null;
		}
	}

}

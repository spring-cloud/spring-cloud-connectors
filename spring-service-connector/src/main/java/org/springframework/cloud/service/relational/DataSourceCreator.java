package org.springframework.cloud.service.relational;

import static org.springframework.cloud.service.Util.hasClass;

import java.sql.DriverManager;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.common.RelationalServiceInfo;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * 
 * @author Ramnivas Laddad
 *
 * @param <SI>
 */
public abstract class DataSourceCreator<SI extends RelationalServiceInfo> extends AbstractServiceConnectorCreator<DataSource, SI> {

	private static Logger logger = Logger.getLogger(DataSourceCreator.class.getName());
	
	private DataSourceConfigurer configurer = new DataSourceConfigurer();
	
	public abstract String getDriverClassName();

	public abstract String getValidationQuery();

	@Override
	public DataSource create(SI serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		try {
			Class.forName(getDriverClassName());
			// Give first preference to user's DBCP datasource
			if (hasClass("org.apache.commons.dbcp.BasicDataSource")) {
				logger.info("Found DBCP on the classpath. Using it for DataSource connection pooling.");
				org.apache.commons.dbcp.BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
				setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig);
				return ds;
				// else, we have one from Tomcat
			} else if (hasClass("org.apache.tomcat.jdbc.pool.DataSource")) {
				logger.info("Found Tomcat connection pool on the classpath. Using it for DataSource connection pooling.");
				org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
				setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig);
				return ds;
			} else {
				// Only for testing outside Tomcat/CloudFoundry
				logger.warning("Found neither DBCP nor Tomcat connection pool on the classpath (no pooling in effect)");
				return new SimpleDriverDataSource(DriverManager.getDriver(serviceInfo.getJdbcUrl()),
						serviceInfo.getJdbcUrl());
			}
		} catch (Exception e) {
			throw new ServiceConnectorCreationException(
					"Failed to created cloud datasource for "
							+ serviceInfo.getId() + " service", e);
		}
	}

	private void setBasicDataSourceProperties(DataSource basicDataSource, RelationalServiceInfo serviceInfo,
											  ServiceConnectorConfig serviceConnectorConfig) {
		BeanWrapper target = new BeanWrapperImpl(basicDataSource);
		target.setPropertyValue("driverClassName", getDriverClassName());
		target.setPropertyValue("url", serviceInfo.getJdbcUrl());
		if (getValidationQuery() != null) {
			target.setPropertyValue("validationQuery", getValidationQuery());
			target.setPropertyValue("testOnBorrow", true);
		}
		configurer.configure(basicDataSource, (DataSourceConfig)serviceConnectorConfig);
	}
}

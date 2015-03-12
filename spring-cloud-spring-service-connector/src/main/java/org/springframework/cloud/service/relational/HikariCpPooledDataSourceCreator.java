package org.springframework.cloud.service.relational;

import static org.springframework.cloud.service.Util.*;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

import com.zaxxer.hikari.HikariDataSource;

public class HikariCpPooledDataSourceCreator<SI extends RelationalServiceInfo> implements PooledDataSourceCreator<SI> {

	protected static Logger logger = Logger.getLogger(PooledDataSourceCreator.class.getName());

	private static final String HIKARI_CLASSNAME = "com.zaxxer.hikari.HikariDataSource";

	private DataSourceConfigurer configurer = new DataSourceConfigurer();

	protected void setBasicDataSourceProperties(DataSource basicDataSource, RelationalServiceInfo serviceInfo,
											   ServiceConnectorConfig serviceConnectorConfig,
												 String driverClassName, String validationQuery) {
		BeanWrapper target = new BeanWrapperImpl(basicDataSource);
		target.setPropertyValue("driverClassName", driverClassName);
		target.setPropertyValue("jdbcUrl", serviceInfo.getJdbcUrl());
		if (validationQuery != null) {
			target.setPropertyValue("connectionTestQuery", validationQuery);
		}

		if (serviceConnectorConfig == null) {
			// choose sensible values so that we set max connection pool size to what
			// free tier services on Cloud Foundry and Heroku allow
			target.setPropertyValue("maximumPoolSize", 10);
			target.setPropertyValue("connectionTimeout", 30000);
			target.setPropertyValue("idleTimeout", 30000);
			target.setPropertyValue("maxLifetime", 55000);
		}
		configurer.configure(basicDataSource, (DataSourceConfig)serviceConnectorConfig);
	}

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
							 String driverClassName, String validationQuery) {
		if (hasClass(HIKARI_CLASSNAME)) {
			logger.info("Found HikariCP on the classpath. Using it for DataSource connection pooling.");
			HikariDataSource ds = new HikariDataSource();
			setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
			return ds;
		} else {
			return null;
		}
	}

}

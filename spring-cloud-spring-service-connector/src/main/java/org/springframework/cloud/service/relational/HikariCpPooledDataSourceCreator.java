package org.springframework.cloud.service.relational;

import java.util.logging.Logger;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

import static org.springframework.cloud.service.Util.hasClass;

public class HikariCpPooledDataSourceCreator<SI extends RelationalServiceInfo> implements PooledDataSourceCreator<SI> {

	protected static Logger logger = Logger.getLogger(PooledDataSourceCreator.class.getName());

	public static final String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

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

		configurer.configure(basicDataSource, (DataSourceConfig)serviceConnectorConfig);
	}

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
							 String driverClassName, String validationQuery) {
		if (hasClass(HIKARI_DATASOURCE)) {
			logger.info("Found HikariCP on the classpath. Using it for DataSource connection pooling.");
			HikariDataSource ds = new HikariDataSource();
			setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
			return new UrlDecodingDataSource(ds, "jdbcUrl");
		} else {
			return null;
		}
	}

}

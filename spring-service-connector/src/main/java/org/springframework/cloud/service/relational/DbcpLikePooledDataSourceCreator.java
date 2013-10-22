package org.springframework.cloud.service.relational;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * Common implementation that assumes DBCP connection pool properties.
 * 
 * @author Ramnivas Laddad
 *
 * @param <SI>
 */
public abstract class DbcpLikePooledDataSourceCreator<SI extends RelationalServiceInfo> implements PooledDataSourceCreator<SI> {
	
	protected static Logger logger = Logger.getLogger(PooledDataSourceCreator.class.getName());

	private DataSourceConfigurer configurer = new DataSourceConfigurer();

	protected void setBasicDataSourceProperties(DataSource basicDataSource, RelationalServiceInfo serviceInfo,
											   ServiceConnectorConfig serviceConnectorConfig,
  											   String driverClassName, String validationQuery) {
		BeanWrapper target = new BeanWrapperImpl(basicDataSource);
		target.setPropertyValue("driverClassName", driverClassName);
		target.setPropertyValue("url", serviceInfo.getJdbcUrl());
		if (validationQuery != null) {
			target.setPropertyValue("validationQuery", validationQuery);
			target.setPropertyValue("testOnBorrow", true);
		}
		configurer.configure(basicDataSource, (DataSourceConfig)serviceConnectorConfig);
	}

}

package org.springframework.cloud.service.relational;

import static org.springframework.cloud.service.Util.setCorrespondingProperties;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.MapServiceConnectionConfigurer;
import org.springframework.cloud.service.MapServiceConnectorConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfigurer;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceConfigurer extends PooledServiceConnectorConfigurer<DataSource, DataSourceConfig> {
	private MapServiceConnectionConfigurer<DataSource, MapServiceConnectorConfig> mapServiceConnectionConfigurer =
			new MapServiceConnectionConfigurer<DataSource, MapServiceConnectorConfig>();

	@Override
	public DataSource configure(DataSource dataSource, DataSourceConfig config) {
		if (config != null) {
			configureConnection(dataSource, config);
			configureConnectionProperties(dataSource, config);
			return super.configure(dataSource, config);
		}
		return dataSource;
	}

	private void configureConnection(DataSource dataSource, DataSourceConfig config) {
		if (config.getConnectionConfiguration() != null) {
			BeanWrapper target = new BeanWrapperImpl(dataSource);
			BeanWrapper connectionSource = new BeanWrapperImpl(config.getConnectionConfiguration());
			setCorrespondingProperties(target, connectionSource);
		}
	}

	private void configureConnectionProperties(DataSource dataSource, DataSourceConfig config) {
		if (config.getConnectionProperties() != null) {
			mapServiceConnectionConfigurer.configure(dataSource, config.getConnectionProperties());
		}
	}
}

package org.springframework.cloud.service.relational;

import static org.springframework.cloud.service.Util.setCorrespondingProperties;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.PooledServiceConnectorConfigurer;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceConfigurer extends PooledServiceConnectorConfigurer<DataSource, DataSourceConfig> {
	@Override
	public DataSource configure(DataSource dataSource, DataSourceConfig config) {
		if (config != null) {
			BeanWrapper target = new BeanWrapperImpl(dataSource);
			
			if (config.getConnectionConfiguration() != null) {
				BeanWrapper connectionSource = new BeanWrapperImpl(config.getConnectionConfiguration());
				setCorrespondingProperties(target, connectionSource);
			}
			return super.configure(dataSource, config);
		}
		return dataSource;
	}
}

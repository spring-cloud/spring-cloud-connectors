package org.springframework.cloud.service;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;

import java.util.Map;

/**
 * Configurer for service connector that takes a map of properties to apply to the connection.
 *
 * @author Scott Frederick
 *
 * @param <SC> service connector type
 * @param <SCC> service connector configurer type
 */
public class MapServiceConnectionConfigurer<SC, SCC extends MapServiceConnectorConfig>
		implements ServiceConnectorConfigurer<SC, SCC> {
	@Override
	public SC configure(SC serviceConnector, SCC config) {
		if (config != null) {
			Map<String, Object> properties = config.getConnectionProperties();
			if (properties != null) {
				BeanWrapper target = new BeanWrapperImpl(serviceConnector);
				target.setPropertyValues(new MutablePropertyValues(properties), true);
			}
		}
		return serviceConnector;
	}
}

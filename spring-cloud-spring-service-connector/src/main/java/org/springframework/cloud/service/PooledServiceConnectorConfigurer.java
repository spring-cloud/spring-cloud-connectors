package org.springframework.cloud.service;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;

import static org.springframework.cloud.service.Util.*;

/**
 * Configurer for pooling config
 * 
 * @author Ramnivas Laddad
 *
 * @param <SC> service connector tyoe
 * @param <SCC> service connector config type
 */
public class PooledServiceConnectorConfigurer<SC, SCC extends PooledServiceConnectorConfig> implements ServiceConnectorConfigurer<SC, SCC> {

	@Override
	public SC configure(SC serviceConnector, SCC config) {
		if (config != null) {
			BeanWrapper target = new BeanWrapperImpl(serviceConnector);
			PoolConfig poolConfig = config.getPoolConfig();
			if (poolConfig != null) {
				BeanWrapper poolSource = new BeanWrapperImpl(poolConfig);
				setCorrespondingProperties(target, poolSource);
			}
		}
		return serviceConnector;
	}

}

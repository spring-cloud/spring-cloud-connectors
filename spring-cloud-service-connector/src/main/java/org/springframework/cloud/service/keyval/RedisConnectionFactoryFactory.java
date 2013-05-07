package org.springframework.cloud.service.keyval;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Spring factory bean for redis service.
 *
 * @author Ramnivas Laddad
 *
 */
public class RedisConnectionFactoryFactory extends AbstractCloudServiceConnectorFactory<RedisConnectionFactory> {
	public RedisConnectionFactoryFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, RedisConnectionFactory.class, serviceConnectorConfiguration);
	}
}

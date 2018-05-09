package org.springframework.cloud.service.keyval;

import redis.clients.jedis.JedisPoolConfig;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.MapServiceConnectionConfigurer;
import org.springframework.cloud.service.MapServiceConnectorConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfigurer;
import org.springframework.cloud.service.Util;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;

/**
 * 
 * @author Scott Frederick
 *
 */
public class RedisJedisClientConfigurer implements ServiceConnectorConfigurer<JedisClientConfigurationBuilder, RedisConnectionFactoryConfig> {
	private MapServiceConnectionConfigurer<JedisClientConfigurationBuilder, MapServiceConnectorConfig> mapClientConfigurer =
			new MapServiceConnectionConfigurer<JedisClientConfigurationBuilder, MapServiceConnectorConfig>();

	@Override
	public JedisClientConfigurationBuilder configure(JedisClientConfigurationBuilder clientConfiguration, RedisConnectionFactoryConfig config) {
		if (config != null) {
			configurePool(clientConfiguration, config);
			configureClient(clientConfiguration, config);
		}
		return clientConfiguration;
	}

	public JedisClientConfigurationBuilder configure(JedisClientConfigurationBuilder clientConfiguration, PooledServiceConnectorConfig config) {
		if (config != null) {
			configurePool(clientConfiguration, config);
		}
		return clientConfiguration;
	}

	private void configurePool(JedisClientConfigurationBuilder clientConfiguration, PooledServiceConnectorConfig config) {
		if (config.getPoolConfig() != null) {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			BeanWrapper target = new BeanWrapperImpl(poolConfig);
			BeanWrapper source = new BeanWrapperImpl(config.getPoolConfig());
			Util.setCorrespondingProperties(target, source);

			clientConfiguration.usePooling().poolConfig(poolConfig);
		}
	}

	private void configureClient(JedisClientConfigurationBuilder clientConfiguration, RedisConnectionFactoryConfig config) {
		if (config.getConnectionProperties() != null) {
			mapClientConfigurer.configure(clientConfiguration, config.getConnectionProperties());
		}
	}

}

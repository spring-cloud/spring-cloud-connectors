package org.springframework.cloud.service.keyval;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.MapServiceConnectionConfigurer;
import org.springframework.cloud.service.MapServiceConnectorConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfigurer;
import org.springframework.cloud.service.Util;

/**
 * 
 * @author Scott Frederick
 *
 */
public class RedisLettuceClientConfigurer implements ServiceConnectorConfigurer<LettuceClientConfigurationBuilder, RedisConnectionFactoryConfig> {
	private MapServiceConnectionConfigurer<LettuceClientConfigurationBuilder, MapServiceConnectorConfig> mapClientConfigurer =
			new MapServiceConnectionConfigurer<>();

	@Override
	public LettuceClientConfigurationBuilder configure(LettuceClientConfigurationBuilder clientConfiguration, RedisConnectionFactoryConfig config) {
		if (config != null) {
			configurePool(clientConfiguration, config);
			configureClient(clientConfiguration, config);
		}
		return clientConfiguration;
	}

	public LettuceClientConfigurationBuilder configure(LettuceClientConfigurationBuilder clientConfiguration, PooledServiceConnectorConfig config) {
		if (config != null) {
			configurePool(clientConfiguration, config);
		}
		return clientConfiguration;
	}

	private void configurePool(LettuceClientConfigurationBuilder clientConfiguration, PooledServiceConnectorConfig config) {
		if (config.getPoolConfig() != null) {
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			BeanWrapper target = new BeanWrapperImpl(poolConfig);
			BeanWrapper source = new BeanWrapperImpl(config.getPoolConfig());
			Util.setCorrespondingProperties(target, source);

			((LettucePoolingClientConfigurationBuilder) clientConfiguration).poolConfig(poolConfig);
		}
	}

	private void configureClient(LettuceClientConfigurationBuilder clientConfiguration, RedisConnectionFactoryConfig config) {
		if (config.getConnectionProperties() != null) {
			mapClientConfigurer.configure(clientConfiguration, config.getConnectionProperties());
		}
	}

}

package org.springframework.cloud.service.keyval;

import org.springframework.cloud.service.*;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * 
 * @author Mark Paluch
 */
public class RedisLettuceConnectionFactoryConfigurer implements ServiceConnectorConfigurer<LettuceConnectionFactory, RedisConnectionFactoryConfig> {
	private MapServiceConnectionConfigurer<LettuceConnectionFactory, MapServiceConnectorConfig> mapServiceConnectionConfigurer =
			new MapServiceConnectionConfigurer<LettuceConnectionFactory, MapServiceConnectorConfig>();

	@Override
	public LettuceConnectionFactory configure(LettuceConnectionFactory connectionFactory, RedisConnectionFactoryConfig config) {
		if (config != null) {
			configureConnection(connectionFactory, config);
		}
		return connectionFactory;
	}

	private void configureConnection(LettuceConnectionFactory connectionFactory, RedisConnectionFactoryConfig config) {
		if (config.getConnectionProperties() != null) {
			mapServiceConnectionConfigurer.configure(connectionFactory, config.getConnectionProperties());
		}
	}
}

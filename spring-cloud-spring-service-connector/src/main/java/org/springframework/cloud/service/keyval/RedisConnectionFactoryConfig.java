package org.springframework.cloud.service.keyval;

import org.springframework.cloud.service.MapServiceConnectorConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig;

import java.util.Map;

/**
 * @author Scott Frederick
 */
public class RedisConnectionFactoryConfig extends PooledServiceConnectorConfig {
	private MapServiceConnectorConfig connectionConfig;

	public RedisConnectionFactoryConfig(PoolConfig poolConfig) {
		this(poolConfig, null);
	}

	public RedisConnectionFactoryConfig(Map<String, Object> properties) {
		this(null, properties);
	}

	public RedisConnectionFactoryConfig(PoolConfig poolConfig, Map<String, Object> properties) {
		super(poolConfig);
		this.connectionConfig = new MapServiceConnectorConfig(properties);
	}

	public MapServiceConnectorConfig getConnectionProperties() {
		return connectionConfig;
	}
}

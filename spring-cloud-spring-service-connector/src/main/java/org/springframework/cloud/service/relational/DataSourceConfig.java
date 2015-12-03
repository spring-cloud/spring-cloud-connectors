package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.MapServiceConnectorConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceConfig extends PooledServiceConnectorConfig {
	private final ConnectionConfig connectionConfig;
	private final MapServiceConnectorConfig connectionProperties;
	private final List<String> pooledDataSourceNames;

	public DataSourceConfig(PoolConfig poolConfig, ConnectionConfig connectionConfig) {
		this(poolConfig, connectionConfig, null, null);
	}

	public DataSourceConfig(List<String> pooledDataSourceNames) {
		this(null, null, pooledDataSourceNames, null);
	}

	public DataSourceConfig(Map<String, Object> properties) {
		this(null, null, null, properties);
	}

	public DataSourceConfig(PoolConfig poolConfig, ConnectionConfig connectionConfig,
							Map<String, Object> properties) {
		this(poolConfig, connectionConfig, null, properties);
	}

	public DataSourceConfig(PoolConfig poolConfig, ConnectionConfig connectionConfig,
							List<String> pooledDataSourceNames) {
		this(poolConfig, connectionConfig, pooledDataSourceNames, null);
	}

	public DataSourceConfig(PoolConfig poolConfig, ConnectionConfig connectionConfig,
							List<String> pooledDataSourceNames, Map<String, Object> properties) {
		super(poolConfig);
		this.connectionConfig = connectionConfig;
		this.pooledDataSourceNames = pooledDataSourceNames;
		this.connectionProperties = new MapServiceConnectorConfig(properties);
	}

	public ConnectionConfig getConnectionConfiguration() {
		return connectionConfig;
	}

	public List<String> getPooledDataSourceNames() {
		return pooledDataSourceNames;
	}

	public MapServiceConnectorConfig getConnectionProperties() {
		return connectionProperties;
	}

	public static class ConnectionConfig {
		private String prop;

		public ConnectionConfig(String prop) {
			this.prop = prop;
		}
		
		public String getConnectionProperties() {
			return prop;
		}
	}
}

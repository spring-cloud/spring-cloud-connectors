package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.PooledServiceConnectorConfig;

import java.util.List;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceConfig extends PooledServiceConnectorConfig {
	private final ConnectionConfig connectionConfig;
	private final List<String> pooledDataSourceNames;

	public DataSourceConfig(PoolConfig poolConfig, ConnectionConfig connectionConfig) {
		this(poolConfig, connectionConfig, null);
	}

	public DataSourceConfig(List<String> pooledDataSourceNames) {
		this(null, null, pooledDataSourceNames);
	}

	public DataSourceConfig(PoolConfig poolConfig, ConnectionConfig connectionConfig,
							List<String> pooledDataSourceNames) {
		super(poolConfig);
		this.connectionConfig = connectionConfig;
		this.pooledDataSourceNames = pooledDataSourceNames;
	}

	public ConnectionConfig getConnectionConfiguration() {
		return connectionConfig;
	}

	public List<String> getPooledDataSourceNames() {
		return pooledDataSourceNames;
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

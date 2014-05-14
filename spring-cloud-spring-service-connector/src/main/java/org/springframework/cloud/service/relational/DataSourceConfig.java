package org.springframework.cloud.service.relational;

import org.springframework.cloud.service.PooledServiceConnectorConfig;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class DataSourceConfig extends PooledServiceConnectorConfig {
	private ConnectionConfig connectionConfig;

	public DataSourceConfig(PoolConfig poolConfig, ConnectionConfig connectionConfig) {
		super(poolConfig);
		this.connectionConfig = connectionConfig;
	}
	
	public ConnectionConfig getConnectionConfiguration() {
		return connectionConfig;
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

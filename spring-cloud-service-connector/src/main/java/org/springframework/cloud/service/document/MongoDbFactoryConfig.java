package org.springframework.cloud.service.document;

import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Class to hold configuration values for Mongo
 *
 * @author Thomas Risberg
 * @author Ramnivas Laddad
 */
public class MongoDbFactoryConfig implements ServiceConnectorConfig {
	private String writeConcern;
	private Integer connectionsPerHost;
	private Integer maxWaitTime;
	
	public MongoDbFactoryConfig(String writeConcern, Integer connectionsPerHost, Integer maxWaitTime) {
		this.writeConcern = writeConcern;
		this.connectionsPerHost = connectionsPerHost;
		this.maxWaitTime = maxWaitTime;
	}

	public String getWriteConcern() {
		return writeConcern;
	}

	/**
	 * Getter corresponding to the MongoOptions connectionsPerHost field
	 */
	public Integer getConnectionsPerHost() {
		return connectionsPerHost;
	}

	/**
	 * Getter corresponding to the MongoOptions maxWaitTime field
	 */
	public Integer getMaxWaitTime () {
		return maxWaitTime ;
	}
}

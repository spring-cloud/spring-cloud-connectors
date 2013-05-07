package org.springframework.cloud.service.messaging;

import org.springframework.cloud.service.ServiceConnectorConfig;

/**
 * Class to hold configuration values for a Rabbit
 *
 * @author Thomas Risberg
 * @author Ramnivas Laddad
 */
public class RabbitConnectionFactoryConfig implements ServiceConnectorConfig {

	private Integer channelCacheSize;

	public RabbitConnectionFactoryConfig(Integer channelCacheSize) {
		this.channelCacheSize = channelCacheSize;
	}
	
	public Integer getChannelCacheSize() {
		return channelCacheSize;
	}
}

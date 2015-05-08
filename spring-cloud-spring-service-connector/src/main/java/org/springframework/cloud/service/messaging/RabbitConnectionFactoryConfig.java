package org.springframework.cloud.service.messaging;

import org.springframework.cloud.service.MapServiceConnectorConfig;

import java.util.Map;

/**
 * Class to hold configuration values for a Rabbit connection
 *
 * @author Thomas Risberg
 * @author Ramnivas Laddad
 * @author Scott Frederick
 */
public class RabbitConnectionFactoryConfig extends MapServiceConnectorConfig {
	private Integer channelCacheSize;

	public RabbitConnectionFactoryConfig(Map<String, Object> properties) {
		this(properties, null);
	}

	public RabbitConnectionFactoryConfig(Integer channelCacheSize) {
		this(null, channelCacheSize);
	}
	
	public RabbitConnectionFactoryConfig(Map<String, Object> properties, Integer channelCacheSize) {
		super(properties);
		this.channelCacheSize = channelCacheSize;
	}

	public Integer getChannelCacheSize() {
		return channelCacheSize;
	}
}

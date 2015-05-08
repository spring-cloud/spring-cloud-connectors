package org.springframework.cloud.service;

import java.util.Map;

/**
 * Service connector configuration that takes a map of properties to apply to the connection.
 *
 * @author Scott Frederick
 */
public class MapServiceConnectorConfig implements ServiceConnectorConfig {
	private Map<String, Object> properties;

	public MapServiceConnectorConfig(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Map<String, Object> getConnectionProperties() {
		return properties;
	}
}

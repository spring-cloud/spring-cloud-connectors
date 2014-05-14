package org.springframework.cloud.service;

/**
 * Factory for creating connector for any service kind (as long as a suitable {@link ServiceConnectorCreator}
 * is available.
 * 
 * @author Ramnivas Laddad
 *
 */
public class GenericCloudServiceConnectorFactory extends AbstractCloudServiceConnectorFactory<Object> {

	public GenericCloudServiceConnectorFactory(String serviceId, ServiceConnectorConfig connectorConfig) {
		super(serviceId, null, connectorConfig);
	}
}

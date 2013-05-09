package org.springframework.cloud.service;

/**
 * 
 * @author Ramnivas Laddad
 *
 * @param <SC> service connector type
 * @param <SI> service info type
 */
public interface ServiceConnectorCreator<SC, SI extends ServiceInfo> {
	/**
	 * Create service for the given service info and configured with the given
	 * configuration
	 * 
	 * @param serviceInfo
	 * @param serviceConnectorConfiguration
	 * @return
	 */
	SC create(SI serviceInfo, ServiceConnectorConfig serviceConnectorConfiguration);
	
	Class<SC> getServiceConnectorType();

	Class<?> getServiceInfoType();
}

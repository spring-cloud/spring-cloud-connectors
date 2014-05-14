package org.springframework.cloud.service;

/**
 * Configurer for service connector
 * 
 * @author Ramnivas Laddad
 *
 * @param <SC> service connector type
 * @param <SCC> service connector configurer type
 */
public interface ServiceConnectorConfigurer<SC, SCC extends ServiceConnectorConfig> {
	SC configure(SC serviceConnector, SCC config);
}

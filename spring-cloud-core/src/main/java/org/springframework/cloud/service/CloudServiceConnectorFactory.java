package org.springframework.cloud.service;

/**
 * Factory to create service connectors. Typically extended by service connector plugins.
 * 
 * @author Ramnivas Laddad
 *
 * @param <SC> service connector type
 */
public interface CloudServiceConnectorFactory<SC> {
	public String getServiceId();
	
	public SC createService();
}

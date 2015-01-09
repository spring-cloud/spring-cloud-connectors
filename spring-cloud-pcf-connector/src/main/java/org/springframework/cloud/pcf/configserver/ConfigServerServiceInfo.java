package org.springframework.cloud.pcf.configserver;

import org.springframework.cloud.service.UriBasedServiceInfo;

/**
 * Service info to access Config Server services
 *
 * @author Chris Schaefer
 */
public class ConfigServerServiceInfo extends UriBasedServiceInfo {
	public ConfigServerServiceInfo(String id, String uriString) {
		super(id, uriString);
	}
}

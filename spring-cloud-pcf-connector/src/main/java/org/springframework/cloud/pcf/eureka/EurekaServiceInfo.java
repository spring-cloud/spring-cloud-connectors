package org.springframework.cloud.pcf.eureka;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfo;

/**
 * Information to access Eureka services
 *
 * @author Chris Schaefer
 */
@ServiceInfo.ServiceLabel("eureka")
public class EurekaServiceInfo extends UriBasedServiceInfo {
	public EurekaServiceInfo(String id, String uriString) {
		super(id, uriString);
	}
}

package org.springframework.cloud.config.java;

import org.springframework.cloud.config.CloudServicesScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The class that introduces a {@link CloudServicesScanner} bean to scan services
 * 
 * @author Ramnivas Laddad
 * @see ServiceScan
 */
@Configuration
public class DelegatingServiceScanConfiguration {
	@Bean
	public CloudServicesScanner serviceScanner() {
		return new CloudServicesScanner();
	}
}

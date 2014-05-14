package org.springframework.cloud.config.java;

import org.springframework.cloud.config.AbstractCloudConfigServiceScanTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Test for the {@link ServiceScan} annotation
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudServiceScanJavaConfigTest extends AbstractCloudConfigServiceScanTest {
	protected ApplicationContext getTestApplicationContext(ServiceInfo... serviceInfos) {
		return getTestApplicationContext(CloudServiceScanJavaConfigTestConfig.class, serviceInfos);
	}

}

@Configuration
@ServiceScan
class CloudServiceScanJavaConfigTestConfig {
	
}
package org.springframework.cloud.config.java;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.cloud.app.ApplicationInstanceInfo;
import org.springframework.cloud.config.AbstractCloudConfigServiceScanTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Test for the {@link CloudScan} annotation
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudScanJavaConfigTest extends AbstractCloudConfigServiceScanTest {
	protected ApplicationContext getTestApplicationContext(ServiceInfo... serviceInfos) {
		return getTestApplicationContext(CloudScanJavaConfigTestConfig.class, serviceInfos);
	}
	
	@Test
    public void cloudScanIntroducesApplicationInstanceInfo() {
        ApplicationContext testContext = getTestApplicationContext();
        
        assertNotNull(testContext.getBean(ApplicationInstanceInfo.class));
    }
}

@Configuration
@CloudScan
class CloudScanJavaConfigTestConfig {
	
}
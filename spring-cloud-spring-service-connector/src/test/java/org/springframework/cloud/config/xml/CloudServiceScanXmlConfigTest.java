package org.springframework.cloud.config.xml;

import org.springframework.cloud.config.AbstractCloudConfigServiceScanTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudServiceScanXmlConfigTest extends AbstractCloudConfigServiceScanTest {
	protected ApplicationContext getTestApplicationContext(ServiceInfo... serviceInfos) {
		return getTestApplicationContext("cloud-scan.xml", serviceInfos);
	}

}

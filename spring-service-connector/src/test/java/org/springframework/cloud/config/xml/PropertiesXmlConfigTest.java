package org.springframework.cloud.config.xml;

import org.springframework.cloud.config.AbstractCloudConfigPropertiesTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class PropertiesXmlConfigTest extends AbstractCloudConfigPropertiesTest {
	protected ApplicationContext getPropertiesTestApplicationContext(ServiceInfo... serviceInfos) {
		return getTestApplicationContext("cloud-properties.xml", serviceInfos);
	}
}

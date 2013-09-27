package org.springframework.cloud.config.xml;

import org.springframework.cloud.config.AbstractCloudConfigServiceTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractCloudServiceConnectorFactoryParserTest<SC> extends AbstractCloudConfigServiceTest<SC> {
	protected abstract String getWithServiceIdContextFileName();
	
	protected abstract String getWithoutServiceIdContextFileName();

	protected ApplicationContext getTestApplicationContextWithServiceId(ServiceInfo... serviceInfos) {
		return getTestApplicationContext(getWithServiceIdContextFileName(), serviceInfos);
	}

	protected ApplicationContext getTestApplicationContextWithoutServiceId(ServiceInfo... serviceInfos) {
		return getTestApplicationContext(getWithoutServiceIdContextFileName(), serviceInfos);
	}
}

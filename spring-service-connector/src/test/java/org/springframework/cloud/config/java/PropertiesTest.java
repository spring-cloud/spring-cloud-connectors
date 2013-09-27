package org.springframework.cloud.config.java;

import java.util.Properties;

import org.springframework.cloud.config.AbstractCloudConfigPropertiesTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class PropertiesTest extends AbstractCloudConfigPropertiesTest {
	protected ApplicationContext getPropertiesTestApplicationContext(ServiceInfo... serviceInfos) {
		return getTestApplicationContext(CloudPropertiesConfig.class, serviceInfos);
	}
}

class CloudPropertiesConfig extends AbstractCloudConfig {
	@Bean
	public Properties cloudProperties() {
		return properties();
	}
}

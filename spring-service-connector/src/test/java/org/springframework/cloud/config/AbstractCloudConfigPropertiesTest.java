package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Test;
import org.springframework.cloud.StubCloudConnectorTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

public abstract class AbstractCloudConfigPropertiesTest extends StubCloudConnectorTest {
	
	protected abstract ApplicationContext getPropertiesTestApplicationContext(ServiceInfo... serviceInfos);
	
	@Test
	public void cloudPropertiesWithNoServices() {
		ApplicationContext testContext = getPropertiesTestApplicationContext();
		
		Properties cloudProperties = testContext.getBean("cloudProperties", Properties.class);
		assertNotNull(cloudProperties);
	}
	
	@Test
	public void cloudScanWithOneService() {
		ApplicationContext testContext = getPropertiesTestApplicationContext(createMysqlService("db"));
		
		Properties cloudProperties = testContext.getBean("cloudProperties", Properties.class);
		assertNotNull(cloudProperties);
		assertEquals("db", cloudProperties.get("cloud.services.db.id"));
		assertEquals("db", cloudProperties.get("cloud.services.mysql.id"));
	}
}

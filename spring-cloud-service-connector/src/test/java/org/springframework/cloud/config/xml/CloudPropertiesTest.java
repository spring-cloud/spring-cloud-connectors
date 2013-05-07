package org.springframework.cloud.config.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Test;
import org.springframework.cloud.StubCloudConnectorTest;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudPropertiesTest extends StubCloudConnectorTest {
	
	@Test
	public void cloudPropertiesWithNoServices() {
		ApplicationContext testContext = getTestApplicationContext("cloud-properties.xml");
		
		Properties cloudProperties = testContext.getBean("cloudProperties", Properties.class);
		assertNotNull(cloudProperties);
	}
	
	@Test
	public void cloudScanWithOneService() {
		ApplicationContext testContext = getTestApplicationContext("cloud-properties.xml", createMysqlService("db"));
		
		Properties cloudProperties = testContext.getBean("cloudProperties", Properties.class);
		assertNotNull(cloudProperties);
		assertEquals("db", cloudProperties.get("cloud.services.db.id"));
		assertEquals("db", cloudProperties.get("cloud.services.mysql.id"));
	}
}

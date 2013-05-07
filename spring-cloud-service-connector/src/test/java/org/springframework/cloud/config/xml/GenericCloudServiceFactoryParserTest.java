package org.springframework.cloud.config.xml;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.cloud.StubCloudConnectorTest;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class GenericCloudServiceFactoryParserTest extends StubCloudConnectorTest {
	@Test
	public void cloudGenericServiceWithServiceName() {
		ApplicationContext testContext = getTestApplicationContext("cloud-generic-with-service-id.xml",
				createMysqlService("db"));
		
		assertNotNull("Getting service by id", testContext.getBean("db", DataSource.class));
		assertNotNull("Getting service by id", testContext.getBean("db-with-type", DataSource.class));		
	}
	
	@Test
	public void cloudGenericServiceWithoutServiceNameOrTypeSpecified_UniqueServiceExists() {
		ApplicationContext testContext = getTestApplicationContext("cloud-generic-without-service-id.xml",
				createMysqlService("db"));
		
		// just <cloud:service/> should bind to the sole service
		assertNotNull("Getting service by id", testContext.getBean("db", DataSource.class)); 
		assertNotNull("Getting service by id", testContext.getBean("db-without-service-name", DataSource.class));		
	}
}

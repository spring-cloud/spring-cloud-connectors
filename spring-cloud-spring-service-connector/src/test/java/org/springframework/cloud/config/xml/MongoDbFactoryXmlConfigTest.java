package org.springframework.cloud.config.xml;

import org.junit.Test;
import org.springframework.cloud.config.MongoDbFactoryCloudConfigTestHelper;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MongoDbFactoryXmlConfigTest extends AbstractServiceXmlConfigTest<MongoDbFactory> {

	protected ServiceInfo createService(String id) {
		return createMongoService(id);
	}
	
	protected String getWithServiceIdContextFileName() {
		return "cloud-mongo-with-service-id.xml";
	}
	
	protected String getWithoutServiceIdContextFileName() {
		return "cloud-mongo-without-service-id.xml";
	}

	protected Class<MongoDbFactory> getConnectorType() {
		return MongoDbFactory.class;
	}
	
	
	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernNone() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-connectionPerHost50-maxWait200-WriteConcernNone", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, -1, 50, 200);
	}

	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernSafe() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-maxWait200-connectionPerHost50-WriteConcernSafe", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, 1, 50, 200);
	}

	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernUnspecified() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-maxWait200-connectionPerHost50-WriteConcernUnspecified", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, null, 50, 200);
	}

	@Test
	public void withConfigOnlyConnectionPerHostSpecified() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-maxWaitUnspecified-connectionPerHost50-WriteConcernUnspecified", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, null, 50, null);
	}

	@Test
	public void withConfigOnlyMaxWaitSpecified() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-maxWait200-connectionPerHostUnspecified-WriteConcernUnspecified", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, null, null, 200);
	}
}

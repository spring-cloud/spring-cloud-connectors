package org.springframework.cloud.config.java;

import org.junit.Test;
import org.springframework.cloud.config.MongoDbFactoryCloudConfigTestHelper;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.document.MongoDbFactoryConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MongoDbFactoryJavaConfigTest extends AbstractServiceJavaConfigTest<MongoDbFactory> {
	public MongoDbFactoryJavaConfigTest() {
		super(MongoDbFactoryConfigWithId.class, MongoDbFactoryConfigWithoutId.class);
	}
	
	protected ServiceInfo createService(String id) {
		return createMongoService(id);
	}
	
	protected Class<MongoDbFactory> getConnectorType() {
		return MongoDbFactory.class;
	}
	
	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernNone() {
		ApplicationContext testContext = 
				getTestApplicationContext(MongoDbFactoryConfigWithServiceConfig.class, createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("connectionPerHost50_MaxWait200_WriteConcernNone", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, -1, 50, 200);
	}

	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernSafe() {
		ApplicationContext testContext = 
				getTestApplicationContext(MongoDbFactoryConfigWithServiceConfig.class, createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("connectionPerHost50_MaxWait200_WriteConcernSafe", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, 1, 50, 200);
	}

	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernUnspecified() {
		ApplicationContext testContext = 
				getTestApplicationContext(MongoDbFactoryConfigWithServiceConfig.class, createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("connectionPerHost50_MaxWait200_WriteConcernUnspecified", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, null, 50, 200);
	}

	@Test
	public void withConfigOnlyConnectionPerHostSpecified() {
		ApplicationContext testContext = 
				getTestApplicationContext(MongoDbFactoryConfigWithServiceConfig.class, createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("connectionPerHost50_MaxWaitUnspecified_WriteConcernUnspecified", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, null, 50, 120000);
	}

	@Test
	public void withConfigOnlyMaxWaitSpecified() {
		ApplicationContext testContext = 
				getTestApplicationContext(MongoDbFactoryConfigWithServiceConfig.class, createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("connectionPerHostUnspecified_MaxWait200_WriteConcernUnspecified", getConnectorType());
		MongoDbFactoryCloudConfigTestHelper.assertConfigProperties(connector, null, 10 /* default*/, 200);
	}
}

class MongoDbFactoryConfigWithId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public MongoDbFactory testMongoDbFactory() {
		return mongoDbFactory("my-service");
	}
}

class MongoDbFactoryConfigWithoutId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public MongoDbFactory testMongoDbFactory() {
		return mongoDbFactory();
	}
}

class MongoDbFactoryConfigWithServiceConfig extends AbstractCloudConfig {
	@Bean
	public MongoDbFactory connectionPerHost50_MaxWait200_WriteConcernNone() {
		MongoDbFactoryConfig serviceConfig = new MongoDbFactoryConfig("NONE", 50, 200);
		return mongoDbFactory("my-service", serviceConfig);
	}

	@Bean
	public MongoDbFactory connectionPerHost50_MaxWait200_WriteConcernSafe() {
		MongoDbFactoryConfig serviceConfig = new MongoDbFactoryConfig("SAFE", 50, 200);
		return mongoDbFactory("my-service", serviceConfig);
	}

	@Bean
	public MongoDbFactory connectionPerHost50_MaxWait200_WriteConcernUnspecified() {
		MongoDbFactoryConfig serviceConfig = new MongoDbFactoryConfig(null, 50, 200);
		return mongoDbFactory("my-service", serviceConfig);
	}
	
	@Bean
	public MongoDbFactory connectionPerHost50_MaxWaitUnspecified_WriteConcernUnspecified() {
		MongoDbFactoryConfig serviceConfig = new MongoDbFactoryConfig(null, 50, null);
		return mongoDbFactory("my-service", serviceConfig);
	}
	
	@Bean
	public MongoDbFactory connectionPerHostUnspecified_MaxWait200_WriteConcernUnspecified() {
		MongoDbFactoryConfig serviceConfig = new MongoDbFactoryConfig(null, null, 200);
		return mongoDbFactory("my-service", serviceConfig);
	}
}
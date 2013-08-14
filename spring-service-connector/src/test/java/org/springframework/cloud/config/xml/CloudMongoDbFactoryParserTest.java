package org.springframework.cloud.config.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.util.ReflectionTestUtils;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudMongoDbFactoryParserTest extends AbstractCloudServiceConnectorFactoryParserTest<MongoDbFactory> {

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
	
	
	/*
	 * <cloud:mongo-db-factory id="service-connectionPerHost50-maxWait200-WriteConcernNone" service-name="my-service" write-concern="NONE">
	 *   <cloud:mongo-options max-wait-time="200" connections-per-host="50"/>
	 * </cloud:mongo-db-factory>
	 */
	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernNone() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-connectionPerHost50-maxWait200-WriteConcernNone", getConnectorType());
		assertConfigProperties(connector, -1, 50, 200);
	}

	/*
	 * <cloud:mongo-db-factory id="service-maxWait200-connectionPerHost50-WriteConcernSafe" service-name="my-service" write-concern="SAFE">
	 *   <cloud:mongo-options max-wait-time="200" connections-per-host="50"/>
	 * </cloud:mongo-db-factory>
	*/
	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernSafe() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-maxWait200-connectionPerHost50-WriteConcernSafe", getConnectorType());
		assertConfigProperties(connector, 1, 50, 200);
	}

	/*
	 * <cloud:mongo-db-factory id="service-maxWait200-connectionPerHost50-WriteConcernUnspecified" service-name="my-service">
	 *   <cloud:mongo-options max-wait-time="200" connections-per-host="50"/>
	 * </cloud:mongo-db-factory>
	 */
	@Test
	public void withConfigAllOptionsSpecifiedWriteConcernUnspecified() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-maxWait200-connectionPerHost50-WriteConcernUnspecified", getConnectorType());
		assertConfigProperties(connector, null, 50, 200);
	}

	/*
	 * <cloud:mongo-db-factory id="service-maxWaitUnspecified-connectionPerHost50-WriteConcernUnspecified" service-name="my-service">
	 *   <cloud:mongo-options max-wait-time="200" connections-per-host="50"/>
	 * </cloud:mongo-db-factory>
	*/
	@Test
	public void withConfigOnlyConnectionPerHostSpecified() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-maxWaitUnspecified-connectionPerHost50-WriteConcernUnspecified", getConnectorType());
		assertConfigProperties(connector, null, 50, 200);
	}

	/*
	 * <cloud:mongo-db-factory id="service-maxWait200-connectionPerHostUnspecified-WriteConcernUnspecified" service-name="my-service">
	 *   <cloud:mongo-options max-wait-time="200" connections-per-host="50"/>
	 * </cloud:mongo-db-factory>
	 */
	@Test
	public void withConfigOnlyMaxWaitSpecified() {
		ApplicationContext testContext = getTestApplicationContext("cloud-mongo-with-config.xml", createService("my-service"));
		
		MongoDbFactory connector = testContext.getBean("service-maxWait200-connectionPerHostUnspecified-WriteConcernUnspecified", getConnectorType());
		assertConfigProperties(connector, null, 50, 200);
	}

	
	private void assertConfigProperties(MongoDbFactory connector, Integer writeConcernW, int connectionsPerHost, int maxWaitTime) {
		assertNotNull(connector);

		WriteConcern writeConcern = (WriteConcern) ReflectionTestUtils.getField(connector, "writeConcern");
		if (writeConcernW != null) {
			assertNotNull(writeConcern);
			assertEquals(writeConcernW.intValue(), writeConcern.getW());
		}
		
		Mongo mongo = (Mongo) ReflectionTestUtils.getField(connector, "mongo");
		assertEquals(connectionsPerHost, mongo.getMongoOptions().connectionsPerHost);
		assertEquals(maxWaitTime, mongo.getMongoOptions().maxWaitTime);
	}
	
}

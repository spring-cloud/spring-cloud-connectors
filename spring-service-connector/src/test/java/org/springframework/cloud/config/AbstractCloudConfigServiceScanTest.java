package org.springframework.cloud.config;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cloud.StubCloudConnectorTest;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class AbstractCloudConfigServiceScanTest extends StubCloudConnectorTest {
	
	protected abstract ApplicationContext getTestApplicationContext(ServiceInfo... serviceInfos);
	
	@Test(expected=NoSuchBeanDefinitionException.class)
	public void cloudScanWithNoServices() {
		ApplicationContext testContext = getTestApplicationContext();
		
		testContext.getBean(DataSource.class);
	}
	
	@Test
	public void cloudScanWithOneService() {
		ApplicationContext testContext = getTestApplicationContext(createMysqlService("db"));
		
		assertNotNull("Getting service by id", testContext.getBean("db"));
		assertNotNull("Getting service by id and type", testContext.getBean("db", DataSource.class));		
	}

	
	@Test
	public void cloudScanWithTwoServicesOfSameType() {
		ApplicationContext testContext = getTestApplicationContext(createMysqlService("db"), createMysqlService("db2"));
		
		assertNotNull("Getting service by id", testContext.getBean("db"));
		assertNotNull("Getting service by id and type", testContext.getBean("db", DataSource.class));		

		assertNotNull("Getting service by id", testContext.getBean("db2"));
		assertNotNull("Getting service by id and type", testContext.getBean("db2", DataSource.class));		
	}

	
	@Test
	public void cloudScanWithAllTypesOfServices() {
		ApplicationContext testContext = getTestApplicationContext(createMysqlService("mysqlDb"), 
																  createPostgresqlService("postDb"),
																  createMongoService("mongoDb"),
																  createRedisService("redisDb"),
																  createRabbitService("rabbit"));
		
		assertNotNull("Getting service by id", testContext.getBean("mysqlDb"));
		assertNotNull("Getting service by id and type", testContext.getBean("mysqlDb", DataSource.class));		

		assertNotNull("Getting service by id", testContext.getBean("postDb"));
		assertNotNull("Getting service by id and type", testContext.getBean("postDb", DataSource.class));		

		assertNotNull("Getting service by id", testContext.getBean("mongoDb"));
		assertNotNull("Getting service by id and type", testContext.getBean("mongoDb", MongoDbFactory.class));		
		
		assertNotNull("Getting service by id", testContext.getBean("redisDb"));
		assertNotNull("Getting service by id and type", testContext.getBean("redisDb", RedisConnectionFactory.class));		

		assertNotNull("Getting service by id", testContext.getBean("rabbit"));
		assertNotNull("Getting service by id and type", testContext.getBean("rabbit", ConnectionFactory.class));		
	}
	
}

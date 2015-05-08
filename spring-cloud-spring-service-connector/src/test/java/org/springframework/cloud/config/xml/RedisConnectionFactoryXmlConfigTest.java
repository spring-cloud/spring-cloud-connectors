package org.springframework.cloud.config.xml;

import org.junit.Test;
import org.springframework.cloud.service.UriBasedServiceInfo;
import org.springframework.cloud.config.RedisConnectionFactoryCloudConfigTestHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RedisConnectionFactoryXmlConfigTest extends AbstractServiceXmlConfigTest<RedisConnectionFactory> {

	protected UriBasedServiceInfo createService(String id) {
		return createRedisService(id);
	}
	
	protected String getWithServiceIdContextFileName() {
		return "cloud-redis-with-service-id.xml";
	}
	
	protected String getWithoutServiceIdContextFileName() {
		return "cloud-redis-without-service-id.xml";
	}

	protected Class<RedisConnectionFactory> getConnectorType() {
		return RedisConnectionFactory.class;
	}
	
	@Test
	public void cloudRedisConnectionFactoryWithMaxPool() {
		ApplicationContext testContext = getTestApplicationContext("cloud-redis-with-config.xml", createService("my-service"));
		
		RedisConnectionFactory connector = testContext.getBean("service-pool20-wait200", getConnectorType());
		RedisConnectionFactoryCloudConfigTestHelper.assertPoolProperties(connector, 20, 0, 200);
	}
	
	@Test
	public void cloudRedisConnectionFactoryWithMinMaxPool() {
		ApplicationContext testContext = getTestApplicationContext("cloud-redis-with-config.xml", createService("my-service"));
		
		RedisConnectionFactory connector = testContext.getBean("service-pool5-30-wait3000", getConnectorType());
		RedisConnectionFactoryCloudConfigTestHelper.assertPoolProperties(connector, 30, 5, 3000);
	}

	@Test
	public void cloudRedisConnectionFactoryWithTimeout() {
		ApplicationContext testContext = getTestApplicationContext("cloud-redis-with-config.xml", createService("my-service"));

		RedisConnectionFactory connector = testContext.getBean("service-timeout10", getConnectorType());
		RedisConnectionFactoryCloudConfigTestHelper.assertConnectionProperties(connector, 10);
	}

	@Test
	public void cloudRedisConnectionFactoryWithMaxPoolAndTimeout() {
		ApplicationContext testContext = getTestApplicationContext("cloud-redis-with-config.xml", createService("my-service"));

		RedisConnectionFactory connector = testContext.getBean("service-pool30-wait300-timeout20", getConnectorType());
		RedisConnectionFactoryCloudConfigTestHelper.assertPoolProperties(connector, 30, 0, 300);
		RedisConnectionFactoryCloudConfigTestHelper.assertConnectionProperties(connector, 20);
	}
}

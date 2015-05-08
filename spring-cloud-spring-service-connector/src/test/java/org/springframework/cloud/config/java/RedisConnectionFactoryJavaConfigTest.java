package org.springframework.cloud.config.java;

import org.junit.Test;
import org.springframework.cloud.config.RedisConnectionFactoryCloudConfigTestHelper;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig.PoolConfig;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.keyval.RedisConnectionFactoryConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ramnivas Laddad
 * @author Scott Frederick
 */
public class RedisConnectionFactoryJavaConfigTest extends AbstractServiceJavaConfigTest<RedisConnectionFactory> {
	public RedisConnectionFactoryJavaConfigTest() {
		super(RedisConnectionFactoryConfigWithId.class, RedisConnectionFactoryConfigWithoutId.class);
	}

	protected ServiceInfo createService(String id) {
		return createRedisService(id);
	}

	protected Class<RedisConnectionFactory> getConnectorType() {
		return RedisConnectionFactory.class;
	}

	@Test
	public void cloudRedisConnectionFactoryWithMaxPool() {
		ApplicationContext testContext =
				getTestApplicationContext(RedisConnectionFactoryConfigWithServiceConfig.class,
						createService("my-service"));

		RedisConnectionFactory connector = testContext.getBean("pool20Wait200", getConnectorType());
		RedisConnectionFactoryCloudConfigTestHelper.assertPoolProperties(connector, 20, 0, 200);
	}

	@Test
	public void cloudRedisConnectionFactoryWithMinMaxPool() {
		ApplicationContext testContext =
				getTestApplicationContext(RedisConnectionFactoryConfigWithServiceConfig.class,
						createService("my-service"));

		RedisConnectionFactory connector = testContext.getBean("pool5_30Wait3000", getConnectorType());
		RedisConnectionFactoryCloudConfigTestHelper.assertPoolProperties(connector, 30, 5, 3000);
	}

	@Test
	public void cloudRedisConnectionFactoryWithTimeout() {
		ApplicationContext testContext =
				getTestApplicationContext(RedisConnectionFactoryConfigWithServiceConfig.class,
						createService("my-service"));

		RedisConnectionFactory connector = testContext.getBean("timeout10", getConnectorType());
		RedisConnectionFactoryCloudConfigTestHelper.assertConnectionProperties(connector, 10);
	}

	@Test
	public void cloudRedisConnectionFactoryWithWithMaxPoolAndTimeout() {
		ApplicationContext testContext =
				getTestApplicationContext(RedisConnectionFactoryConfigWithServiceConfig.class,
						createService("my-service"));

		RedisConnectionFactory connector = testContext.getBean("pool30Wait300_timeout20", getConnectorType());
		RedisConnectionFactoryCloudConfigTestHelper.assertPoolProperties(connector, 30, 0, 300);
		RedisConnectionFactoryCloudConfigTestHelper.assertConnectionProperties(connector, 20);
	}
}

class RedisConnectionFactoryConfigWithId extends AbstractCloudConfig {
	@Bean(name = "my-service")
	public RedisConnectionFactory testRedisConnectionFactory() {
		return connectionFactory().redisConnectionFactory("my-service");
	}
}

class RedisConnectionFactoryConfigWithoutId extends AbstractCloudConfig {
	@Bean(name = "my-service")
	public RedisConnectionFactory testRedisConnectionFactory() {
		return connectionFactory().redisConnectionFactory();
	}
}

class RedisConnectionFactoryConfigWithServiceConfig extends AbstractCloudConfig {
	@Bean
	public RedisConnectionFactory pool20Wait200() {
		PoolConfig poolConfig = new PoolConfig(20, 200);
		PooledServiceConnectorConfig serviceConfig = new PooledServiceConnectorConfig(poolConfig);
		return connectionFactory().redisConnectionFactory("my-service", serviceConfig);
	}

	@Bean
	public RedisConnectionFactory pool5_30Wait3000() {
		PoolConfig poolConfig = new PoolConfig(5, 30, 3000);
		PooledServiceConnectorConfig serviceConfig = new RedisConnectionFactoryConfig(poolConfig);
		return connectionFactory().redisConnectionFactory("my-service", serviceConfig);
	}

	@Bean
	public RedisConnectionFactory timeout10() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("timeout", 10);
		RedisConnectionFactoryConfig serviceConfig = new RedisConnectionFactoryConfig(properties);
		return connectionFactory().redisConnectionFactory("my-service", serviceConfig);
	}

	@Bean
	public RedisConnectionFactory pool30Wait300_timeout20() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("timeout", 20);
		PoolConfig poolConfig = new PoolConfig(30, 300);
		RedisConnectionFactoryConfig serviceConfig = new RedisConnectionFactoryConfig(poolConfig, properties);
		return connectionFactory().redisConnectionFactory("my-service", serviceConfig);
	}
}
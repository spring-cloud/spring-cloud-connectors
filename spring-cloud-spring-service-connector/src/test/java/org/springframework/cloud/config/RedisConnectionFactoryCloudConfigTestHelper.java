package org.springframework.cloud.config;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.JedisPoolConfig;

import static org.junit.Assert.assertEquals;

/**
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class RedisConnectionFactoryCloudConfigTestHelper extends CommonPoolCloudConfigTestHelper {
	
	public static void assertPoolProperties(RedisConnectionFactory connector, int maxActive, int minIdle, long maxWait) {
		JedisPoolConfig poolConfig = (JedisPoolConfig) ReflectionTestUtils.getField(connector, "poolConfig");
		assertCommonsPoolProperties(poolConfig, maxActive, minIdle, maxWait);
	}

	public static void assertConnectionProperties(RedisConnectionFactory connector, int timeout) {
		JedisConnectionFactory jedisConnector = (JedisConnectionFactory) connector;
		assertEquals(timeout, jedisConnector.getTimeout());
	}

}

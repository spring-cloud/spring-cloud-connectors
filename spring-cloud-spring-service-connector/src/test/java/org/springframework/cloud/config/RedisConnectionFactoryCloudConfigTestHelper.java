package org.springframework.cloud.config;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RedisConnectionFactoryCloudConfigTestHelper extends CommonPoolCloudConfigTestHelper {
	
	public static void assertPoolProperties(RedisConnectionFactory connector, int maxActive, int minIdle, long maxWait) {
		JedisPoolConfig poolConfig = (JedisPoolConfig) ReflectionTestUtils.getField(connector, "poolConfig");
		assertCommonsPoolProperties(poolConfig, maxActive, minIdle, maxWait);
	}
	
}

package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

import redis.clients.jedis.JedisPoolConfig;

public class RedisConnectionFactoryCloudConfigTestHelper {
	
	public static void assertPoolProperties(RedisConnectionFactory connector, int maxActive, int minIdle, long maxWait) {
		JedisPoolConfig poolConfig = (JedisPoolConfig) ReflectionTestUtils.getField(connector, "poolConfig");
		assertEquals(maxActive, ReflectionTestUtils.getField(poolConfig, "maxActive"));
		assertEquals(minIdle, ReflectionTestUtils.getField(poolConfig, "minIdle"));
		assertEquals(maxWait, ReflectionTestUtils.getField(poolConfig, "maxWait"));		
	}
}

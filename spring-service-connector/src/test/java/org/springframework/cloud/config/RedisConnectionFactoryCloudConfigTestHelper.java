package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RedisConnectionFactoryCloudConfigTestHelper {
	
	public static void assertPoolProperties(RedisConnectionFactory connector, int maxActive, int minIdle, long maxWait) {
		JedisPoolConfig poolConfig = (JedisPoolConfig) ReflectionTestUtils.getField(connector, "poolConfig");
		assertEquals(maxActive, getValue(poolConfig, "maxActive", "maxTotal"));
		assertEquals(minIdle, getValue(poolConfig, "minIdle"));
		assertEquals(maxWait, getValue(poolConfig, "maxWait", "maxWaitMillis"));		
	}
	
	private static Object getValue(Object object, String... fieldNames) {
	    for (String fieldName : fieldNames) {
	        try {
	            return ReflectionTestUtils.getField(object, fieldName);
	        } catch (IllegalArgumentException ex) {
	        }
	    }
	    return null;
	}
}

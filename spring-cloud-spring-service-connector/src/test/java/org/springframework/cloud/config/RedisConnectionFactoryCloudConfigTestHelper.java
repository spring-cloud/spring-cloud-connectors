package org.springframework.cloud.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class RedisConnectionFactoryCloudConfigTestHelper extends CommonPoolCloudConfigTestHelper {

	public static void assertPoolProperties(RedisConnectionFactory connector, int maxActive, int minIdle, long maxWait) {
		GenericObjectPoolConfig poolConfig = null;
		if (connector instanceof JedisConnectionFactory) {
			poolConfig = ((JedisConnectionFactory) connector).getPoolConfig();
		} else if (connector instanceof LettuceConnectionFactory) {
			LettuceClientConfiguration config = ((LettuceConnectionFactory) connector).getClientConfiguration();
			if (config instanceof LettucePoolingClientConfiguration) {
				poolConfig = ((LettucePoolingClientConfiguration) config).getPoolConfig();
			}
		}
		assertCommonsPoolProperties(poolConfig, maxActive, minIdle, maxWait);
	}

	public static void assertConnectionProperties(RedisConnectionFactory connector, int timeout) {
		if (connector instanceof JedisConnectionFactory) {
			JedisConnectionFactory connectionFactory = (JedisConnectionFactory) connector;
			assertEquals(timeout, connectionFactory.getTimeout());
		} else  if (connector instanceof LettuceConnectionFactory) {
			LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) connector;
			assertEquals(timeout, connectionFactory.getTimeout());
		}
	}

}

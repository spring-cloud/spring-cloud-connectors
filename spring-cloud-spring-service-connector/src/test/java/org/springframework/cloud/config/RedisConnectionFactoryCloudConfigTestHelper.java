package org.springframework.cloud.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class RedisConnectionFactoryCloudConfigTestHelper extends CommonPoolCloudConfigTestHelper {

	public static void assertNoPoolProperties(RedisConnectionFactory connector) {
		if (connector instanceof JedisConnectionFactory) {
			assertFalse(((JedisConnectionFactory) connector).getUsePool());
		} else if (connector instanceof LettuceConnectionFactory) {
			LettuceClientConfiguration config = ((LettuceConnectionFactory) connector).getClientConfiguration();
			assertThat(config, instanceOf(LettuceClientConfiguration.class));
		}
	}

	public static void assertPoolProperties(RedisConnectionFactory connector, int maxActive, int minIdle, long maxWait) {
		GenericObjectPoolConfig poolConfig = null;
		if (connector instanceof JedisConnectionFactory) {
			poolConfig = ((JedisConnectionFactory) connector).getPoolConfig();
		} else if (connector instanceof LettuceConnectionFactory) {
			LettuceClientConfiguration config = ((LettuceConnectionFactory) connector).getClientConfiguration();
			assertThat(config, instanceOf(LettucePoolingClientConfiguration.class));
			poolConfig = ((LettucePoolingClientConfiguration) config).getPoolConfig();
		}
		assertCommonsPoolProperties(poolConfig, maxActive, minIdle, maxWait);
	}

}

package org.springframework.cloud.service.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.cloud.service.keyval.RedisConnectionFactoryCreator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class RedisServiceConnectorCreatorTest {
	private static final String TEST_HOST = "10.20.30.40";
	private static final int TEST_PORT = 1234;
	private static final String TEST_PASSWORD = "mypass";


	@Mock private RedisServiceInfo mockRedisServiceInfo;
	
	private RedisConnectionFactoryCreator testCreator = new RedisConnectionFactoryCreator();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void cloudRedisCreationNoConfig() {
		RedisServiceInfo serviceInfo = createServiceInfo(RedisServiceInfo.REDIS_SCHEME);

		RedisConnectionFactory dataSource = testCreator.create(serviceInfo, null);

		assertConnectorProperties(serviceInfo, dataSource, false);
	}

	@Test
	public void cloudRedisCreationSecureConnection() {
		RedisServiceInfo serviceInfo = createServiceInfo(RedisServiceInfo.REDISS_SCHEME);

		RedisConnectionFactory dataSource = testCreator.create(serviceInfo, null);

		assertConnectorProperties(serviceInfo, dataSource, true);
	}

	public RedisServiceInfo createServiceInfo(String scheme) {
		when(mockRedisServiceInfo.getScheme()).thenReturn(scheme);
		when(mockRedisServiceInfo.getHost()).thenReturn(TEST_HOST);
		when(mockRedisServiceInfo.getPort()).thenReturn(TEST_PORT);
		when(mockRedisServiceInfo.getPassword()).thenReturn(TEST_PASSWORD);
		
		return mockRedisServiceInfo;
	}

	private void assertConnectorProperties(RedisServiceInfo serviceInfo, RedisConnectionFactory connector,
										   boolean isSecure) {
		assertNotNull(connector);

		if (connector instanceof JedisConnectionFactory) {
			JedisConnectionFactory connectionFactory = (JedisConnectionFactory) connector;
			assertEquals(serviceInfo.getHost(), connectionFactory.getHostName());
			assertEquals(serviceInfo.getPort(), connectionFactory.getPort());
			assertEquals(serviceInfo.getPassword(), connectionFactory.getPassword());
			assertEquals(isSecure, connectionFactory.isUseSsl());
		} else if (connector instanceof LettuceConnectionFactory) {
			LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) connector;
			assertEquals(serviceInfo.getHost(), connectionFactory.getHostName());
			assertEquals(serviceInfo.getPort(), connectionFactory.getPort());
			assertEquals(serviceInfo.getPassword(), connectionFactory.getPassword());
			assertEquals(isSecure, connectionFactory.isUseSsl());
		} else {
			fail("Expected RedisConnectionFactory of type " +
					JedisConnectionFactory.class.getName() + " or " + LettuceConnectionFactory.class.getName() +
					" but was of type " + connector.getClass().getName());
		}
	}
}

package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class RabbitConnectionFactoryCloudConfigTestHelper {
	
	public static void assertConfigProperties(ConnectionFactory connector, Integer channelCacheSize,
											  Integer requestedHeartbeat, Integer connectionTimeout) {
		assertNotNull(connector);

		assertEquals(channelCacheSize, ReflectionTestUtils.getField(connector, "channelCacheSize"));

		Object rabbitConnectionFactory = ReflectionTestUtils.getField(connector, "rabbitConnectionFactory");
		assertNotNull(rabbitConnectionFactory);
		assertEquals(requestedHeartbeat, ReflectionTestUtils.getField(rabbitConnectionFactory, "requestedHeartbeat"));
		assertEquals(connectionTimeout, ReflectionTestUtils.getField(rabbitConnectionFactory, "connectionTimeout"));
	}
}

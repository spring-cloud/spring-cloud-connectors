package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RabbitConnectionFactoryCloudConfigTestHelper {
	
	public static void assertConfigProperties(ConnectionFactory connector, Integer channelCacheSize) {
		assertNotNull(connector);

		assertEquals(channelCacheSize, ReflectionTestUtils.getField(connector, "channelCacheSize"));
	}
}

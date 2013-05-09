package org.springframework.cloud.service.rabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryCreator;
import org.springframework.cloud.service.messaging.RabbitServiceInfo;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RabbitConnectionFactoryCreatorTest {
	private static final String TEST_HOST = "10.20.30.40";
	private static final int TEST_PORT = 1234;
	private static final String TEST_USERNAME = "myuser";
	private static final String TEST_PASSWORD = "mypass";
	private static final String TEST_VH = "myVirtualHost";

	
	private RabbitConnectionFactoryCreator testCreator = new RabbitConnectionFactoryCreator();

	@Test
	public void cloudRabbitCreationNoConfig() throws Exception {
		RabbitServiceInfo serviceInfo = createServiceInfo();

		ConnectionFactory connector = testCreator.create(serviceInfo, null);

		assertConnectorProperties(serviceInfo, connector);
	}

	public RabbitServiceInfo createServiceInfo() {
		return new RabbitServiceInfo("id", TEST_HOST, TEST_PORT, TEST_USERNAME, TEST_PASSWORD, TEST_VH);
	}

	private void assertConnectorProperties(RabbitServiceInfo serviceInfo, ConnectionFactory connector) {
		assertNotNull(connector);
		
		assertEquals(serviceInfo.getHost(), connector.getHost());
		assertEquals(serviceInfo.getPort(), connector.getPort());
		com.rabbitmq.client.ConnectionFactory underlying = (com.rabbitmq.client.ConnectionFactory) ReflectionTestUtils.getField(connector, "rabbitConnectionFactory");
		assertEquals(serviceInfo.getUserName(), ReflectionTestUtils.getField(underlying, "username"));
		assertEquals(serviceInfo.getPassword(), ReflectionTestUtils.getField(underlying, "password"));

		assertEquals(serviceInfo.getVirtualHost(), connector.getVirtualHost());
	}
}

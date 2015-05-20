package org.springframework.cloud.service.rabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.rabbitmq.client.Address;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryCreator;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Ramnivas Laddad
 * @author Scott Frederick
 *
 */
public class RabbitConnectionFactoryCreatorTest {
	private static final String TEST_HOST = "10.20.30.40";
	private static final String TEST_HOST2 = "11.21.31.41";
	private static final int TEST_PORT = 1234;
	private static final int TEST_PORT2 = 5678;
	private static final String TEST_USERNAME = "myuser";
	private static final String TEST_PASSWORD = "mypass";
	private static final String TEST_VH = "myVirtualHost";

	private RabbitConnectionFactoryCreator testCreator = new RabbitConnectionFactoryCreator();

	@Test
	public void cloudRabbitCreationNoUri() throws Exception {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", TEST_HOST, TEST_PORT, TEST_USERNAME, TEST_PASSWORD, TEST_VH);

		ConnectionFactory connector = testCreator.create(serviceInfo, null);

		assertConnectorPropertiesMatchUri(connector, serviceInfo.getUri());
	}

	@Test
	public void cloudRabbitCreationWithUri() throws Exception {
		String userinfo = String.format("%s:%s", TEST_USERNAME, TEST_PASSWORD);
		URI uri = new URI("amqp", userinfo, TEST_HOST, TEST_PORT, "/" + TEST_VH, null, null);
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", uri.toString());

		ConnectionFactory connector = testCreator.create(serviceInfo, null);

		assertConnectorPropertiesMatchUri(connector, serviceInfo.getUri());
	}

	@Test
	public void cloudRabbitCreationWithUris() throws Exception {
		String userinfo = String.format("%s:%s", TEST_USERNAME, TEST_PASSWORD);
		URI uri = new URI("amqp", userinfo, "0.0.0.0", 0, "/" + TEST_VH, null, null);
		URI uri1 = new URI("amqp", userinfo, TEST_HOST, TEST_PORT, "/" + TEST_VH, null, null);
		URI uri2 = new URI("amqp", userinfo, TEST_HOST2, TEST_PORT2, "/" + TEST_VH, null, null);
		List<String> uris = Arrays.asList(uri1.toString(), uri2.toString());
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", uri.toString(), null, uris, null);

		ConnectionFactory connector = testCreator.create(serviceInfo, null);

		assertConnectorPropertiesMatchUri(connector, uri1.toString());
		assertConnectorPropertiesMatchHosts(connector, uris);
	}

	private void assertConnectorPropertiesMatchUri(ConnectionFactory connector, String uriString) throws Exception {
		assertNotNull(connector);

		URI uri = new URI(uriString);
		assertEquals(uri.getHost(), connector.getHost());
		assertEquals(uri.getPort(), connector.getPort());
		com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory =
				(com.rabbitmq.client.ConnectionFactory) ReflectionTestUtils.getField(connector, "rabbitConnectionFactory");
		String[] userInfo = uri.getRawUserInfo().split(":");
		assertEquals(userInfo[0], ReflectionTestUtils.getField(rabbitConnectionFactory, "username"));
		assertEquals(userInfo[1], ReflectionTestUtils.getField(rabbitConnectionFactory, "password"));

		assertTrue(uri.getPath().endsWith(connector.getVirtualHost()));
	}

	private void assertConnectorPropertiesMatchHosts(ConnectionFactory connector, List<String> uriStrings) throws Exception {
		Address[] addresses = (Address[]) ReflectionTestUtils.getField(connector, "addresses");
		assertNotNull(addresses);
		assertEquals(uriStrings.size(), addresses.length);

		for (int i = 0; i < uriStrings.size(); i++) {
			URI uri = new URI(uriStrings.get(i));
			assertEquals(uri.getHost(), addresses[i].getHost());
			assertEquals(uri.getPort(), addresses[i].getPort());
		}
	}
}

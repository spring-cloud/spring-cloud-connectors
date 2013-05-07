package org.springframework.cloud.config.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudRabbitConnectionFactoryParserTest extends AbstractCloudServiceConnectorFactoryParserTest<ConnectionFactory> {

	protected ServiceInfo createService(String id) {
		return createRabbitService(id);
	}
	
	protected String getWithServiceIdContextFileName() {
		return "cloud-rabbit-with-service-id.xml";
	}
	
	protected String getWithoutServiceIdContextFileName() {
		return "cloud-rabbit-without-service-id.xml";
	}

	protected Class<ConnectionFactory> getConnectorType() {
		return ConnectionFactory.class;
	}
	
	@Test
	public void clouRabbitConnectionFactoryWithChannelCacheSize() {
		ApplicationContext testContext = getTestApplicationContext("cloud-rabbit-with-config.xml", createService("my-service"));
		
		ConnectionFactory connector = testContext.getBean("service-channelCacheSize200", getConnectorType());
		assertConfigProperties(connector, 200);
	}
	
	private void assertConfigProperties(ConnectionFactory connector, Integer channelCacheSize) {
		assertNotNull(connector);

		assertEquals(channelCacheSize, ReflectionTestUtils.getField(connector, "channelCacheSize"));
	}
}

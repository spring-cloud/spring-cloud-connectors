package org.springframework.cloud.config.xml;

import org.junit.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.RabbitConnectionFactoryCloudConfigTestHelper;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

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
	public void cloudRabbitConnectionFactoryWithChannelCacheSize() {
		ApplicationContext testContext = getTestApplicationContext("cloud-rabbit-with-config.xml", createService("my-service"));
		
		ConnectionFactory connector = testContext.getBean("service-channelCacheSize200", getConnectorType());
		RabbitConnectionFactoryCloudConfigTestHelper.assertConfigProperties(connector, 200);
	}
	
}

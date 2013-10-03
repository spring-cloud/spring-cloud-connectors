package org.springframework.cloud.config.java;

import org.junit.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.RabbitConnectionFactoryCloudConfigTestHelper;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RabbitConnectionFactoryJavaConfigTest extends AbstractServiceJavaConfigTest<ConnectionFactory> {
	public RabbitConnectionFactoryJavaConfigTest() {
		super(RabbitConnectionFactoryConfigWithId.class, RabbitConnectionFactoryConfigWithoutId.class);
	}
	
	protected ServiceInfo createService(String id) {
		return createRabbitService(id);
	}
	
	protected Class<ConnectionFactory> getConnectorType() {
		return ConnectionFactory.class;
	}
	
	@Test
	public void cloudRabbitConnectionFactoryWithChannelCacheSize() {
		ApplicationContext testContext = 
			getTestApplicationContext(RabbitConnectionFactoryConfigWithServiceConfig.class, createService("my-service"));
		
		ConnectionFactory connector = testContext.getBean("channelCacheSize200", getConnectorType());
		RabbitConnectionFactoryCloudConfigTestHelper.assertConfigProperties(connector, 200);
	}
}

class RabbitConnectionFactoryConfigWithId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public ConnectionFactory testRabbitConnectionFactory() {
		return rabbitConnectionFactory("my-service");
	}
}

class RabbitConnectionFactoryConfigWithoutId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public ConnectionFactory testRabbitConnectionFactory() {
		return rabbitConnectionFactory();
	}
}

class RabbitConnectionFactoryConfigWithServiceConfig extends AbstractCloudConfig {
	@Bean
	public ConnectionFactory channelCacheSize200() {
		RabbitConnectionFactoryConfig serviceConfig = new RabbitConnectionFactoryConfig(200);
		return rabbitConnectionFactory("my-service", serviceConfig);		
	}
}
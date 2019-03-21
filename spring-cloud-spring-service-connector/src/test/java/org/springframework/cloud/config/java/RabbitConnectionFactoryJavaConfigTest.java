/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.cloud.config.java;

import static org.springframework.cloud.config.RabbitConnectionFactoryCloudConfigTestHelper.DEFAULT_CHANNEL_CACHE_SIZE;

import java.util.HashMap;
import java.util.Map;

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
 * @author Scott Frederick
 * @author Gary Russell
 *
 */
public class RabbitConnectionFactoryJavaConfigTest extends AbstractServiceJavaConfigTest<ConnectionFactory> {
	public RabbitConnectionFactoryJavaConfigTest() {
		super(RabbitConnectionFactoryConfigWithId.class, RabbitConnectionFactoryConfigWithoutId.class);
	}

	@Override
	protected ServiceInfo createService(String id) {
		return createRabbitService(id);
	}

	@Override
	protected Class<ConnectionFactory> getConnectorType() {
		return ConnectionFactory.class;
	}

	@Test
	public void cloudRabbitConnectionFactoryWithConfig() {
		ApplicationContext testContext =
			getTestApplicationContext(RabbitConnectionFactoryConfigWithServiceConfig.class, createService("my-service"));

		ConnectionFactory connector = testContext.getBean("connectionFactoryWithConfig", getConnectorType());
		RabbitConnectionFactoryCloudConfigTestHelper.assertConfigProperties(connector, 200, -1, -1);
	}

	@Test
	public void cloudRabbitConnectionFactoryWithProperties() {
		ApplicationContext testContext =
			getTestApplicationContext(RabbitConnectionFactoryConfigWithServiceConfig.class, createService("my-service"));

		ConnectionFactory connector = testContext.getBean("connectionFactoryWithProperties", getConnectorType());
		RabbitConnectionFactoryCloudConfigTestHelper.assertConfigProperties(connector, DEFAULT_CHANNEL_CACHE_SIZE, 5, 10);
	}

	@Test
	public void cloudRabbitConnectionFactoryWithConfigAndProperties() {
		ApplicationContext testContext =
			getTestApplicationContext(RabbitConnectionFactoryConfigWithServiceConfig.class, createService("my-service"));

		ConnectionFactory connector = testContext.getBean("connectionFactoryWithConfigAndProperties", getConnectorType());
		RabbitConnectionFactoryCloudConfigTestHelper.assertConfigProperties(connector, 300, 15, 20);
	}
}

class RabbitConnectionFactoryConfigWithId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public ConnectionFactory testRabbitConnectionFactory() {
		return connectionFactory().rabbitConnectionFactory("my-service");
	}
}

class RabbitConnectionFactoryConfigWithoutId extends AbstractCloudConfig {
	@Bean(name="my-service")
	public ConnectionFactory testRabbitConnectionFactory() {
		return connectionFactory().rabbitConnectionFactory();
	}
}

class RabbitConnectionFactoryConfigWithServiceConfig extends AbstractCloudConfig {
	@Bean
	public ConnectionFactory connectionFactoryWithConfig() {
		RabbitConnectionFactoryConfig serviceConfig = new RabbitConnectionFactoryConfig(200);
		return connectionFactory().rabbitConnectionFactory("my-service", serviceConfig);
	}

	@Bean
	public ConnectionFactory connectionFactoryWithProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("requestedHeartbeat", 5);
		properties.put("connectionTimeout", 10);
		RabbitConnectionFactoryConfig serviceConfig = new RabbitConnectionFactoryConfig(properties);
		return connectionFactory().rabbitConnectionFactory("my-service", serviceConfig);
	}

	@Bean
	public ConnectionFactory connectionFactoryWithConfigAndProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("requestedHeartbeat", 15);
		properties.put("connectionTimeout", 20);
		RabbitConnectionFactoryConfig serviceConfig = new RabbitConnectionFactoryConfig(properties, 300);
		return connectionFactory().rabbitConnectionFactory("my-service", serviceConfig);
	}
}

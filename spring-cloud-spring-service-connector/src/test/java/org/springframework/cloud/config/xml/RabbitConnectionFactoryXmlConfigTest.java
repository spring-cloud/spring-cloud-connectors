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
package org.springframework.cloud.config.xml;

import static org.springframework.cloud.config.RabbitConnectionFactoryCloudConfigTestHelper.DEFAULT_CHANNEL_CACHE_SIZE;

import org.junit.Test;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.RabbitConnectionFactoryCloudConfigTestHelper;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ramnivas Laddad
 * @author Gary Russell
 *
 */
public class RabbitConnectionFactoryXmlConfigTest extends AbstractServiceXmlConfigTest<ConnectionFactory> {

	@Override
	protected ServiceInfo createService(String id) {
		return createRabbitService(id);
	}

	@Override
	protected String getWithServiceIdContextFileName() {
		return "cloud-rabbit-with-service-id.xml";
	}

	@Override
	protected String getWithoutServiceIdContextFileName() {
		return "cloud-rabbit-without-service-id.xml";
	}

	@Override
	protected Class<ConnectionFactory> getConnectorType() {
		return ConnectionFactory.class;
	}

	@Test
	public void cloudRabbitConnectionFactoryWithConfiguration() {
		ApplicationContext testContext = getTestApplicationContext("cloud-rabbit-with-config.xml", createService("my-service"));

		ConnectionFactory connector = testContext.getBean("service-channelCacheSize200", getConnectorType());
		RabbitConnectionFactoryCloudConfigTestHelper.assertConfigProperties(connector, 200, -1, -1);
	}

	@Test
	public void cloudRabbitConnectionFactoryWithProperties() {
		ApplicationContext testContext = getTestApplicationContext("cloud-rabbit-with-config.xml", createService("my-service"));

		ConnectionFactory connector = testContext.getBean("service-properties", getConnectorType());
		RabbitConnectionFactoryCloudConfigTestHelper.assertConfigProperties(connector, DEFAULT_CHANNEL_CACHE_SIZE, 5, 10);
	}

	@Test
	public void cloudRabbitConnectionFactoryWithConfigurationAndProperties() {
		ApplicationContext testContext = getTestApplicationContext("cloud-rabbit-with-config.xml", createService("my-service"));

		ConnectionFactory connector = testContext.getBean("service-channelCacheSize200-properties", getConnectorType());
		RabbitConnectionFactoryCloudConfigTestHelper.assertConfigProperties(connector, 200, 5, 10);
	}
}

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
package org.springframework.cloud.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Ramnivas Laddad
 * @author Scott Frederick
 * @author Gary Russell
 *
 */
public class RabbitConnectionFactoryCloudConfigTestHelper {
	public static final Integer DEFAULT_CHANNEL_CACHE_SIZE =
			(Integer) ReflectionTestUtils.getField(new org.springframework.amqp.rabbit.connection.CachingConnectionFactory(), "channelCacheSize");

	private final static Integer DEFAULT_FACTORY_TIMEOUT =
			(Integer) ReflectionTestUtils.getField(new com.rabbitmq.client.ConnectionFactory(), "connectionTimeout");

	private final static Integer DEFAULT_FACTORY_HEARTBEAT =
			(Integer) ReflectionTestUtils.getField(new com.rabbitmq.client.ConnectionFactory(), "requestedHeartbeat");

	public static void assertConfigProperties(ConnectionFactory connector, Integer channelCacheSize,
											  Integer requestedHeartbeat, Integer connectionTimeout) {
		Integer timeoutToTest = connectionTimeout < 0 ? DEFAULT_FACTORY_TIMEOUT : connectionTimeout;
		Integer heartBeatToTest = requestedHeartbeat < 0 ? DEFAULT_FACTORY_HEARTBEAT : requestedHeartbeat;

		assertNotNull(connector);

		assertEquals(channelCacheSize, ReflectionTestUtils.getField(connector, "channelCacheSize"));

		Object rabbitConnectionFactory = ReflectionTestUtils.getField(connector, "rabbitConnectionFactory");
		assertNotNull(rabbitConnectionFactory);
		assertEquals(heartBeatToTest, ReflectionTestUtils.getField(rabbitConnectionFactory, "requestedHeartbeat"));
		assertEquals(timeoutToTest, ReflectionTestUtils.getField(rabbitConnectionFactory, "connectionTimeout"));
	}
}

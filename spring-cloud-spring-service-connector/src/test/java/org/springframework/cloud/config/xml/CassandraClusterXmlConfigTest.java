/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.config.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;

/**
 * @author Vinicius Carvalho
 */
public class CassandraClusterXmlConfigTest extends AbstractServiceXmlConfigTest<Cluster> {

	@Test
	public void cassandraSessionWithConfiguration() throws Exception {
		ApplicationContext testContext = getTestApplicationContext(
				"cloud-cassandra-with-config.xml", createService("my-service"));
		Cluster cluster = testContext.getBean("cassandra-full-config",
				getConnectorType());

		assertNotNull(cluster.getConfiguration().getSocketOptions());
		assertEquals(15000,
				cluster.getConfiguration().getSocketOptions().getConnectTimeoutMillis());
		assertTrue(DefaultRetryPolicy.class.isAssignableFrom(
				cluster.getConfiguration().getPolicies().getRetryPolicy().getClass()));
		assertTrue(RoundRobinPolicy.class.isAssignableFrom(cluster.getConfiguration()
				.getPolicies().getLoadBalancingPolicy().getClass()));
		assertTrue(ConstantReconnectionPolicy.class.isAssignableFrom(cluster
				.getConfiguration().getPolicies().getReconnectionPolicy().getClass()));
	}

	@Override
	protected String getWithServiceIdContextFileName() {
		return "cloud-cassandra-with-service-id.xml";
	}

	@Override
	protected String getWithoutServiceIdContextFileName() {
		return "cloud-cassandra-without-service-id.xml";
	}

	@Override
	protected ServiceInfo createService(String id) {
		return createCassandraService(id);
	}

	@Override
	protected Class<Cluster> getConnectorType() {
		return Cluster.class;
	}
}

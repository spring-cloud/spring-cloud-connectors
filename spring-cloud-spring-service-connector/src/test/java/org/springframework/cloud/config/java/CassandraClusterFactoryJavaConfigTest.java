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

package org.springframework.cloud.config.java;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.column.CassandraClusterConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.SocketOptions;

/**
 * @author Mark Paluch
 */
public class CassandraClusterFactoryJavaConfigTest
		extends AbstractServiceJavaConfigTest<Cluster> {

	public CassandraClusterFactoryJavaConfigTest() {
		super(CassandraClusterConfigWithId.class, CassandraClusterConfigWithoutId.class);
	}

	protected ServiceInfo createService(String id) {
		return createCassandraService(id);
	}

	protected Class<Cluster> getConnectorType() {
		return Cluster.class;
	}

	@Test
	public void cloudCassandraConnectionFactoryConfig() {
		ApplicationContext testContext = getTestApplicationContext(
				CassandraClusterConfigWithServiceConfig.class,
				createService("my-service"));

		Cluster connector = testContext.getBean("my-service",
				getConnectorType());

		assertThat(connector.getConfiguration().getSocketOptions().getSendBufferSize(),
				is(12345));
	}
}

class CassandraClusterConfigWithId extends AbstractCloudConfig {
	@Bean(name = "my-service")
	public Cluster testClusterFactory() {
		return connectionFactory().cluster("my-service");
	}
}

class CassandraClusterConfigWithoutId extends AbstractCloudConfig {
	@Bean(name = "my-service")
	public Cluster testClusterFactory() {
		return connectionFactory().cluster();
	}
}

class CassandraClusterConfigWithServiceConfig extends AbstractCloudConfig {
	@Bean(name = "my-service")
	public Cluster testClusterFactoryWithConfig() {

		CassandraClusterConfig config = new CassandraClusterConfig();
		SocketOptions socketOptions = new SocketOptions();
		socketOptions.setSendBufferSize(12345);
		config.setSocketOptions(socketOptions);

		return connectionFactory().cluster("my-service", config);
	}
}

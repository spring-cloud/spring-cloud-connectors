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

package org.springframework.cloud.service.column;

import java.util.Arrays;

import org.mockito.Mock;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactoryTest;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.CassandraServiceInfo;

import com.datastax.driver.core.Cluster;

/**
 * 
 * @author Mark Paluch
 */
public class CassandraClusterFactoryTest extends
		AbstractCloudServiceConnectorFactoryTest<CassandraClusterFactory, Cluster, CassandraServiceInfo> {
	@Mock
	Cluster mockConnector;

	public CassandraClusterFactory createTestCloudServiceConnectorFactory(String id,
			ServiceConnectorConfig config) {
		return new CassandraClusterFactory(id, config);
	}

	public Class<Cluster> getConnectorType() {
		return Cluster.class;
	}

	public Cluster getMockConnector() {
		return mockConnector;
	}

	public CassandraServiceInfo getTestServiceInfo(String id) {
		return new CassandraServiceInfo(id, Arrays.asList("127.0.0.1"), 9042, "user",
				"pass");
	}
}

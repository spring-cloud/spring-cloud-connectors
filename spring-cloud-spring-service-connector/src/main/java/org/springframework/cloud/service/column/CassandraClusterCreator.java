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

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreator;
import org.springframework.cloud.service.common.CassandraServiceInfo;
import org.springframework.util.StringUtils;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;

/**
 * {@link ServiceConnectorCreator} implementation to provide a Cassandra {@link Cluster}.
 * Allows optionally to apply a {@link CassandraClusterConfig} configuration.
 *
 * @author Mark Paluch
 */
public class CassandraClusterCreator
		extends AbstractServiceConnectorCreator<Cluster, CassandraServiceInfo> {

	@Override
	public Cluster create(CassandraServiceInfo serviceInfo,
			ServiceConnectorConfig serviceConnectorConfig) {

		Builder builder = Cluster.builder()
				.addContactPoints(serviceInfo.getContactPoints().toArray(new String[0]))
				.withPort(serviceInfo.getPort());

		if (StringUtils.hasText(serviceInfo.getUsername())) {
			builder.withCredentials(serviceInfo.getUsername(), serviceInfo.getPassword());
		}

		if (serviceConnectorConfig instanceof CassandraClusterConfig) {

			CassandraClusterConfig config = (CassandraClusterConfig) serviceConnectorConfig;

			if (config.getCompression() != null) {
				builder.withCompression(config.getCompression());
			}

			builder.withPoolingOptions(config.getPoolingOptions());
			builder.withSocketOptions(config.getSocketOptions());
			builder.withQueryOptions(config.getQueryOptions());
			builder.withNettyOptions(config.getNettyOptions());
			builder.withLoadBalancingPolicy(config.getLoadBalancingPolicy());
			builder.withReconnectionPolicy(config.getReconnectionPolicy());
			builder.withRetryPolicy(config.getRetryPolicy());
			builder.withProtocolVersion(config.getProtocolVersion());

			if (!config.isMetricsEnabled()) {
				builder.withoutMetrics();
			}

			if (!config.isJmxReportingEnabled()) {
				builder.withoutJMXReporting();
			}
		}

		return builder.build();
	}
}

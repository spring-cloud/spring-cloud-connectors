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

import org.springframework.cloud.service.ServiceConnectorConfig;

import com.datastax.driver.core.NettyOptions;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolOptions.Compression;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;

/**
 * Configuration for a Cassandra {@link com.datastax.driver.core.Cluster}.
 *
 * @author Mark Paluch
 */
public class CassandraClusterConfig implements ServiceConnectorConfig {

	public static final boolean DEFAULT_METRICS_ENABLED = true;
	public static final boolean DEFAULT_JMX_REPORTING_ENABLED = true;

	// Protocol options
	private Compression compression;
	private NettyOptions nettyOptions;
	private ProtocolVersion protocolVersion;

	// Policies
	private LoadBalancingPolicy loadBalancingPolicy;
	private ReconnectionPolicy reconnectionPolicy;
	private RetryPolicy retryPolicy;

	private PoolingOptions poolingOptions;
	private QueryOptions queryOptions;
	private SocketOptions socketOptions;

	private boolean metricsEnabled = DEFAULT_METRICS_ENABLED;
	private boolean jmxReportingEnabled = DEFAULT_JMX_REPORTING_ENABLED;

	public Compression getCompression() {
		return compression;
	}

	public void setCompression(Compression compression) {
		this.compression = compression;
	}

	public NettyOptions getNettyOptions() {
		return nettyOptions;
	}

	public void setNettyOptions(NettyOptions nettyOptions) {
		this.nettyOptions = nettyOptions;
	}

	public ProtocolVersion getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(ProtocolVersion protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public LoadBalancingPolicy getLoadBalancingPolicy() {
		return loadBalancingPolicy;
	}

	public void setLoadBalancingPolicy(LoadBalancingPolicy loadBalancingPolicy) {
		this.loadBalancingPolicy = loadBalancingPolicy;
	}

	public ReconnectionPolicy getReconnectionPolicy() {
		return reconnectionPolicy;
	}

	public void setReconnectionPolicy(ReconnectionPolicy reconnectionPolicy) {
		this.reconnectionPolicy = reconnectionPolicy;
	}

	public RetryPolicy getRetryPolicy() {
		return retryPolicy;
	}

	public void setRetryPolicy(RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	public PoolingOptions getPoolingOptions() {
		return poolingOptions;
	}

	public void setPoolingOptions(PoolingOptions poolingOptions) {
		this.poolingOptions = poolingOptions;
	}

	public QueryOptions getQueryOptions() {
		return queryOptions;
	}

	public void setQueryOptions(QueryOptions queryOptions) {
		this.queryOptions = queryOptions;
	}

	public SocketOptions getSocketOptions() {
		return socketOptions;
	}

	public void setSocketOptions(SocketOptions socketOptions) {
		this.socketOptions = socketOptions;
	}

	public boolean isMetricsEnabled() {
		return metricsEnabled;
	}

	public void setMetricsEnabled(boolean metricsEnabled) {
		this.metricsEnabled = metricsEnabled;
	}

	public boolean isJmxReportingEnabled() {
		return jmxReportingEnabled;
	}

	public void setJmxReportingEnabled(boolean jmxReportingEnabled) {
		this.jmxReportingEnabled = jmxReportingEnabled;
	}
}

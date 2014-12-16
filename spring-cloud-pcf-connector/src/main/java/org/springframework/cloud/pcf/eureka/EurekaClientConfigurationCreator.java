package org.springframework.cloud.pcf.eureka;

import com.netflix.discovery.EurekaClientConfig;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Connector creator for Eureka client services
 *
 * @author Chris Schaefer
 */
public class EurekaClientConfigurationCreator extends AbstractServiceConnectorCreator<EurekaClientConfig, EurekaServiceInfo> {
	@Override
	public EurekaClientConfig create(EurekaServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		return getEurekaClientConfig(serviceInfo);
	}

	protected EurekaClientConfig getEurekaClientConfig(EurekaServiceInfo serviceInfo) {
		return new DefaultPcfEurekaClientConfig(serviceInfo.getUri());
	}

	private static final class DefaultPcfEurekaClientConfig implements EurekaClientConfig {
		private static final int MINUTES = 60;
		private static final String REGION = "default";
		private static final String DEFAULT_ZONE = "defaultZone";
		private static final String EUREKA_API_PREFIX = "/eureka/";

		private final String uri;

		public DefaultPcfEurekaClientConfig(String uri) {
			this.uri = uri + EUREKA_API_PREFIX;
		}

		@Override
		public int getRegistryFetchIntervalSeconds() {
			return 5;
		}

		@Override
		public int getInstanceInfoReplicationIntervalSeconds() {
			return 30;
		}

		@Override
		public int getInitialInstanceInfoReplicationIntervalSeconds() {
			return 40;
		}

		@Override
		public int getEurekaServiceUrlPollIntervalSeconds() {
			return 5 * MINUTES;
		}

		@Override
		public String getProxyHost() {
			return null;
		}

		@Override
		public String getProxyPort() {
			return null;
		}

		@Override
		public boolean shouldGZipContent() {
			return true;
		}

		@Override
		public int getEurekaServerReadTimeoutSeconds() {
			return 8;
		}

		@Override
		public int getEurekaServerConnectTimeoutSeconds() {
			return 5;
		}

		@Override
		public String getBackupRegistryImpl() {
			return null;
		}

		@Override
		public int getEurekaServerTotalConnections() {
			return 200;
		}

		@Override
		public int getEurekaServerTotalConnectionsPerHost() {
			return 50;
		}

		@Override
		public String getEurekaServerURLContext() {
			return null;
		}

		@Override
		public String getEurekaServerPort() {
			return null;
		}

		@Override
		public String getEurekaServerDNSName() {
			return null;
		}

		@Override
		public boolean shouldUseDnsForFetchingServiceUrls() {
			return false;
		}

		@Override
		public boolean shouldRegisterWithEureka() {
			return true;
		}

		@Override
		public boolean shouldPreferSameZoneEureka() {
			return true;
		}

		@Override
		public boolean shouldLogDeltaDiff() {
			return false;
		}

		@Override
		public boolean shouldDisableDelta() {
			return false;
		}

		@Nullable
		@Override
		public String fetchRegistryForRemoteRegions() {
			return null;
		}

		@Override
		public String getRegion() {
			return REGION;
		}

		@Override
		public String[] getAvailabilityZones(String region) {
			return new String[] { DEFAULT_ZONE };
		}

		@Override
		public List<String> getEurekaServerServiceUrls(String myZone) {
			return Arrays.asList(uri);
		}

		@Override
		public boolean shouldFilterOnlyUpInstances() {
			return true;
		}

		@Override
		public int getEurekaConnectionIdleTimeoutSeconds() {
			return 30;
		}

		@Override
		public boolean shouldFetchRegistry() {
			return true;
		}

		@Nullable
		@Override
		public String getRegistryRefreshSingleVipAddress() {
			return null;
		}

		@Override
		public int getHeartbeatExecutorThreadPoolSize() {
			return 2;
		}

		@Override
		public int getCacheRefreshExecutorThreadPoolSize() {
			return 2;
		}
	}
}

package org.springframework.cloud.pcf.eureka;

import com.netflix.discovery.EurekaClientConfig;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test cases around the Eureka client configuration creator
 *
 * @author Chris Schaefer
 */
public class EurekaClientConfigurationCreatorTest {
	private static final String REGION = "default";
	private static final String SERVICE_INFO_ID = "id";
	private static final int REGISTRY_FETCH_INTERVAL_SECS = 5;
	private static final String AVAILABLITY_ZONE = "defaultZone";
	private static final String URI = "http://user:pass@192.168.23.4:1234";
	private static final String EUREKA_API_PREFIX = "/eureka/";

	private EurekaClientConfigurationCreator eurekaClientConfigurationCreator = new EurekaClientConfigurationCreator();

	@Test
	public void testClientConfiguration() {
		EurekaServiceInfo eurekaServiceInfo = new EurekaServiceInfo(SERVICE_INFO_ID, URI);

		EurekaClientConfig eurekaClientConfig = eurekaClientConfigurationCreator.create(eurekaServiceInfo, null);
		List<String> serviceUrls = eurekaClientConfig.getEurekaServerServiceUrls(null);

		assertEquals(1, serviceUrls.size());
		assertEquals(URI + EUREKA_API_PREFIX, serviceUrls.get(0));
		assertEquals(REGION, eurekaClientConfig.getRegion());
		assertEquals(REGISTRY_FETCH_INTERVAL_SECS, eurekaClientConfig.getRegistryFetchIntervalSeconds());

		String[] availablityZones = eurekaClientConfig.getAvailabilityZones(null);

		assertEquals(1, availablityZones.length);
		assertEquals(AVAILABLITY_ZONE, availablityZones[0]);
	}
}

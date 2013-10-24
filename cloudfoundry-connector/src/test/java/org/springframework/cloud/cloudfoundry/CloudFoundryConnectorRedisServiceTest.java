package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getRedisServicePayload;
import static org.springframework.cloud.cloudfoundry.CloudFoundryConnectorTestHelper.getServicesPayload;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;

public class CloudFoundryConnectorRedisServiceTest extends AbstractCloudFactoryConnectorTest {
	@Test
	public void redisServiceCreation() {
		String[] versions = {"2.0", "2.2"};
		for (String version : versions) {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getRedisServicePayload(version, "redis-1", hostname, port, password, "redis-db"),
						getRedisServicePayload(version, "redis-2", hostname, port, password, "redis-db")));
		}

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "redis-1"));
		assertNotNull(getServiceInfo(serviceInfos, "redis-2"));
	}
}

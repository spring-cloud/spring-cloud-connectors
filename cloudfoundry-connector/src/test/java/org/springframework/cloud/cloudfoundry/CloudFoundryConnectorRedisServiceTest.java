package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFoundryConnectorRedisServiceTest extends AbstractCloudFoundryConnectorTest {
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
	
	private String getRedisServicePayload(String version, String serviceName,
                                			 String hostname, int port,
                                			 String password, String name) {
		String payload = readTestDataFile("test-redis-info.json");
		payload = payload.replace("$version", version);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$password", password);
		payload = payload.replace("$name", name);
		
		return payload;
	}
}

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
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
			.thenReturn(getServicesPayload(
					getRedisServicePayload("redis-1", hostname, port, password, "redis-db"),
					getRedisServicePayload("redis-2", hostname, port, password, "redis-db")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "redis-1"));
		assertNotNull(getServiceInfo(serviceInfos, "redis-2"));
	}

	@Test
	public void redisServiceCreationNoLabelNoTags() {
		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getRedisServicePayloadNoLabelNoTags("redis-1", hostname, port, password, "redis-db"),
						getRedisServicePayloadNoLabelNoTags("redis-2", hostname, port, password, "redis-db")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "redis-1"));
		assertNotNull(getServiceInfo(serviceInfos, "redis-2"));
	}

	private String getRedisServicePayload(String serviceName,
										  String hostname, int port,
										  String password, String name) {
		return getRedisServicePayload("test-redis-info.json", serviceName, hostname, port, password, name);
	}

	private String getRedisServicePayloadNoLabelNoTags(String serviceName,
													   String hostname, int port,
													   String password, String name) {
		return getRedisServicePayload("test-redis-info-no-label-no-tags.json", serviceName, hostname, port, password, name);
	}

	private String getRedisServicePayload(String payloadFile, String serviceName,
										  String hostname, int port,
										  String password, String name) {
		String payload = readTestDataFile(payloadFile);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$password", password);
		payload = payload.replace("$name", name);

		return payload;
	}
}
